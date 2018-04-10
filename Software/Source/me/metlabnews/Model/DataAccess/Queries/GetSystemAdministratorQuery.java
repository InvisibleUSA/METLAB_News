package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.SystemAdministrator;



public class GetSystemAdministratorQuery implements IQuery<SystemAdministrator>
{
	public GetSystemAdministratorQuery(String email)
	{
		m_email = email;
	}

	private GetSystemAdministratorQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		try
		{
			// TODO: replace property string with reflection
			m_result = (SystemAdministrator)MariaDbConnector.getInstance().getUniqueEntity(
					SystemAdministrator.class.getSimpleName(), "email", m_email);
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
	public SystemAdministrator getResult()
	{
		return m_result;
	}



	protected String              m_email;
	protected SystemAdministrator m_result;
}
