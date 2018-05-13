package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import org.basex.core.Command;
import org.basex.core.cmd.Delete;



/**
 *   deletes profile template based on its ID
 *
 *   returns: nothing
 *
 * @author Marco Rempfer
 */
public class QueryRemoveProfileTemplate extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryRemoveProfileTemplate.class, Logger.Channel.DocDBMS);
	}

	public String templateID = "";
	public String result = "";

	@Override
	protected Command createBaseXQuery()
	{
		return new Delete("/templates/" + templateID);
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
