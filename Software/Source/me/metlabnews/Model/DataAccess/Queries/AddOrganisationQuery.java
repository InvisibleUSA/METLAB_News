package me.metlabnews.Model.DataAccess.Queries;



import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.Entities.Organisation;



public class AddOrganisationQuery implements IQuery<Void>
{
	public AddOrganisationQuery(Organisation organisation)
	{
		m_organisation = organisation;
	}

	private AddOrganisationQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		try
		{
			MariaDbConnector.getInstance().addEntity(m_organisation);
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



	protected Organisation m_organisation;
}
