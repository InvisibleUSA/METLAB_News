package me.metlabnews.Presentation;



class Session
{
	public Session(IUserInterface ui)
	{
		m_uiInstance = ui;
	}

	enum SessionStates
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



	private IUserInterface m_uiInstance = null;
}
