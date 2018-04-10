package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Organisation;



public class GetOrganisationQuery implements IQuery<Organisation>
{
	public GetOrganisationQuery(String name)
	{
		m_name = name;
	}

	private GetOrganisationQuery()
	{}

	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		try
		{
			// TODO: replace property string with reflection
			m_result = (Organisation)MariaDbConnector.getInstance().getUniqueEntity(
					"name", m_name);
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
	public Organisation getResult()
	{
		return m_result;
	}



	protected String       m_name;
	protected Organisation m_result;
}
