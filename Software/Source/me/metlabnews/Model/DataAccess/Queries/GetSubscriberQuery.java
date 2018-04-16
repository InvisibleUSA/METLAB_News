package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Subscriber;



/**
 * Get exactly one subscriber with matching email address
 */
public class GetSubscriberQuery implements IQuery<Subscriber>
{
	/**
	 * @param email email address to match
	 */
	public GetSubscriberQuery(String email)
	{
		m_email = email;
	}

	@SuppressWarnings("unused")
	private GetSubscriberQuery()
	{}


	/**
	 * @return returns false if
	 *         - no subscriber with matching email address was found
	 *         - more than one subscriber with matching email address was found
	 *         returns true otherwise
	 */
	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		try
		{
			// TODO: replace property string with reflection
			m_result = (Subscriber)RelationalDbConnector.getInstance().getUniqueEntity(
					Subscriber.class.getSimpleName(), "email", m_email);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			success = false;
		}
		catch(UnexpectedNonUniqueDataException e)
		{
			success = false;
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR,
			                         "in GetSubscriberQuery.execute(): " +
					                         "subscriber with non-unique email: " + m_email);
		}
		return success;
	}


	@Override
	public Subscriber getResult()
	{
		return m_result;
	}



	private String     m_email;
	private Subscriber m_result;
}
