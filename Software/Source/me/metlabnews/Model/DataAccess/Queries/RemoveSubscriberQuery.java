package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Subscriber;



public class RemoveSubscriberQuery implements IQuery<Void>
{
	public RemoveSubscriberQuery(Subscriber subscriber)
	{
		m_subscriber = subscriber;
	}

	private RemoveSubscriberQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			RelationalDbConnector.getInstance().deleteEntity(m_subscriber);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			success = false;
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
