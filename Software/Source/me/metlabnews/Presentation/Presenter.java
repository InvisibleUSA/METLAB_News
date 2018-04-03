package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.*;
import java.util.Vector;



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
		Session session = new Session(ui);

		ui.registerUserLoginCallback((email, pw) ->
				                             m_userManager.subscriberLogin(session, email, pw));
		ui.registerUserRegisterCallback((email, pw, fName, lName, org) ->
				                             m_userManager.registerNewSubscriber(session, email, pw,
				                                                                 fName, lName, org));

		m_sessions.add(session);
	}



	private static Presenter m_instance = null;
	private UserManager m_userManager;
	private final int       m_initialSessionCapacity = 20;
	private final int       m_sessionCapacityIncrement = 5;
	private Vector<Session> m_sessions  = new Vector<>(m_initialSessionCapacity,
	                                                   m_sessionCapacityIncrement);
}
