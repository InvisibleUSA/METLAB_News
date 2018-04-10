package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Subscriber;



public class GetSubscriberQuery implements IQuery<Subscriber>
{
	public GetSubscriberQuery(String email)
	{
		m_email = email;
	}

	private GetSubscriberQuery()
	{}

	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		try
		{
			// TODO: replace property string with reflection
			m_result = (Subscriber)MariaDbConnector.getInstance().getUniqueEntity(
					Subscriber.class.getSimpleName(), "email", m_email);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			success = false;
		}
		catch(UnexpectedNonUniqueDataException e)
		{
			success = false;
		}
		return success;
	}


	@Override
	public Subscriber getResult()
	{
		return m_result;
	}



	protected String     m_email;
	protected Subscriber m_result;
}
