package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



public class QueryGetProfileTemplateById extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetProfileTemplateById.class, Logger.Channel.DocDBMS);
	}

	public String templateID = "";
	private ObservationProfileTemplate m_template;

	public ObservationProfileTemplate getTemplate()
	{
		return m_template;
	}


	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $template in /templates\n" +
				" where $template/id = '" + templateID + "'\n" +
				" return $template";
		return new XQuery(query);
	}


	@Override
	protected void processResults(String str)
	{
		try
		{
			XMLTag result = new XMLTag(str);
			m_template = new ObservationProfileTemplate(result);
		}
		catch(IllegalArgumentException e)
		{
			m_logger.logError(this, "Database contains invalid article. " + e.toString());
		}
	}
}
