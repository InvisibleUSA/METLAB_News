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

import java.sql.SQLException;



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
	}

	private boolean organisationExists(String organisationName) throws SQLException
	{
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		qgo.organisationName = organisationName;
		if(!qgo.execute())
		{
			throw new SQLException();
		}
		return qgo.organisationExists;
	}

	private boolean userExists(String email) throws SQLException
	{
		QueryGetUser qgu = new QueryGetUser();
		qgu.email = email;
		if(!qgu.execute())
		{
			throw new SQLException();
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
			onFailure.execute(Messages.WrongPassword);
			return;
		}

		session.login(qlu.subscriber);
		onSuccess.execute();
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

	public void verifySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String subscriberEmail, Boolean grantAdmin)
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
	}

	public void denySubscriber(Session session, IGenericEvent onSuccess, IGenericFailureEvent onFailure, String email)
	{
		removeSubscriber(session, onSuccess, onFailure, email);
	}

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
		if(!qrs.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}

	public void getAllOrganisations(Session session, IGetStringArrayEvent onSuccess, IGenericFailureEvent onFailure)
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
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		if(!qgo.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute(qgo.organisations);
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

}
