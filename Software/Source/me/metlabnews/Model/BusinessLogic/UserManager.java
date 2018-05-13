package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.*;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import me.metlabnews.Model.ResourceManagement.IResource;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericFailureEvent;
import me.metlabnews.Presentation.IUserInterface.IGetStringArrayEvent;
import me.metlabnews.Presentation.UserDataRepresentation;
import org.apache.commons.validator.routines.EmailValidator;

// TODO: FOR FUCKS SAKE! Why the heck is anything SQL specific used here?
import java.sql.Date;
import java.sql.SQLException;



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
		Logger.getInstance().register(UserManager.class, Logger.Channel.UserManager);
	}


	// region Subscriber Interaction

	/**
	 * called when new user registers
	 *
	 * @param session          Takes the current Session
	 * @param onSuccess        Generic success Event
	 * @param onFailure        Generic failure Event
	 * @param email            mail address the user uses to register
	 * @param password         password chosen by the user
	 * @param firstName        first name of the user
	 * @param lastName         last name of the user
	 * @param organisationName name of the organization the user belongs to
	 * @param clientAdmin      true if user requests admin status
	 */
	public void registerNewSubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean clientAdmin)
	{
		try
		{
			if(userExists(email))
			{
				onFailure.execute(Messages.EmailAddressAlreadyInUse);
				return;
			}
			if(!organisationExists(organisationName))
			{
				onFailure.execute(Messages.UnknownOrganisation);
				return;
			}
		}
		catch(SQLException e)
		{
			Logger.getInstance().logError(this, "SQL DB Error: " + e.toString());
			onFailure.execute(Messages.UnknownError);
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
		QueryLoginUser qlu = new QueryLoginUser();
		qlu.byPassword = true;
		qlu.password = password;
		if(!qlu.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		if(qlu.userExists)
		{
			onFailure.execute("Das Passwort wird bereits von diesem Nutzer verwendet: " + qlu.subscriber.getEmail());
			return;
		}

		Subscriber subscriber = new Subscriber(email, password, firstName, lastName, new Organisation(organisationName),
		                                       clientAdmin); //TODO: Change Organisation

		QueryAddUser qau = new QueryAddUser();
		qau.subscriber = subscriber;
		if(!qau.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		session.login(subscriber);
		onSuccess.execute();

		Logger.getInstance().logActivity(this, "New Subscriber registered:" + System.lineSeparator()
				+ "[Name]: " + firstName + " , " + lastName + System.lineSeparator()
				+ "[E-Mail]: " + email + System.lineSeparator()
				+ "[Password]: " + password + System.lineSeparator()
				+ "[Organisation]: " + organisationName + System.lineSeparator());
	}

	/**
	 * returns whether an organization exists or not
	 *
	 * @param organisationName name of the organization that is to be checked
	 * @return true if the given organization is existing
	 * @throws SQLException must be catched where called
	 */
	private boolean organisationExists(String organisationName) throws SQLException
	{
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		qgo.organisationName = organisationName;
		if(!qgo.execute())
		{
			// TODO: WHAT THE FUCKING FUCK?!
			throw new SQLException();
		}
		return qgo.organisationExists;
	}

	/**
	 * returns whether a user exists or not
	 *
	 * @param email email of the user that is to be checked
	 * @return true if the given user is existing
	 * @throws SQLException must be catched where called
	 */
	private boolean userExists(String email) throws SQLException
	{
		QueryGetUser qgu = new QueryGetUser();
		qgu.email = email;
		if(!qgu.execute())
		{
			// TODO: WHAT THE FUCKING FUCK?!
			throw new SQLException();
		}
		return qgu.userExists;
	}

	/**
	 * called when User is logging in
	 *
	 * @param session takes the current Session
	 * @param onSuccess Generic success Event
	 * @param onVerificationPending Generic verification pending Event
	 * @param onFailure Generic failure Event
	 * @param email mail address provided by user
	 * @param password password provided by user
	 */
	public void subscriberLogin(Session session, IUserInterface.IGenericEvent onSuccess, IGenericEvent onVerificationPending, IGenericFailureEvent onFailure, String email, String password)
	{
		QueryLoginUser qlu = new QueryLoginUser();
		qlu.password = password;
		qlu.email = email;
		if(!qlu.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		if(!qlu.userLoginSuccessful)
		{
			if(!qlu.userExists)
			{
				onFailure.execute(Messages.InvalidEmailAddress);
				return;
			}
			if(!qlu.userVerified)
			{
				onVerificationPending.execute();
				return;
			}
			if(qlu.isDeactivated)
			{
				onFailure.execute("Konto wurde deaktiviert");
				return;
			}
			onFailure.execute(Messages.WrongPassword);
			return;
		}

		session.login(qlu.subscriber);
		onSuccess.execute();

		Logger.getInstance().logActivity(this, "New Login from Subscriber:" + System.lineSeparator()
				+ "[E-Mail]: " + email + System.lineSeparator()
				+ "[Password]: " + password + System.lineSeparator());
	}

	// endregion Subscriber Interaction

	/**
	 * returns whether the current user is an Administrator or not
	 *
	 * @param session takes current Session
	 * @param onFailure Generic failure Event
	 * @return true if User from current Session is an Administrator, false if not
	 */
	private boolean adminCheck(Session session, IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return false;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotClientAdmin);
			return false;
		}
		if(!((Subscriber)session.getUser()).isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return false;
		}
		return true;
	}

	// region Client Admin Interaction

	/**
	 * gets the current verification requests
	 *
	 * @param session takes current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 */
	public void getPendingVerificationRequests(Session session, IUserInterface.IFetchPendingVerificationRequestsEvent onSuccess, IGenericFailureEvent onFailure)
	{
		if(!adminCheck(session, onFailure)) //Error handling in adminCheck
		{
			return;
		}

		QueryGetVerificationPending qgvp = new QueryGetVerificationPending();
		qgvp.organization = ((Subscriber)session.getUser()).getOrganisationId().getName();
		if(!qgvp.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		UserDataRepresentation[] users = qgvp.users;
		onSuccess.execute(users);
	}

	/**
	 * called when Subscriber is being verified
	 *
	 * @param session         takes the current Session
	 * @param onSuccess       Generic success Event
	 * @param onFailure       Generic failure Event
	 * @param subscriberEmail mail address of user to be verified
	 * @param grantAdmin      true if the user should be provided Administrator status
	 */
	public void verifySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String subscriberEmail, boolean grantAdmin)
	{
		if(!adminCheck(session, onFailure)) //Error handling done in adminCheck method
		{
			return;
		}

		QueryGetUser qgu = new QueryGetUser();
		qgu.email = subscriberEmail;
		if(!qgu.execute())
		{
			onFailure.execute(Messages.UnknownEmail);
			return;
		}
		if(!qgu.userExists)
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		Subscriber subscriber = qgu.subscriber;

		String subscriberOrgName = subscriber.getOrganisationId().getName();

		if(!subscriberOrgName.equals(((Subscriber)session.getUser()).getOrganisationId().getName()))
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		if(subscriber.isVerificationPending())
		{
			QueryVerifyUser qvu = new QueryVerifyUser();
			qvu.email = subscriberEmail;
			qvu.status = 1;
			if(!qvu.execute())
			{
				onFailure.execute(Messages.UnknownError);
				return;
			}
			if(!qvu.userExists)
			{
				onFailure.execute(Messages.UnknownEmail);
				return;
			}

			subscriber.setVerificationPending(false);
			subscriber.setOrganisationAdministrator(grantAdmin);
			onSuccess.execute();
			return;
		}
		onFailure.execute(Messages.UserIsAlreadyVerified);
	}

	// endregion Client Admin Interaction


	// region System Admin Interaction

	/**
	 * called when System Administrator logs in
	 *
	 * @param session takes the current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param email mail address provided by User
	 * @param password password provided by User
	 */
	public void systemAdministratorLogin(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, String password)
	{
		QueryLoginSysadmin qls = new QueryLoginSysadmin();
		qls.email = email;
		qls.password = password;
		if(!qls.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		if(!qls.adminLoginSuccessful)
		{
			if(!qls.userExists)
			{
				onFailure.execute(Messages.UnknownEmail);
				return;
			}
			onFailure.execute(Messages.WrongPassword);
			return;
		}

		SystemAdministrator admin = qls.sysadmin;

		session.login(admin);
		onSuccess.execute();

		Logger.getInstance().logActivity(this, "New Login from System Administrator:" + System.lineSeparator()
				+ "[E-Mail]: " + email + System.lineSeparator()
				+ "[Password]: " + password + System.lineSeparator());
	}

	/**
	 * called when new Organisation is added
	 *
	 * @param session takes the current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param organisationName name of Organisation to be added
	 * @param adminFirstName first name of initial Organisation Administrator
	 * @param adminLastName last name of initial Organisation Administrator
	 * @param adminEmail mail address of initial Organisation Administrator
	 * @param adminPassword password of initial Organisation Administrator
	 */
	public void addOrganisation(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String organisationName, String adminFirstName, String adminLastName, String adminEmail, String adminPassword)
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
		Organisation         organisation = new Organisation(organisationName);
		QueryAddOrganisation qao          = new QueryAddOrganisation();
		qao.orgName = organisationName;
		if(!qao.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		Subscriber subscriber = new Subscriber(adminEmail, adminPassword, adminFirstName, adminLastName, organisation,
		                                       true);

		QueryAddUser qau = new QueryAddUser();
		qau.subscriber = subscriber;
		if(!qau.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();

		Logger.getInstance().logActivity(this, "New Organisation added:" + System.lineSeparator()
				+ "[Name]: " + adminFirstName + " , " + adminLastName + System.lineSeparator()
				+ "[E-Mail]: " + adminEmail + System.lineSeparator()
				+ "[Password]: " + adminPassword + System.lineSeparator()
				+ "[Organisation]: " + organisationName + System.lineSeparator());
	}

	/**
	 * calls removeSubscriber
	 *
	 * @param session takes current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param email email of User to be removed/denied
	 * @param date not used
	 */
	public void denySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, Date date)
	{
		removeSubscriber(session, onSuccess, onFailure, email);
	}

	/**
	 * called when User deactivates their Account
	 *
	 * @param session takes current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param email email of User to be deactivated
	 * @param date current date
	 */
	public void deactivateSubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, Date date)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}
		QueryRemoveSubscriber qrs = new QueryRemoveSubscriber();
		qrs.email = email;
		qrs.isFinal = false;
		qrs.date = date;
		if(!qrs.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}

	/**
	 * removes Subscriber
	 *
	 * @param session takes current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param email email of User to be removed
	 */
	public void removeSubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}
		QueryRemoveSubscriber qrs = new QueryRemoveSubscriber();
		qrs.email = email;
		qrs.isFinal = true;
		if(!qrs.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}

	/**
	 * gets all Organisations
	 *
	 * @param session takes current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @return String array of Organisation, null if failed
	 */
	public String[] getAllOrganisations(Session session, IGetStringArrayEvent onSuccess, IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return null;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return null;
		}
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		if(!qgo.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return null;
		}
		onSuccess.execute(qgo.organisations);
		return qgo.organisations;
	}

	/**
	 * removes an Organisation
	 *
	 * @param session takes the current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param organisationName Name of the Organisation to be removed
	 */
	public void removeOrganisation(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String organisationName)
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

		QueryDeleteOrganization qdo = new QueryDeleteOrganization();
		qdo.orgName = organisationName;
		if(!qdo.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}

	/**
	 * called on password change
	 *
	 * @param session takes the current Session
	 * @param onSuccess Generic success Event
	 * @param onFailure Generic failure Event
	 * @param email mail of user
	 * @param oldPW old password
	 * @param newPW new password
	 */
	public void changePassword(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, String oldPW, String newPW)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.NotSystemAdmin);
			return;
		}

		QueryLoginUser qlu = new QueryLoginUser();
		qlu.password = oldPW;
		qlu.email = email;
		if(!qlu.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		if(!qlu.userLoginSuccessful)
		{
			if(!qlu.userExists)
			{
				onFailure.execute(Messages.InvalidEmailAddress);
				return;
			}
			if(!qlu.userVerified)
			{
				onFailure.execute(Messages.UnknownError);
				return;
			}
			if(qlu.isDeactivated)
			{
				onFailure.execute("Konto wurde deaktiviert");
				return;
			}
			onFailure.execute(Messages.WrongPassword);
			return;
		}

		QueryChangePassword qcp = new QueryChangePassword();
		qcp.email = email;
		qcp.password = newPW;
		if(!qcp.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
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
				if(m_digitIsRequired)
				{
					regex.append("(?=.*[0-9])");
				}
				if(m_lowerCaseLetterIsRequired)
				{
					regex.append("(?=.*[a-z])");
				}
				if(m_upperCaseLetterIsRequired)
				{
					regex.append("(?=.*[A-Z])");
				}
				if(m_specialCharacterIsRequired)
				{
					regex.append("(?=.*[@#$%^&+=])");
				}
				if(m_whitespaceIsForbidden)
				{
					regex.append("(?=\\S+$)");
				}
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



	private static UserManager m_instance;
}
