package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.*;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.ResourceManagement.IResource;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericFailureEvent;
import me.metlabnews.Presentation.IUserInterface.IGetStringArrayEvent;
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
		if(checkPasswordRequirements(password) != Passwordrequirements.NONE)
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
			return; //TODO: Error
		}
	}

	private Passwordrequirements checkPasswordRequirements(String password)
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
		if(!qgu.execute())
		{
			return false; //TODO: Error handling
		}
		return qgu.userExists;
	}

	public void subscriberLogin(Session session, IUserInterface.IGenericEvent onSuccess, String email, String password)
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
				System.out.println("User nonexistent");
				return; //TODO: Error handling User doesn't exist
			}
			System.out.println("Wrong password");
			return; //TODO: Error handling invalid password
		}
		System.out.println("Login successful");
		Subscriber subscriber = new Subscriber();
		session.login(subscriber);
		onSuccess.execute();
		//TODO: User Login
	}

	// endregion Subscriber Interaction


	// region Client Admin Interaction

	public void getPendingVerificationRequests(Session session)
	{
		QueryGetVerificationpending qgvp = new QueryGetVerificationpending();
		if(!qgvp.execute())
		{
			return; //TODO: Error handling
		}
		String[] mails = qgvp.mails;
		//TODO: Mails treatment
	}

	public void verifySubscriber(Session session, String subscriberEmail, Boolean grantAdmin)
	{
		QueryVerifyUser qvu = new QueryVerifyUser();
		qvu.email = subscriberEmail;
		qvu.status = 1;
		if(!qvu.execute())
		{
			return; //TODO: Error handling
		}
	}


	// TODO: merge with verifySubscriber()
	public void denySubscriberVerification(Session session, String subscriberEmail)
	{
		//TODO: Figure out what to do here
	}

	// endregion Client Admin Interaction


	// region System Admin Interaction

	public void systemAdministratorLogin(Session session, String email, String password)
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
				return; //TODO: Error handling User doesn't exist
			}
			return; //TODO: Error handling invalid password
		}
		//TODO: Admin Login
	}

	public void addOrganisation(Session session, String organisationName, String adminFirstName, String adminLastName, String adminEmail, String adminPassword)
	{
		QueryAddOrganisation qao = new QueryAddOrganisation();
		qao.orgName = organisationName;
		if(!qao.execute())
		{
			return; //TODO: Error handling
		}
	}

	public void deleteOrganisation(Session session, String organisationName)
	{
		QueryDeleteOrganization qdo = new QueryDeleteOrganization();
		qdo.orgName = organisationName;
		if(!qdo.execute())
		{
			return; //TODO: Error handling
		}
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
