package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;

import java.util.List;
import java.util.stream.Collectors;



public class GetAllOrganisationsQuery implements IQuery<List<Organisation>>
{
	@Override
	public boolean execute()
	{
		boolean success = true;
		m_result = null;
		List<Object> result = RelationalDbConnector.getInstance().getEntityList(
				Organisation.class.getSimpleName());
		// cast List<Object> to List<Organisation>
		m_result = result.stream()
				.map( (object) -> (Organisation)object )
				.collect(Collectors.toList());
		return success;
	}


	@Override
	public List<Organisation> getResult()
	{
		return null;
	}



	protected List<Organisation> m_result;
}
