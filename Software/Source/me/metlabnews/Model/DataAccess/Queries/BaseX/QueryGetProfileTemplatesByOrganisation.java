package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class QueryGetProfileTemplatesByOrganisation extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetProfileTemplatesByOrganisation.class, Logger.Channel.DocDBMS);
	}

	public String organisationId = "";

	private ArrayList<ObservationProfileTemplate> m_results = new ArrayList<>();

	public List<ObservationProfileTemplate> getResults()
	{
		return Collections.unmodifiableList(m_results);
	}


	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $template in /templates\n" +
				" where template/organisation = '" + organisationId + "'\n" +
				" return $template";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag result = new XMLTag("<templates>" + str + "</templates>");
		for(XMLTag tag : result.children("template"))
		{
			try
			{
				ObservationProfileTemplate template = new ObservationProfileTemplate(tag);
				m_results.add(template);
			}
			catch(IllegalArgumentException e)
			{
				m_logger.logError(this, "Database contains invalid res.");
			}
		}
	}
}
