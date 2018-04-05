package me.metlabnews.Presentation;

import me.metlabnews.Model.Entities.User;



public class Session implements ISubscriberEventHandler,
                                IClientAdminEventHandler,
                                ISysAdminEventHandler,
                                ICommonEventHandler
{
	Session(IUserInterface uiInstance)
	{
		m_uiInstance = uiInstance;
	}

	synchronized void close()
	{
		m_uiInstance = null;
	}

	private synchronized void onLogin()
	{
		m_loggedIn = true;
	}

	private synchronized void onLogout()
	{
		m_loggedIn = false;
	}

	public synchronized boolean isLoggedIn()
	{
		return m_loggedIn;
	}


	public synchronized User getUser()
	{
		return m_user;
	}

	public synchronized void setUser(User m_user)
	{
		this.m_user = m_user;
	}


	@Override
	public void subscriberLoginSuccessfulEvent(boolean asAdmin)
	{
		if(m_uiInstance != null)
		{
			onLogin();
			m_uiInstance.subscriberLoginSuccessfulEvent(asAdmin);
		}
	}

	@Override
	public void subscriberLoginFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.subscriberLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void userLogoutEvent()
	{
		if(m_uiInstance != null)
		{
			onLogout();
			m_uiInstance.userLogoutEvent();
		}
	}

	@Override
	public void subscriberVerificationPendingEvent()
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.subscriberVerificationPendingEvent();
		}
	}

	@Override
	public void subscriberRegistrationFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.subscriberLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void subscriberVerificationFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.subscriberVerificationFailedEvent(errorMessage);
		}
	}

	@Override
	public void subscriberVerificationDenialSuccessfulEvent()
	{

	}

	@Override
	public void getPendingVerificationRequestsSuccessfulEvent(SubscriberDataRepresentation[] data)
	{

	}

	@Override
	public void getPendingVerificationRequestsFailedEvent(String errorMessage)
	{

	}

	@Override
	public void subscriberVerificationSuccessfulEvent()
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.subscriberVerificationSuccessfulEvent();
		}
	}

	@Override
	public void sysAdminLoginSuccessfulEvent()
	{
		if(m_uiInstance != null)
		{
			onLogin();
			m_uiInstance.sysAdminLoginSuccessfulEvent();
		}
	}

	@Override
	public void sysAdminLoginFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.sysAdminLoginFailedEvent(errorMessage);
		}
	}

	@Override
	public void addingOrganisationSuccessfulEvent()
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.addingOrganisationSuccessfulEvent();
		}
	}

	@Override
	public void addingOrganisationFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.addingOrganisationFailedEvent(errorMessage);
		}
	}

	@Override
	public void deletingOrganisationSuccessfulEvent()
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.deletingOrganisationSuccessfulEvent();
		}
	}

	@Override
	public void deletingOrganisationFailedEvent(String errorMessage)
	{
		if(m_uiInstance != null)
		{
			m_uiInstance.deletingOrganisationFailedEvent(errorMessage);
		}
	}



	private IUserInterface  m_uiInstance;
	private User m_user;
	private boolean m_loggedIn;
}
