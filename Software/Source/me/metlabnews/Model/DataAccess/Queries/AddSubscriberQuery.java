package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.Entities.Subscriber;



public class AddSubscriberQuery implements IQuery<Void>
{
	public AddSubscriberQuery(Subscriber subscriber)
	{
		m_subscriber = subscriber;
	}

	private AddSubscriberQuery()
	{}

	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			MariaDbConnector.getInstance().addEntity(m_subscriber);
		}
		catch(DataCouldNotBeAddedException e)
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
