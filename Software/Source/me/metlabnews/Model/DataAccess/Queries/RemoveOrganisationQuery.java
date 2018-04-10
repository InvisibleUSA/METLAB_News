package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Organisation;



public class RemoveOrganisationQuery implements IQuery<Void>
{
	public RemoveOrganisationQuery(Organisation organisation)
	{
		m_organisation = organisation;
	}

	private RemoveOrganisationQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			MariaDbConnector.getInstance().deleteEntity(m_organisation);
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



	protected Organisation m_organisation;
}
