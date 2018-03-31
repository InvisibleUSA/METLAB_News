package me.metlabnews.Presentation;

import java.util.Vector;



class SessionHandler
{
	public void openNewSession()
	{

	}


	private final int       m_initialSessionCapacity = 20;
	private final int       m_sessionCapacityIncrement = 5;
	private Vector<Session> m_sessions  = new Vector<>(m_initialSessionCapacity,
	                                                   m_sessionCapacityIncrement);
}
