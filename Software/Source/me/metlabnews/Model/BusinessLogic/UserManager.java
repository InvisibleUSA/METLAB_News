package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.DataCouldNotBeAddedException;
import me.metlabnews.Model.DataAccess.DataUpdateFailedException;
import me.metlabnews.Model.DataAccess.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.UnexpectedDataException;
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
		m_dbConnector = RelationalDbConnector.getInstance();
	}


    // region Subscriber Interaction
	public void registerNewSubscriber(Session session, String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean clientAdmin)
	{
		boolean emailIsAlreadyTaken = true;
		try
		{
			m_dbConnector.getSubscriberByEmail(email);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			emailIsAlreadyTaken = false;
		}
		catch(UnexpectedDataException e)
		{
			session.subscriberRegistrationFailedEvent(Messages.UnknownError);
			return;
		}
		if(emailIsAlreadyTaken)
		{
			session.subscriberRegistrationFailedEvent(Messages.EmailAddressAlreadyInUse);
			return;
		}

		Organisation organisation;
		try
		{
			organisation = m_dbConnector.getOrganisationByName(organisationName);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberRegistrationFailedEvent(Messages.UnknownOrganisation);
			return;
		}
		Subscriber subscriber = new Subscriber(email, password, firstName, lastName,
		                                       organisation, clientAdmin);
		session.setUser(subscriber);
		try
		{
			m_dbConnector.addSubscriber(subscriber);
		}
		catch(DataCouldNotBeAddedException e)
		{
			session.subscriberRegistrationFailedEvent(Messages.UnknownError);
		}
		session.subscriberVerificationPendingEvent();
	}


	public void subscriberLogin(Session session, String email, String password)
	{
		Subscriber subscriber;
		String correctPassword;
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
		String adminOrganisationName = subscriber.getOrganisationId().getName();
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
		String adminOrganisationName = subscriber.getOrganisationId().getName();
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



	private static UserManager m_instance = null;
	private RelationalDbConnector m_dbConnector;
}
