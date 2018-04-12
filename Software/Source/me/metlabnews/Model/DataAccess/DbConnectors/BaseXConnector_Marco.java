package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.ResourceManagement.IResource;



public class BaseXConnector_Marco implements IResource
{
	public static BaseXConnector_Marco getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new BaseXConnector_Marco();
		}
		return m_instance;
	}


	private BaseXConnector_Marco()
	{ }


	@Override
	public void initialize()
	{
	}


	@Override
	public void close()
	{
	}



	private static BaseXConnector_Marco m_instance;
}
