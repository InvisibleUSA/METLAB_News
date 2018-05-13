package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;
import org.basex.core.Command;
import org.basex.core.cmd.Replace;


/**
 *  changes a profile template that is already in basex
 *  returns: nothing
 * @author Marco Rempfer
 */
public class QueryUpdateProfileTemplate extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryUpdateProfileTemplate.class, Logger.Channel.DocDBMS);
	}

	public ObservationProfileTemplate template;
	public String                     result;

	@Override
	protected Command createBaseXQuery()
	{
		return new Replace("/profiles/" + template.getID(), template.toXML());
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
