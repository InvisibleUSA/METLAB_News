package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.ResourceManagement.IResource;



public class BaseXConnector implements IResource
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
	{ }


	@Override
	public void initialize()
	{
	}


	@Override
	public void close()
	{
	}



	private static BaseXConnector m_instance;
}
