package me.metlabnews.Presentation;



public class Session implements IEventHandler
{
	Session(IUserInterface uiInstance)
	{
		m_uiInstance = uiInstance;
	}

	synchronized void close()
	{
		setState(SessionState.Dead);
		m_uiInstance = null;
	}

	synchronized SessionState getState()
	{
		return m_currentState;
	}

	synchronized void setState(SessionState state)
	{
		m_currentState = state;
	}

	synchronized IUserInterface getUiInstance()
	{
		return m_uiInstance;
	}


	@Override
	public void subscriberLoginSuccessfulEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoggedInAsSubscriber);
			m_uiInstance.subscriberLoginSuccessfulEvent();
		}
	}

	@Override
	public void subscriberLoginFailedEvent(String errorMessage)
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.NotYetLoggedIn);
			m_uiInstance.subscriberLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void subscriberLogoutEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoggedOut);
			m_uiInstance.subscriberLogoutEvent();
		}
	}

	@Override
	public void subscriberVerificationPendingEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.VerificationPending);
			m_uiInstance.subscriberVerificationPendingEvent();
		}
	}

	@Override
	public void subscriberRegistrationFailedEvent(String errorMessage)
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.RegistrationFailed);
			m_uiInstance.subscriberLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void subscriberVerificationSuccessfulEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.Registered);
			m_uiInstance.subscriberVerificationSuccessfulEvent();
		}
	}

	@Override
	public void subscriberVerificationDeniedEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.VerificationDenied);
			m_uiInstance.subscriberVerificationDeniedEvent();
		}
	}

	@Override
	public void clientAdminLoginSuccessfulEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoggedInAsClientAdmin);
			m_uiInstance.clientAdminLoginSuccessfulEvent();
		}
	}

	@Override
	public void clientAdminLoginFailedEvent(String errorMessage)
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoginDenied);
			m_uiInstance.clientAdminLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void sysAdminLoginSuccessfulEvent()
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoggedInAsSysAdmin);
			m_uiInstance.sysAdminLoginSuccessfulEvent();
		}
	}

	@Override
	public void sysAdminLoginFailedEvent(String errorMessage)
	{
		if(getState() != SessionState.Dead)
		{
			setState(SessionState.LoginDenied);
			m_uiInstance.clientAdminLoginFailedEvent(errorMessage);
		}
	}


	enum SessionState
	{
		NotYetLoggedIn,
		LoggedInAsSubscriber,
		LoggedInAsClientAdmin,
		LoggedInAsSysAdmin,
		LoginDenied,
		LoggedOut,
		RegistrationFailed,
		VerificationPending,
		VerificationDenied,
		Registered,
		Dead
	}



	private SessionState    m_currentState = SessionState.NotYetLoggedIn;
	private IUserInterface  m_uiInstance;
}
