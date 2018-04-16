package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.Entities.Subscriber;



/**
 * Update subscriber in database
 * Can be used to change a subscriber's password or other properties
 * @see Subscriber
 * DO NOT CHANGE SUBSCRIBER ID!
 */
public class UpdateSubscriberQuery implements IQuery<Void>
{
	/**
	 * @param subscriber Subscriber to update
	 */
	public UpdateSubscriberQuery(Subscriber subscriber)
	{
		m_subscriber = subscriber;
	}

	@SuppressWarnings("unused")
	private UpdateSubscriberQuery()
	{}



	/**
	 * @return returns false in case of an error; returns true otherwise
	 */
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
			success = false;
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR,
			                         "in UpdateSubscriberQuery.execute(): " + e.toString());
		}
		return success;
	}


	@Override
	public Void getResult()
	{
		return null;
	}



	private Subscriber m_subscriber;
}
