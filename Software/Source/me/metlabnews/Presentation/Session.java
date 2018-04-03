package me.metlabnews.Presentation;



public class Session implements IEventHandler
{
	Session(IUserInterface uiInstance)
	{
		m_uiInstance = uiInstance;
	}

	SessionState getState()
	{
		return m_currentState;
	}

	void setState(SessionState state)
	{
		m_currentState = state;
	}

	IUserInterface getUiInstance()
	{
		return m_uiInstance;
	}


	@Override
	public void userLoginSuccessfulEvent()
	{
		setState(SessionState.LoggedIn);
		m_uiInstance.userLoginSuccessfulEvent();
	}

	@Override
	public void userLoginFailedEvent(String errorMessage)
	{
		setState(SessionState.NotYetLoggedIn);
		m_uiInstance.userLoginFailedEvent(errorMessage);
	}

	@Override
	public void userRegistrationSuccessfulEvent()
	{
		setState(SessionState.RegistrationPending);
		m_uiInstance.userRegistrationSuccessfulEvent();
	}

	@Override
	public void userRegistrationFailedEvent(String errorMessage)
	{
		setState(SessionState.RegistrationFailed);
		m_uiInstance.userLoginFailedEvent(errorMessage);
	}

	@Override
	public void userVerificationSuccessfulEvent()
	{
		m_uiInstance.userVerificationSuccessfulEvent();
	}

	@Override
	public void userVerificationDeniedEvent()
	{
		m_uiInstance.userVerificationDeniedEvent();
	}

	@Override
	public void adminLoginSuccessfulEvent()
	{
		m_uiInstance.adminLoginSuccessfulEvent();
	}

	@Override
	public void adminLoginFailedEvent(String errorMessage)
	{
		m_uiInstance.adminLoginFailedEvent(errorMessage);
	}

	@Override
	public void sysAdminLoginSuccessfulEvent()
	{
		m_uiInstance.sysAdminLoginSuccessfulEvent();
	}

	@Override
	public void sysAdminLoginFailedEvent(String errorMessage)
	{
		m_uiInstance.adminLoginFailedEvent(errorMessage);
	}


	enum SessionState
	{
		NotYetLoggedIn,
		LoginPending,
		LoggedIn,
		LoginDenied,
		LoggedOut,
		RegistrationPending,
		RegistrationFailed,
		VerificationPending,
		VerificationDenied,
		Registered
	}



	private SessionState   m_currentState = SessionState.NotYetLoggedIn;
	private IUserInterface m_uiInstance = null;
}
