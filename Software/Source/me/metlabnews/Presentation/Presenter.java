package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.*;
import java.util.concurrent.ConcurrentHashMap;



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
		m_sessions = new ConcurrentHashMap<>();
	}


	public void connect(IUserInterface ui)
	{
		Session session = new Session(ui);
	// region Callbacks
		ui.registerCallbackSubscriberLogin((email, pw) ->
			m_userManager.subscriberLogin(session, email, pw));

		ui.registerCallbackSubscriberRegistration((fName, lName, org, email, pw, admin) ->
			m_userManager.registerNewSubscriber(session, email, pw,
			                                    fName, lName, org, admin));

		ui.registerCallbackLogout(session::userLogoutEvent);

		ui.registerCallbackSysAdminLogin((email, pw) ->
				m_userManager.systemAdministratorLogin(session, email, pw));

	// endregion Callbacks

		m_sessions.put(ui, session);
	}


	public void disconnect(IUserInterface ui)
	{
		m_sessions.get(ui).close();
		m_sessions.remove(ui);
	}



	private static Presenter m_instance = null;
	private UserManager m_userManager;
	private ConcurrentHashMap<IUserInterface, Session> m_sessions;
}
