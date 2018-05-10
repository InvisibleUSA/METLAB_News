package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.Clipping;
import org.basex.core.Command;
import org.basex.core.cmd.Add;



public class QueryAddClipping extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryAddClipping.class, Logger.Channel.DocDBMS);
	}

	public Clipping clipping;
	public String result;

	@Override
	protected Command createBaseXQuery()
	{
		//TODO implement clipping
		String filename = "/Clippings/" /*+ clipping.getProfileName() + "/" + clipping.getGenerationTime().format(
				DateTimeFormatter.ofPattern("YYYY-MM-DD"))*/;
		return new Add(filename, clipping.toString());
	}

	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
