package me.metlabnews.Presentation;



class Session
{
	public Session(IUserInterface ui)
	{
		m_uiInstance = ui;
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



	private SessionState m_currentState = SessionState.NotYetLoggedIn;
	private IUserInterface m_uiInstance = null;
}
