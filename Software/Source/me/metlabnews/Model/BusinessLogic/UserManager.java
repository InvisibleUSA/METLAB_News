package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Queries.*;
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
	}


    // region Subscriber Interaction
	public void registerNewSubscriber(Session session, String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean asClientAdmin)
	{
		GetSubscriberQuery subscriberQuery = new GetSubscriberQuery(email);
		boolean emailIsAlreadyTaken = !subscriberQuery.execute();
		if(emailIsAlreadyTaken)
		{
			session.subscriberRegistrationFailedEvent(Messages.EmailAddressAlreadyInUse);
			return;
		}

		GetOrganisationQuery organisationQuery =
				new GetOrganisationQuery(organisationName);
		boolean organisationExists = organisationQuery.execute();
		if(!organisationExists)
		{
			session.subscriberRegistrationFailedEvent(Messages.UnknownOrganisation);
			return;
		}

		Subscriber subscriber = new Subscriber(email, password, firstName, lastName,
		                                       organisationQuery.getResult(), asClientAdmin);
		AddSubscriberQuery query = new AddSubscriberQuery(subscriber);
		if(!query.execute())
		{
			session.subscriberRegistrationFailedEvent(Messages.UnknownError);
		}
		session.setUser(subscriber);
		session.subscriberVerificationPendingEvent();
	}


	public void subscriberLogin(Session session, String email, String password)
	{
		GetSubscriberQuery query = new GetSubscriberQuery(email);
		if(!query.execute())
		{
			session.subscriberLoginFailedEvent(Messages.UnknownEmail);
			return;
		}

		Subscriber subscriber = query.getResult();
		String correctPassword = subscriber.getPassword();


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
		GetSubscribersOfOrganisationQuery query =
				new GetSubscribersOfOrganisationQuery(organisation);
		query.execute();

		List<SubscriberDataRepresentation> table = new ArrayList<>();
		for(Subscriber subscriber : query.getResult())
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

		GetSubscriberQuery query = new GetSubscriberQuery(subscriberEmail);
		if(!query.execute())
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownEmail);
			return;
		}
		Subscriber subscriber = query.getResult();

		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			session.subscriberVerificationFailedEvent(Messages.IllegalOperation);
			return;
		}
		if(subscriber.isVerificationPending())
		{
			subscriber.setVerificationPending(false);
			UpdateSubscriberQuery updateQuery = new UpdateSubscriberQuery(subscriber);
			if(!updateQuery.execute())
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

		GetSubscriberQuery fetchQuery = new GetSubscriberQuery(subscriberEmail);
		if(!fetchQuery.execute())
		{
			session.subscriberVerificationFailedEvent(Messages.UnknownEmail);
			return;
		}
		Subscriber subscriber = new Subscriber();

		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			session.subscriberVerificationFailedEvent(Messages.IllegalOperation);
			return;
		}
		if(subscriber.isVerificationPending())
		{
			subscriber.setVerificationPending(false);
			UpdateSubscriberQuery updateQuery = new UpdateSubscriberQuery(subscriber);
			if(!updateQuery.execute())
			{
				session.subscriberVerificationFailedEvent(Messages.UnknownError);
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
		GetSystemAdministratorQuery query = new GetSystemAdministratorQuery(email);
		if(!query.execute())
		{
			session.subscriberLoginFailedEvent(Messages.UnknownEmail);
			return;
		}

		SystemAdministrator admin = query.getResult();
		String correctPassword = admin.getPassword();

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

		GetOrganisationQuery organisationQuery = new GetOrganisationQuery(organisationName);
		boolean nameIsAlreadyTaken = organisationQuery.execute();
		if(nameIsAlreadyTaken)
		{
			session.addingOrganisationFailedEvent(Messages.OrganisationNameAlreadyTaken);
			return;
		}

		Organisation organisation = new Organisation(organisationName);
		AddOrganisationQuery addQuery = new AddOrganisationQuery(organisation);
		if(addQuery.execute())
		{
			session.addingOrganisationSuccessfulEvent();
		}
		else
		{
			session.addingOrganisationFailedEvent(Messages.UnknownError);
		}
	}

	public void deleteOrganisation(Session session, String organisationName)
	{
		GetOrganisationQuery fetchQuery = new GetOrganisationQuery(organisationName);
		if(!fetchQuery.execute())
		{
			session.deletingOrganisationFailedEvent(Messages.UnknownOrganisation);
			return;
		}

		Organisation organisation = fetchQuery.getResult();
		RemoveOrganisationQuery removeQuery = new RemoveOrganisationQuery(organisation);
		if(removeQuery.execute())
		{
			session.deletingOrganisationSuccessfulEvent();
		}
		else
		{
			session.deletingOrganisationFailedEvent(Messages.UnknownError);
		}
	}

	// endregion Client Admin Interaction



	private static UserManager m_instance = null;
}
