package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Organisation;



/**
 * Get exactly one organisation with matching name
 */
public class GetOrganisationQuery implements IQuery<Organisation>
{
	/**
	 * @param name organisation name to match
	 */
	public GetOrganisationQuery(String name)
	{
		m_name = name;
	}

	@SuppressWarnings("unused")
	private GetOrganisationQuery()
	{}


	/**
	 * @return returns false if
	 *         - no organisation with matching name was found
	 *         - more than one organisation with matching name was found
	 *         returns true otherwise
	 */
	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		try
		{
			m_result = (Organisation)RelationalDbConnector.getInstance().getUniqueEntity(
					Organisation.class.getSimpleName(), "name", m_name);
		}
		catch(RequestedDataDoesNotExistException e)
		{
			success = false;
		}
		catch(UnexpectedNonUniqueDataException e)
		{
			success = false;
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR,
			                         "in GetOrganisationQuery.execute(): " +
					                         "organisation with non-unique name : " + m_name);
		}
		return success;
	}


	@Override
	public Organisation getResult()
	{
		return m_result;
	}



	private String       m_name;
	private Organisation m_result;
}
