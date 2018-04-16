package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.SystemAdministrator;



/**
 * Get exactly one system administrator with specified email address
 */
public class GetSystemAdministratorQuery implements IQuery<SystemAdministrator>
{
	public GetSystemAdministratorQuery(String email)
	{
		m_email = email;
	}

	@SuppressWarnings("unused")
	private GetSystemAdministratorQuery()
	{}


	/**
	 * @return returns false if
	 *         - no administrator with matching email address was found
	 *         - more than one administrator with matching email address was found
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
			m_result = (SystemAdministrator)RelationalDbConnector.getInstance().getUniqueEntity(
					SystemAdministrator.class.getSimpleName(), "email", m_email);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			success = false;
		}
		catch(UnexpectedNonUniqueDataException e)
		{
			success = false;
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR,
			                         "in GetSystemAdministratorQuery.execute(): " +
					                         "admin with non-unique email: " + m_email);
		}
		return success;
	}


	@Override
	public SystemAdministrator getResult()
	{
		return m_result;
	}



	private String              m_email;
	private SystemAdministrator m_result;
}
