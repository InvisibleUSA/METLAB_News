package me.metlabnews.Model.DataAccess.Queries.BaseX;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;
import org.basex.core.Command;
import org.basex.core.cmd.Add;



/**
 * adds  a profile template to basex
 * returns: nothing
 * @author Marco Rempfer
 */
public class QueryAddProfileTemplate extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryAddProfileTemplate.class, Logger.Channel.DocDBMS);
	}

	public ObservationProfileTemplate template;
	public String                     result;

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("/templates/" + template.getID(), template.toXML());
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
