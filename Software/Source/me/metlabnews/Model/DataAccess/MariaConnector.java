package me.metlabnews.Model.DataAccess;



public class MariaConnector
{
	public static MariaConnector getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new MariaConnector();
		}
		return m_instance;
	}


	private MariaConnector()
	{
	}


	private void connect()
	{
	}

	private void disconnect()
	{
	}


	private static MariaConnector m_instance = null;
}
