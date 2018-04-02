package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.UserManager;



public class Presenter
{
	public static Presenter getInstance() throws IllegalStateException
	{
		if(m_instance == null)
		{
			throw new IllegalStateException(
					"Presenter has not been initialized");
		}
		return m_instance;
	}

	static Presenter create(UserManager userManager)
	{
		if(m_instance == null)
		{
			m_instance = new Presenter(userManager);
		}
		return m_instance;
	}


	private Presenter(UserManager userManager)
	{
		m_userManager = userManager;
		System.out.println("[MESSAGE] new Presenter created: " + this.toString());
	}


	public void connect(IUserInterface ui)
	{
		ui.registerUserLoginCallback(m_userManager::subscriberLogin);
	}



	private static Presenter m_instance = null;
	private UserManager m_userManager;
}
