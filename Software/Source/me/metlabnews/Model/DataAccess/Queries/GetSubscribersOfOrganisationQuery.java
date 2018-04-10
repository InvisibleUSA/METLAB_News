package me.metlabnews.Model.DataAccess.Queries;



import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import java.util.List;
import java.util.stream.Collectors;



public class GetSubscribersOfOrganisationQuery implements IQuery<List<Subscriber>>
{
	public GetSubscribersOfOrganisationQuery(Organisation organisation)
	{
		m_organisation = organisation;
	}

	private GetSubscribersOfOrganisationQuery()
	{}


	@Override
	public boolean execute()
	{
		boolean success = true;
		// TODO: replace property string with reflection
		List<Object> result = MariaDbConnector.getInstance().getEntityList(
				"organisationId", Long.toString(m_organisation.getId()));
		// cast List<Object> to List<Subscriber>
		m_result = result.stream()
				.map( (object) -> (Subscriber)object )
				.collect(Collectors.toList());
		return success;
	}


	@Override
	public List<Subscriber> getResult()
	{
		return m_result;
	}



	protected Organisation      m_organisation;
	protected List<Subscriber>  m_result;
}
