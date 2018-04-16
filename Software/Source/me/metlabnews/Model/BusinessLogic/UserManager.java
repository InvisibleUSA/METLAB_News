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

	/**
	 * Register a new subscriber, who is an employee of an already registered organisation.
	 * In case of success the subscriber is added to a database and is pending for verification.
	 *
	 * @param session            session associated with the caller
	 * @param onSuccess          function to execute in case of success
	 *                              (the subscriber is now registered)
	 * @param onFailure          function to execute in case of failure:
	 *                           - email is already used by another user
	 *                           - email is invalid
	 *                           - organisationName is not registered
	 *                           - password does not meet security requirements
	 * @param email              email address (cannot be used by multiple subscribers)
	 * @param password           password (has to meet the security requirements set in [Settings.xml])
	 * @param firstName          first and possibly middle name
	 * @param lastName           last name
	 * @param organisationName   name of a registered organisation
	 * @param requestAdminStatus indicates if the subscriber requests to be administrator of
	 *                           the organisation
	 */
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


	/**
	 * Log in an already registered subscriber.
	 *
	 * @param session               session associated with the caller
	 * @param onSuccess             function to execute in case of success
	 *                              (subscriber is now logged in)
	 * @param onVerificationPending function to execute in case
	 *                              the subscribers verification is pending
	 *                              (the subscriber is now still not logged in)
	 * @param onFailure             function to execute in case of failure:
	 *                              - email is not associated with a registered subscriber
	 *                              - password is incorrect
	 * @param email                 email address of a registered subscriber
	 * @param password              password
	 */
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


	/**
	 * Remove a registered subscriber.
	 * Can be called from any subscriber to quit his own account or from an organisation
	 * administrator to quit am employee's account.
	 * In case of success the subscriber is deleted from a database and will not be able
	 * to login (again).
	 *
	 * @param session            session associated with the caller
	 * @param onSuccess          function to execute in case of success
	 *                           - if the caller removed himself:
	 *                              the caller is now logged out
	 *                           - if the caller removed somebody else:
	 *                              the removed subscriber is now potentially
	 *                              still logged in but will not be able
	 *                              to login again once he logged out.
	 * @param onFailure          function to execute in case of failure:
	 *                           - caller is not logged in
	 *                           - caller tries to remove somebody else but has not the
	 *                             privileges to do so
	 *                           - email is not associated with a registered subscriber
	 *                           - caller tries to remove a subscriber of another organisation
	 * @param subscriberEmail    email address of the subscriber to remove
	 *                           the specified subscriber has to be in the same organisation as
	 *                           the caller
	 *                           (if it matches the caller's email address, the caller removes
	 *                           himself)
	 */
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

	/**
	 * Fetch all registered subscribers (within the same organisation as the caller)
	 * who have not yet been verified or denied.
	 * Caller has to be administrator of an organisation.
	 *
	 * @param session            session associated with the caller
	 * @param onSuccess          function to execute in case of success
	 *                           (the result is passed as UserDataRepresentation[])
	 * @param onFailure          function to execute in case of failure:
	 *                           - caller is not logged in
	 *                           - caller does not have the required privileges
	 */
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
		// TODO: outsource to separate query?
		for(Subscriber subscriber : query.getResult())
		{
			if(subscriber.isVerificationPending())
			{
				table.add(new UserDataRepresentation(subscriber));
			}
		}
		onSuccess.execute(table.toArray(new UserDataRepresentation[table.size()]));
	}


	/**
	 * Grant a pending verification request.
	 * Caller has to be administrator of an organisation.
	 *
	 * @param session            session associated with the caller
	 * @param onSuccess          function to execute in case of success
	 *                           (verified subscriber can now log in)
	 * @param onFailure          function to execute in case of failure:
	 *                           - caller is not logged in
	 *                           - caller does not have the required privileges
	 *                           - email is not associated with a registered subscriber
	 *                           - caller tries to verify a subscriber of another organisation
	 *                           - the subscriber has already been verified
	 * @param subscriberEmail    email address of a registered subscriber
	 * @param grantAdminStatus   allow the subscriber to be an organisation administrator
	 *                           in case subscriber has requested that
	 */
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


	/**
	 * Deny a pending verification request.
	 * Caller has to be administrator of an organisation.
	 *
	 * @param session            session associated with the caller
	 * @param onSuccess          function to execute in case of success
	 *                           (rejected subscriber is now removed)
	 * @param onFailure          function to execute in case of failure:
	 *                           - caller is not logged in
	 *                           - caller does not have the required privileges
	 *                           - email is not associated with a registered subscriber
	 *                           - caller tries to reject a subscriber of another organisation
	 *                           - the subscriber has already been verified
	 * @param subscriberEmail    email address of a registered subscriber
	 */
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

	/**
	 * Log in a registered system administrator.
	 *
	 * @param session               session associated with the caller
	 * @param onSuccess             function to execute in case of success
	 *                              (caller is now logged in)
	 * @param onFailure             function to execute in case of failure:
	 *                              - email is not associated with a system administrator
	 *                              - password is incorrect
	 * @param email                 email address of a system administrator
	 * @param password              password
	 */
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



	/**
	 * Add a new organisation and register an initial organisation administrator.
	 * Caller has to be system administrator.
	 *
	 * @param session               session associated with the caller
	 * @param onSuccess             function to execute in case of success
	 *                              (organisation was added and organisation administrator was registered)
	 * @param onFailure             function to execute in case of failure:
	 *                              - caller is not logged in
	 *                              - caller does not have the required privileges
	 *                              - organisationName is already used by another organisation
	 *                              - adminEmail is already used by another user
	 *                              - adminPassword does not meet security requirements
	 * @param organisationName      name of the organisation, has to be unique
	 * @param adminFirstName        first (and potentially second) name of initial organisation administrator
	 * @param adminLastName         last name of initial organisation administrator
	 * @param adminEmail            email of initial organisation administrator
	 *                                 (must not be used by another user)
	 * @param adminPassword         password of initial organisation administrator
	 */
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

		if(!Validator.validateEmailAddress(adminEmail))
		{
			onFailure.execute(Messages.InvalidEmailAddress);
			return;
		}
		if(!Validator.validatePassword(adminPassword))
		{
			onFailure.execute(Messages.PasswordDoesNotMatchRequirements);
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


	/**
	 * Remove a registered organisation and all its associated subscribers.
	 * Caller has to be system administrator.
	 *
	 * @param session               session associated with the caller
	 * @param onSuccess             function to execute in case of success
	 *                              (organisation and all associated subscribers were removed)
	 * @param onFailure             function to execute in case of failure:
	 *                              - caller is not logged in
	 *                              - caller does not have the required privileges
	 *                              - organisationName is not associated with a registered organisation
	 * @param organisationName      name of the organisation to remove
	 */
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
		// TODO: make sure that all associated subscribers get deleted
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


	/**
	 * Get all registered organisations.
	 * Caller has to be system administrator.
	 *
	 * @param session               session associated with the caller
	 * @param onSuccess             function to execute in case of success
	 *                              (the result is passed as String[],
	 *                              containing the name of each organisation)
	 * @param onFailure             function to execute in case of failure:
	 *                              - caller is not logged in
	 *                              - caller does not have the required privileges
	 */
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


		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
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



	private static UserManager m_instance = null;
}
