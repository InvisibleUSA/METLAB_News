package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.Entities.Subscriber;



public class UpdateSubscriberQuery implements IQuery<Void>
{
	public UpdateSubscriberQuery(Subscriber subscriber)
	{
		m_subscriber = subscriber;
	}

	private UpdateSubscriberQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			RelationalDbConnector.getInstance().updateEntity(m_subscriber);
		}
		catch(DataUpdateFailedException e)
		{
			e.printStackTrace();
		}
		return success;
	}


	@Override
	public Void getResult()
	{
		return null;
	}



	protected Subscriber m_subscriber;
}
