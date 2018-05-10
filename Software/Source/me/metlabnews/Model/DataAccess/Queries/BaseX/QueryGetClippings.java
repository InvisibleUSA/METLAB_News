package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



public class QueryGetClippings extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryGetClippings.class, Logger.Channel.DocDBMS);
	}
    //TODO filter by profile, date, etc
	@Override
	protected Command createBaseXQuery()
	{
		return new XQuery("/clipping");
	}

	@Override
	protected void processResults(String str)
	{

	}
}
