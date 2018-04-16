package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.Entities.Subscriber;



/**
 * Add subscriber to database
 */
public class AddSubscriberQuery implements IQuery<Void>
{
	/**
	 * @param subscriber Subscriber to add
	 */
	public AddSubscriberQuery(Subscriber subscriber)
	{
		m_subscriber = subscriber;
	}

	@SuppressWarnings("unused")
	private AddSubscriberQuery()
	{}

	/**
	 * @return returns false in case of an error
	 *         returns true otherwise
	 */
	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			RelationalDbConnector.getInstance().addEntity(m_subscriber);
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
