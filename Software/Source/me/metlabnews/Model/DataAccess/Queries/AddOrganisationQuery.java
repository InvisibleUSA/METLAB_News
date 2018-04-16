package me.metlabnews.Model.DataAccess.Queries;



import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.Entities.Organisation;



/**
 * Add organisation to database.
 */
public class AddOrganisationQuery implements IQuery<Void>
{
	/**
	 * @param organisation Organisation to add
	 */
	public AddOrganisationQuery(Organisation organisation)
	{
		m_organisation = organisation;
	}

	@SuppressWarnings("unused")
	private AddOrganisationQuery()
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
			RelationalDbConnector.getInstance().addEntity(m_organisation);
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



	private Organisation m_organisation;
}
