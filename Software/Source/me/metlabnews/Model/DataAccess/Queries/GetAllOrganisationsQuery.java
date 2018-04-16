package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;

import java.util.List;
import java.util.stream.Collectors;



/**
 * Get all organisations stored in database
 */
public class GetAllOrganisationsQuery implements IQuery<List<Organisation>>
{
	/**
	 * @return returns false in case of an error
	 *         returns true otherwise
	 */
	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		List<Object> result = RelationalDbConnector.getInstance().getEntityList(
				Organisation.class.getSimpleName());

		try
		{
			// cast List<Object> to List<Organisation>
			m_result = result.stream()
					.map(Organisation.class::cast)
					.collect(Collectors.toList());
		}
		catch(NullPointerException e)
		{
			success = false;
		}
		return success;
	}


	@Override
	public List<Organisation> getResult()
	{
		return m_result;
	}



	private List<Organisation> m_result;
}
