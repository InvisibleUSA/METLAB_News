package me.metlabnews.Presentation;

import me.metlabnews.Model.Entities.User;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;



public class Session
{
	Session()
	{
	}

	synchronized void close()
	{
		m_user = null;
	}

	public synchronized void login(User user)
	{
		m_user = user;
	}

	public synchronized void logout(IGenericEvent onExecute)
	{
		onExecute.execute();
		m_user = null;
	}

	public synchronized boolean isLoggedIn()
	{
		return m_user != null;
	}

	public synchronized User getUser()
	{
		return m_user;
	}



	private User m_user;
}
