package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Model.DataAccess.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;



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


	public void registerNewSubscriber(String email, String password, String firstName, String lastName,
	                                  String organisationName)
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
			System.out.println("[MESSAGE] Email is already in use!");
			return;
		}

		Organisation organisation;
		try
		{
			organisation = m_dbConnector.getOrganisationByName(organisationName);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			System.out.println("[MESSAGE] No organisation with this name!");
			return;
		}
		Subscriber subscriber = new Subscriber(email, password, firstName, lastName, organisation);
		m_dbConnector.addSubscriber(subscriber);
	}


	public void subscriberLogin(String email, String password)
	{
		System.out.println("[MESSAGE] Attempted Login with email: " + email + " ; pw: " + password);
		String correctPassword;
		try
		{
			correctPassword = m_dbConnector.getSubscriberByEmail(email).getPassword();
		}
		catch(RequestedDataDoesNotExistException e)
		{
			System.out.println("[MESSAGE] No user with this email!");
			return;
		}
		if(password.equals(correctPassword))
		{
			System.out.println("[MESSAGE] Login successful");
		}
		else
		{
			System.out.println("[MESSAGE] Wrong Password!");
		}
	}



	private static UserManager m_instance = null;
	private RelationalDbConnector m_dbConnector;
}
