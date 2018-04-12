package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.ResourceManagement.IInitializable;



public class BaseXConnector implements IInitializable, AutoCloseable
{
	public static BaseXConnector getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new BaseXConnector();
		}
		return m_instance;
	}


	private BaseXConnector()
	{

	}



	@Override
	public void close()
	{
	}

	@Override
	public void initialize()
	{
	}



	private static BaseXConnector m_instance;
}
