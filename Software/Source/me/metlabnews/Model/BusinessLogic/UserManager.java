package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;



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
		                                       organisation, false);
		m_dbConnector.addSubscriber(subscriber);
		session.subscriberVerificationPendingEvent();
	}


	public void subscriberLogin(Session session, String email, String password)
	{
		String correctPassword;
		try
		{
			correctPassword = m_dbConnector.getSubscriberByEmail(email).getPassword();
		}
		catch(RequestedDataDoesNotExistException e)
		{
			session.subscriberLoginFailedEvent(Messages.UnknownEmail);
			return;
		}
		if(password.equals(correctPassword))
		{
			session.subscriberLoginSuccessfulEvent();
			return;
		}
		else
		{
			session.subscriberLoginFailedEvent(Messages.WrongPassword);
		}
	}



	private static       UserManager m_instance  = null;
	private RelationalDbConnector m_dbConnector;
}
