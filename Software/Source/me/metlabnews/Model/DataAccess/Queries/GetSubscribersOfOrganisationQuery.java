package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Get all subscribers associated with an organisation
 */
public class GetSubscribersOfOrganisationQuery implements IQuery<List<Subscriber>>
{
	/**
	 * @param organisation Organisation to match
	 */
	public GetSubscribersOfOrganisationQuery(Organisation organisation)
	{
		m_organisation = organisation;
	}

	@SuppressWarnings("unused")
	private GetSubscribersOfOrganisationQuery()
	{}


	/**
	 * @return returns false in case of an error;
	 *         returns true otherwise
	 */
	@Override
	public boolean execute()
	{
		boolean success = true;
		// TODO: replace property string with reflection
		List<Object> result = RelationalDbConnector.getInstance().getEntityList(
				Subscriber.class.getSimpleName(), "organisationId",
				m_organisation);
		try
		{
			// cast List<Object> to List<Subscriber>
			m_result = result.stream()
					.map(Subscriber.class::cast)
					.collect(Collectors.toList());
		}
		catch(NullPointerException e)
		{
			success = false;
		}
		return success;
	}


	@Override
	public List<Subscriber> getResult()
	{
		return m_result;
	}



	private Organisation     m_organisation;
	private List<Subscriber> m_result;
}
