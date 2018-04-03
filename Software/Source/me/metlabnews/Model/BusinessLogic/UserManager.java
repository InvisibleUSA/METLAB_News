package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Presentation.Session;
import org.basex.query.value.item.Str;



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


	public void registerNewSubscriber(Session session, String email, String password,
	                                  String firstName, String lastName, String organisationName)
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
		if(emailIsAlreadyTaken)
		{
			session.userRegistrationFailedEvent("Die angegebene E-Mail-Adresse " +
					                             "wird bereits von einem anderen Nutzer verwendet!");
			return;
		}

		Organisation organisation;
		try
		{
			organisation = m_dbConnector.getOrganisationByName(organisationName);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.userRegistrationFailedEvent("Die angegebene Organisation existiert nicht!");
			return;
		}
		Subscriber subscriber = new Subscriber(email, password, firstName, lastName,
		                                       organisation, false);
		m_dbConnector.addSubscriber(subscriber);
		session.userRegistrationSuccessfulEvent();
	}


	public void subscriberLogin(Session session, String email, String password)
	{
		System.out.println("[MESSAGE] Attempted Login with email: " + email + "; pw: " + password);
		String correctPassword;
		try
		{
			correctPassword = m_dbConnector.getSubscriberByEmail(email).getPassword();
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.userLoginFailedEvent("Kein Benutzer mit dieser E-Mail vorhanden!");
			return;
		}
		if(password.equals(correctPassword))
		{
			session.userLoginSuccessfulEvent();
			return;
		}
		else
		{
			session.userLoginFailedEvent("Falsches Passwort!");
		}
	}



	private static UserManager m_instance = null;
	private RelationalDbConnector m_dbConnector;
}
