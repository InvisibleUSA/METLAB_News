package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.*;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import me.metlabnews.Model.ResourceManagement.IResource;
import me.metlabnews.Presentation.IUserInterface.IFetchPendingVerificationRequestsEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericFailureEvent;
import me.metlabnews.Presentation.IUserInterface.IGetStringArrayEvent;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;
import me.metlabnews.Presentation.UserDataRepresentation;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;



public class UserManager
{
	public static UserManager getInstance()
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
	public void registerNewSubscriber(Session session, IGenericEvent onSuccess,
	                                  IGenericFailureEvent onFailure,
	                                  String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean requestAdminStatus)
	{
		GetSubscriberQuery subscriberQuery = new GetSubscriberQuery(email);
		boolean emailIsAlreadyTaken = subscriberQuery.execute();
		if(emailIsAlreadyTaken)
		{
			onFailure.execute(Messages.EmailAddressAlreadyInUse);
			return;
		}

		GetOrganisationQuery organisationQuery =
				new GetOrganisationQuery(organisationName);
		boolean organisationExists = organisationQuery.execute();
		if(!organisationExists)
		{
			onFailure.execute(Messages.UnknownOrganisation);
			return;
		}

		if(!Validator.validateEmailAddress(email))
		{
			onFailure.execute(Messages.InvalidEmailAddress);
			return;
		}
		if(!Validator.validatePassword(password))
		{
			onFailure.execute(Messages.PasswordDoesNotMatchRequirements);
			return;
		}

		Subscriber subscriber = new Subscriber(email, password, firstName, lastName,
		                                       organisationQuery.getResult(), requestAdminStatus);
		AddSubscriberQuery query = new AddSubscriberQuery(subscriber);
		if(!query.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		session.login(subscriber);
		onSuccess.execute();
	}


	public void subscriberLogin(Session session,
	                            IGenericEvent onSuccess,
	                            IGenericEvent onVerificationPending,
	                            IGenericFailureEvent onFailure,
	                            String email, String password)
	{
		GetSubscriberQuery query = new GetSubscriberQuery(email);
		if(!query.execute())
		{
			onFailure.execute(Messages.UnknownEmail);
			return;
		}

		Subscriber subscriber = query.getResult();
		String correctPassword = subscriber.getPassword();


		if(password.equals(correctPassword))
		{
			session.login(subscriber);
			if(subscriber.isVerificationPending())
			{
				onVerificationPending.execute();
			}
			else
			{
				onSuccess.execute();
			}
		}
		else
		{
			onFailure.execute(Messages.WrongPassword);
		}
	}


	public void removeSubscriber(Session session, IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String subscriberEmail)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		Subscriber subscriber;
		boolean removesHimself = session.getUser().getEmail().equals(subscriberEmail);
		if(removesHimself)
		{
			subscriber = (Subscriber)session.getUser();
		}
		else
		{
			boolean isAdmin = ((Subscriber)session.getUser()).isOrganisationAdministrator();
			if(!isAdmin)
			{
				onFailure.execute(Messages.NotClientAdmin);
				return;
			}
			GetSubscriberQuery fetchQuery = new GetSubscriberQuery(subscriberEmail);
			if(!fetchQuery.execute())
			{
				onFailure.execute(Messages.UnknownEmail);
				return;
			}
			subscriber = fetchQuery.getResult();

			boolean sameOrganisation = ((Subscriber)session.getUser()).
					getOrganisationId().getName().equals(subscriber.getOrganisationId().getName());
			if(!sameOrganisation)
			{
				onFailure.execute(Messages.IllegalOperation);
				return;
			}
		}

		RemoveSubscriberQuery removeQuery = new RemoveSubscriberQuery(subscriber);
		if(removeQuery.execute())
		{
			if(removesHimself)
			{
				session.logout(onSuccess);
			}
			else
			{
				onSuccess.execute();
			}
		}
		else
		{
			onFailure.execute(Messages.UnknownError);
		}
	}


	// endregion Subscriber Interaction


	// region Client Admin Interaction

	public void getPendingVerificationRequests(Session session,
	                                           IFetchPendingVerificationRequestsEvent onSuccess,
	                                           IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}
		Organisation organisation = ((Subscriber)session.getUser()).getOrganisationId();
		GetSubscribersOfOrganisationQuery query =
				new GetSubscribersOfOrganisationQuery(organisation);
		query.execute();

		List<UserDataRepresentation> table = new ArrayList<>();
		for(Subscriber subscriber : query.getResult())
		{
			if(subscriber.isVerificationPending())
			{
				table.add(new UserDataRepresentation(subscriber));
			}
		}
		onSuccess.execute(table.toArray(new UserDataRepresentation[table.size()]));
	}

	public void verifySubscriber(Session session,
	                             IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String subscriberEmail,
	                             boolean grantAdminStatus)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		GetSubscriberQuery query = new GetSubscriberQuery(subscriberEmail);
		if(!query.execute())
		{
			onFailure.execute(Messages.UnknownEmail);
			return;
		}
		Subscriber subscriber = query.getResult();

		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		if(subscriber.isVerificationPending())
		{
			subscriber.setVerificationPending(false);
			if(subscriber.isOrganisationAdministrator())
			{
				subscriber.setOrganisationAdministrator(grantAdminStatus);
			}
			UpdateSubscriberQuery updateQuery = new UpdateSubscriberQuery(subscriber);
			if(!updateQuery.execute())
			{
				onFailure.execute(Messages.UnknownError);
			}
			onSuccess.execute();
		}
		else
		{
			onFailure.execute(Messages.UserIsAlreadyVerified);
		}
	}


	public void denySubscriberVerification(Session session,
	                                       IGenericEvent onSuccess,
	                                       IGenericFailureEvent onFailure,
	                                       String subscriberEmail)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		GetSubscriberQuery fetchQuery = new GetSubscriberQuery(subscriberEmail);
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownEmail);
			return;
		}
		Subscriber subscriber = new Subscriber();

		String subscriberOrganisationName = subscriber.getOrganisationId().getName();
		String adminOrganisationName = subscriber.getOrganisationId().getName();
		if(!subscriberOrganisationName.equals(adminOrganisationName))
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		if(subscriber.isVerificationPending())
		{
			RemoveSubscriberQuery removalQuery = new RemoveSubscriberQuery(subscriber);
			if(!removalQuery.execute())
			{
				onFailure.execute(Messages.UnknownError);
			}
			onSuccess.execute();
		}
		else
		{
			onFailure.execute(Messages.UserIsAlreadyVerified);
		}
	}

	// endregion Client Admin Interaction


	// region System Admin Interaction

	public void systemAdministratorLogin(Session session,
	                                     IGenericEvent onSuccess,
	                                     IGenericFailureEvent onFailure,
	                                     String email, String password)
	{
		GetSystemAdministratorQuery query = new GetSystemAdministratorQuery(email);
		if(!query.execute())
		{
			onFailure.execute(Messages.UnknownEmail);
			return;
		}

		SystemAdministrator admin = query.getResult();
		String correctPassword = admin.getPassword();

		if(password.equals(correctPassword))
		{
			session.login(admin);
			onSuccess.execute();
		}
		else
		{
			onFailure.execute(Messages.WrongPassword);
		}
	}

	public void addOrganisation(Session session,
	                            IGenericEvent onSuccess,
	                            IGenericFailureEvent onFailure,
	                            String organisationName,
	                            String adminFirstName, String adminLastName,
	                            String adminEmail, String adminPassword)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != SystemAdministrator.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}

		GetOrganisationQuery organisationQuery = new GetOrganisationQuery(organisationName);
		boolean nameIsAlreadyTaken = organisationQuery.execute();
		if(nameIsAlreadyTaken)
		{
			onFailure.execute(Messages.OrganisationNameAlreadyTaken);
			return;
		}

		GetSubscriberQuery subscriberQuery = new GetSubscriberQuery(adminEmail);
		boolean emailIsAlreadyTaken = subscriberQuery.execute();
		if(emailIsAlreadyTaken)
		{
			onFailure.execute(Messages.EmailAddressAlreadyInUse);
			return;
		}

		Organisation organisation = new Organisation(organisationName);
		AddOrganisationQuery addOrganisationQuery = new AddOrganisationQuery(organisation);
		if(!addOrganisationQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
		}

		Subscriber admin = new Subscriber(adminEmail, adminPassword, adminFirstName, adminLastName,
		                                       organisation, true);
		admin.setVerificationPending(false);

		AddSubscriberQuery addSubscriberQuery = new AddSubscriberQuery(admin);
		if(addSubscriberQuery.execute())
		{
			onSuccess.execute();
		}
		else
		{
			onFailure.execute(Messages.UnknownError);
		}
	}

	public void removeOrganisation(Session session,
	                               IGenericEvent onSuccess,
	                               IGenericFailureEvent onFailure,
	                               String organisationName)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != SystemAdministrator.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}

		GetOrganisationQuery fetchQuery = new GetOrganisationQuery(organisationName);
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownOrganisation);
			return;
		}

		Organisation organisation = fetchQuery.getResult();
		RemoveOrganisationQuery removeQuery = new RemoveOrganisationQuery(organisation);
		if(removeQuery.execute())
		{
			onSuccess.execute();
		}
		else
		{
			onFailure.execute(Messages.UnknownError);
		}
	}

	public void getAllOrganisations(Session session, IGetStringArrayEvent onSuccess,
	                                IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != SystemAdministrator.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}
		GetAllOrganisationsQuery query = new GetAllOrganisationsQuery();
		if(query.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		List<Organisation> resultSet = query.getResult();
		List<String> list = new ArrayList<>();
		for(Organisation organisation : resultSet)
		{
			list.add(organisation.getName());
		}
		onSuccess.execute(list.toArray(new String[list.size()]));
	}

	// endregion Client Admin Interaction


	public static class Validator implements IResource
	{
		public static Validator getInstance()
		{
			if(m_instance == null)
			{
				m_instance = new Validator();
			}
			return m_instance;
		}


		@Override
		public void initialize()
		{
			ConfigurationManager config = ConfigurationManager.getInstance();
			m_minimumPasswordLength = config.getSecurityPasswordMinimumLength();
			m_lowerCaseLetterIsRequired = config.getSecurityPasswordLowerCaseLetterRequired();
			m_upperCaseLetterIsRequired = config.getSecurityPasswordUpperCaseLetterRequired();
			m_digitIsRequired = config.getSecurityPasswordDigitRequired();
			m_specialCharacterIsRequired = config.getSecurityPasswordSpecialCharacterRequired();
			m_whitespaceIsForbidden = config.getSecurityPasswordWhitespaceForbidden();
		}

		@Override
		public void close()
		{

		}


		private static boolean validatePassword(String password)
		{
			boolean valid;
			if(password.length() < m_minimumPasswordLength)
			{
				valid = false;
			}
			else
			{
				StringBuilder regex = new StringBuilder("([^.]+?)");
				if(m_digitIsRequired) regex.append("(?=.*[0-9])");
				if(m_lowerCaseLetterIsRequired)regex.append("(?=.*[a-z])");
				if(m_upperCaseLetterIsRequired)regex.append("(?=.*[A-Z])");
				if(m_specialCharacterIsRequired)regex.append("(?=.*[@#$%^&+=])");
				if(m_whitespaceIsForbidden)regex.append("(?=\\S+$)");
				valid = password.matches(regex.toString());
			}
			return valid;
		}

		private static boolean validateEmailAddress(String address)
		{
			return EmailValidator.getInstance().isValid(address);
		}



		private static Validator m_instance = null;

		// region password requirements
		private static int     m_minimumPasswordLength;
		private static boolean m_digitIsRequired;
		private static boolean m_lowerCaseLetterIsRequired;
		private static boolean m_upperCaseLetterIsRequired;
		private static boolean m_specialCharacterIsRequired;
		private static boolean m_whitespaceIsForbidden;
		// endregion password requirements
	}



	private static UserManager m_instance              = null;
}
