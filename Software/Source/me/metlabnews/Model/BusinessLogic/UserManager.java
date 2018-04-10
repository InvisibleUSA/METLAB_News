package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.DataAccess.MariaConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedDataException;
import me.metlabnews.Model.DataAccess.Queries.QueryAddUser;
import me.metlabnews.Model.DataAccess.Queries.QueryGetOrganisation;
import me.metlabnews.Model.DataAccess.Queries.QueryGetUser;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;
import me.metlabnews.Presentation.SubscriberDataRepresentation;

import java.util.ArrayList;
import java.util.List;



public class UserManager
{
	public static UserManager create()
	{
		if(m_instance == null)
		{
			m_instance = new UserManager();
		}
		return m_instance;
	}


	private UserManager()
	{
		//m_dbConnector = MariaConnector.getInstance();
	}


	// region Subscriber Interaction
	public void registerNewSubscriber(Session session, String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean clientAdmin)
	{
		if(userExists(email))
		{
			return; //TODO: Implement Error User exists
		}
		if(!organisationExists(organisationName))
		{
			return; //TODO: Implement Error Oranization does not exist
		}
		if(checkPassworRequirements(password) != Passwordrequirements.NONE)
		{
			return; //TODO: Implement Passwordrequirements not met
		}

		QueryAddUser qau = new QueryAddUser();
		qau.email = email;
		qau.password = password;
		qau.firstName = firstName;
		qau.lastName = lastName;
		qau.organisationName = organisationName;
		qau.clientAdmin = clientAdmin;
		if(!qau.execute())
		{
			; //TODO: Error
		}
	}

	private Passwordrequirements checkPassworRequirements(String password)
	{
		if(password.length() < 5)
		{
			return Passwordrequirements.LENGTH;
		}
		return Passwordrequirements.NONE;
	}

	private boolean organisationExists(String organisationName)
	{
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		qgo.organisationName = organisationName;
		if(!qgo.execute())
		{
			return false; //TODO: Error handling
		}
		return qgo.organisationExists;
	}

	private boolean userExists(String email)
	{
		QueryGetUser qgu = new QueryGetUser();
		qgu.email = email;
		if(qgu.execute())
		{
			return false; //TODO: Error handling
		}
		return qgu.userExists;
	}

	public void subscriberLogin(Session session, String email, String password)
	{
		Subscriber subscriber;
		String     correctPassword;
		try
		{
			subscriber = m_dbConnector.getSubscriberByEmail(email);
			correctPassword = subscriber.getPassword();
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberLoginFailedEvent(Messages.UnknownEmail);
			return;
		}
		catch(UnexpectedDataException e)
		{
			session.subscriberLoginFailedEvent(Messages.UnknownError);
			return;
		}
		if(password.equals(correctPassword))
		{
			session.setUser(subscriber);
			if(subscriber.isVerificationPending())
			{
				session.subscriberVerificationPendingEvent();
			}
			else
			{
				session.subscriberLoginSuccessfulEvent(
						subscriber.isOrganisationAdministrator());
			}
		}
		else
		{
			session.subscriberLoginFailedEvent(Messages.WrongPassword);
		}
	}

	// endregion Subscriber Interaction


	// region Client Admin Interaction

	public void getPendingVerificationRequests(Session session)
	{
		if(!session.isLoggedIn())
		{
			session.getPendingVerificationRequestsFailedEvent(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			session.getPendingVerificationRequestsFailedEvent(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			session.getPendingVerificationRequestsFailedEvent(Messages.NotClientAdmin);
			return;
		}
		Organisation organisation = ((Subscriber)session.getUser()).getOrganisationId();
		Subscriber[] matchingSubscribers =
				m_dbConnector.getAllSubscribersOfOrganisation(organisation);
		List<SubscriberDataRepresentation> table = new ArrayList<>();

		for(Subscriber subscriber : matchingSubscribers)
		{
			table.add(new SubscriberDataRepresentation(subscriber.getEmail(),
			                                           subscriber.getFirstName(),
			                                           subscriber.getLastName(),
			                                           subscriber.isOrganisationAdministrator(),
			                                           true));
		}
		session.getPendingVerificationRequestsSuccessfulEvent(
				table.toArray(new SubscriberDataRepresentation[table.size()]));
	}

	public void verifySubscriber(Session session, String subscriberEmail)
	{
		// TODO: replace redundant code

		if(!session.isLoggedIn())
		{
			session.subscriberVerificationFailedEvent(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			session.subscriberVerificationFailedEvent(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			session.subscriberVerificationFailedEvent(Messages.NotClientAdmin);
			return;
		}
		Subscriber subscriber;
		try
		{
			subscriber = m_dbConnector.getSubscriberByEmail(subscriberEmail);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownEmail);
			return;
		}
		catch(UnexpectedDataException e)
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownError);
			return;
		}
		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName      = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			session.subscriberVerificationFailedEvent(Messages.IllegalOperation);
		}
		if(subscriber.isVerificationPending())
		{
			subscriber.setVerificationPending(false);
			try
			{
				m_dbConnector.updateSubscriber(subscriber);
			}
			catch(DataUpdateFailedException e)
			{
				session.subscriberVerificationFailedEvent(Messages.UnknownError);
			}
			session.subscriberVerificationSuccessfulEvent();
		}
		else
		{
			session.subscriberVerificationFailedEvent(Messages.UserIsAlreadyVerified);
		}
	}


	// TODO: merge with verifySubscriber()
	public void denySubscriberVerification(Session session, String subscriberEmail)
	{
		if(!session.isLoggedIn())
		{
			session.subscriberVerificationFailedEvent(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			session.subscriberVerificationFailedEvent(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			session.subscriberVerificationFailedEvent(Messages.NotClientAdmin);
			return;
		}
		Subscriber subscriber;
		//TODO: replace redundant code
		try
		{
			subscriber = m_dbConnector.getSubscriberByEmail(subscriberEmail);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownEmail);
			return;
		}
		catch(UnexpectedDataException e)
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownError);
			return;
		}
		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName      = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			session.subscriberVerificationFailedEvent(Messages.IllegalOperation);
		}
		if(subscriber.isVerificationPending())
		{
			try
			{
				m_dbConnector.deleteSubscriber(subscriber);
			}
			catch(RequestedDataDoesNotExistException e)
			{
				session.subscriberVerificationFailedEvent(Messages.UnknownError);
				return;
			}
			session.subscriberVerificationDenialSuccessfulEvent();
		}
		else
		{
			session.subscriberVerificationFailedEvent(Messages.UserIsAlreadyVerified);
		}
	}

	// endregion Client Admin Interaction


	// region System Admin Interaction

	public void systemAdministratorLogin(Session session, String email, String password)
	{
		SystemAdministrator admin;
		String              correctPassword;
		try
		{
			admin = m_dbConnector.getSystemAdministratorByEmail(email);
			correctPassword = admin.getPassword();
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberLoginFailedEvent(Messages.UnknownEmail);
			return;
		}
		catch(UnexpectedDataException e)
		{
			session.subscriberLoginFailedEvent(Messages.UnknownError);
			return;
		}
		if(password.equals(correctPassword))
		{
			session.setUser(admin);
			session.sysAdminLoginSuccessfulEvent();
		}
		else
		{
			session.sysAdminLoginFailedEvent(Messages.WrongPassword);
		}
	}

	public void addOrganisation(Session session, String organisationName)
	{
		if(!session.isLoggedIn())
		{
			session.subscriberVerificationFailedEvent(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != SystemAdministrator.class)
		{
			session.subscriberVerificationFailedEvent(Messages.NotSystemAdmin);
			return;
		}
		boolean nameIsAlreadyTaken = true;
		try
		{
			m_dbConnector.getSubscriberByEmail(organisationName);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			// TODO: more efficient "already in use" check (avoid intended exceptions)
			nameIsAlreadyTaken = false;
		}
		catch(UnexpectedDataException e)
		{
			session.addingOrganisationFailedEvent(Messages.UnknownError);
			return;
		}
		if(nameIsAlreadyTaken)
		{
			session.addingOrganisationFailedEvent(Messages.OrganisationNameAlreadyTaken);
			return;
		}
		Organisation organisation = new Organisation(organisationName);
		try
		{
			m_dbConnector.addOrganisation(organisation);
			session.addingOrganisationSuccessfulEvent();
		}
		catch(DataCouldNotBeAddedException e)
		{
			session.addingOrganisationFailedEvent(Messages.UnknownError);
		}
	}

	public void deleteOrganisation(Session session, String organisationName)
	{
		Organisation organisation;
		try
		{
			organisation = m_dbConnector.getOrganisationByName(organisationName);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.deletingOrganisationFailedEvent(Messages.UnknownOrganisation);
			return;
		}
		try
		{
			m_dbConnector.deleteOrganisation(organisation);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.deletingOrganisationFailedEvent(Messages.UnknownError);
			return;
		}
		session.deletingOrganisationSuccessfulEvent();
	}

	// endregion Client Admin Interaction



	private static UserManager    m_instance = null;
	private        MariaConnector m_dbConnector;
}
