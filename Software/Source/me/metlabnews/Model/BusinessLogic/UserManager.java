package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.*;
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




public class UserManager
{


	public UserManager()
	{
	}


	// region Subscriber Interaction
	public void registerNewSubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, String password,
	                                  String firstName, String lastName,
	                                  String organisationName, boolean clientAdmin)
	{
		if(userExists(email))
		{
			onFailure.execute(Messages.EmailAddressAlreadyInUse);
			return; //TODO: Implement Error User exists
		}
		if(!organisationExists(organisationName))
		{
			onFailure.execute(Messages.UnknownOrganisation);
			return; //TODO: Implement Error Organization does not exist
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

		QueryAddUser qau = new QueryAddUser();
		qau.email = email;
		qau.password = password;
		qau.firstName = firstName;
		qau.lastName = lastName;
		qau.organisationName = organisationName;
		qau.clientAdmin = clientAdmin;
		if(!qau.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return; //TODO: Error
		}
		Subscriber subscriber = new Subscriber(email, password, firstName, lastName, new Organisation(organisationName),
		                                       clientAdmin); //TODO: Change Organisation to String
		session.login(subscriber);
		onSuccess.execute();
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
		if(!qgu.execute())
		{
			return false; //TODO: Error handling
		}
		return qgu.userExists;
	}

	public void subscriberLogin(Session session, IUserInterface.IGenericEvent onSuccess, IGenericEvent onVerificationPending, IGenericFailureEvent onFailure, String email, String password)
	{
		QueryLoginUser qlu = new QueryLoginUser();
		qlu.password = password;
		qlu.email = email;
		if(!qlu.execute())
		{
			return; //TODO: Error handling
		}
		if(!qlu.userLoginSuccessful)
		{
			if(!qlu.userExists)
			{
				onFailure.execute(Messages.InvalidEmailAddress);
				return; //TODO: Error handling User doesn't exist
			}
			if(!qlu.userVerified)
			{
				onVerificationPending.execute();
				return;
			}
			onFailure.execute(Messages.WrongPassword);
			return; //TODO: Error handling invalid password
		}

		session.login(qlu.subscriber);
		onSuccess.execute();
		//TODO: User Login
	}

	// endregion Subscriber Interaction

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

	public void getPendingVerificationRequests(Session session, IUserInterface.IFetchPendingVerificationRequestsEvent onSuccess, IGenericFailureEvent onFailure)
	{
		if(!adminCheck(session, onFailure))
		{
			return;
		}

		QueryGetVerificationpending qgvp = new QueryGetVerificationpending();
		if(!qgvp.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return; //TODO: Error handling
		}
		UserDataRepresentation[] users = qgvp.users; //TODO: Change users to needed type
		onSuccess.execute(users);
		//TODO: Mails treatment
	}

	public void verifySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String subscriberEmail, Boolean grantAdmin)
	{
		if(!adminCheck(session, onFailure))
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
				return; //TODO: Error handling
			}
			if(!qvu.userExists)
			{
				onFailure.execute(Messages.UnknownEmail);
				return;
			}

			subscriber.setVerificationPending(false);
			subscriber.setOrganisationAdministrator(grantAdmin);
			onSuccess.execute();
		}
		onFailure.execute(Messages.UserIsAlreadyVerified);

	}

	// endregion Client Admin Interaction


	// region System Admin Interaction

	public void systemAdministratorLogin(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email, String password)
	{
		QueryLoginSysadmin qls = new QueryLoginSysadmin();
		qls.email = email;
		qls.password = password;
		if(!qls.execute())
		{
			return; //TODO: Error handling
		}
		if(!qls.adminLoginSuccessful)
		{
			if(!qls.userExists)
			{
				onFailure.execute(Messages.UnknownEmail);
				return; //TODO: Error handling User doesn't exist
			}
			onFailure.execute(Messages.WrongPassword);
			return; //TODO: Error handling invalid password
		}

		SystemAdministrator admin = qls.sysadmin;

		session.login(admin);
		onSuccess.execute();
		//TODO: Admin Login
	}

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
		}
		if(!Validator.validatePassword(adminPassword))
		{
			onFailure.execute(Messages.PasswordDoesNotMatchRequirements);
			return;
		}
		Organisation         organisation = new Organisation(organisationName);
		QueryAddOrganisation qao = new QueryAddOrganisation();
		qao.orgName = organisationName;
		if(!qao.execute())
		{
			return; //TODO: Error handling
		}

		QueryAddUser qau = new QueryAddUser();
		qau.organisationName = organisationName;
		qau.password = adminPassword;
		qau.firstName = adminFirstName;
		qau.lastName = adminLastName;
		qau.clientAdmin = true;
		qau.email = adminEmail;
		if(!qau.execute())
		{
			return;
		}

		onSuccess.execute();
	}

	public void denySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email)
	{
		removeSubscriber(session, onSuccess, onFailure, email);
	}

	public void removeSubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email)
	{
		//TODO: Implement
	}

	public void getAllOrganisations(Session session, IGetStringArrayEvent onSuccess, IGenericFailureEvent onFailure)
	{
		//TODO: Implement
	}

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
			return; //TODO: Error handling
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

}
