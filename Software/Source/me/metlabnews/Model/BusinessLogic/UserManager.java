package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.*;
import me.metlabnews.Presentation.Session;



public class UserManager
{


	public UserManager()
	{
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

	public void subscriberLogin(Session session, String email, String password)
	{
		QueryLoginUser qlu = new QueryLoginUser();
		qlu.password = password;
		qlu.email = email;
		if(!qlu.execute())
		{
			Logger.getInstance().log(Logger.enum_channel.DataBase, Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile, "SQL Failed at Subscriber Login");
			return; //TODO: Error handling
		}
		if(!qlu.userLoginSuccessful)
		{
			if(!qlu.userExists)
			{
				return; //TODO: Error handling User doesn't exist
			}
			return; //TODO: Error handling invalid password
		}
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

	public void verifySubscriber(Session session, String subscriberEmail)
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

	public void addOrganisation(Session session, String organisationName)
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

}
