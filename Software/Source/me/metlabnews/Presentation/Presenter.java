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

	public static Presenter create()
	{
		if(m_instance == null)
		{
			m_instance = new Presenter();
		}
		return m_instance;
	}

	private Presenter()
	{
		System.out.println("[MESSAGE] new Presenter created: " + this.toString());
	}


	public void connect(IUserInterface ui)
	{
		ui.registerUserLoginCallback(UserManager.getInstance()::userLogin);
	}



	private static Presenter m_instance = null;
}
