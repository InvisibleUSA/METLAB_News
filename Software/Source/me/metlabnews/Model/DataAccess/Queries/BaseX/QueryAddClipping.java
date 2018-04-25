package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Entities.Clipping;
import org.basex.core.Command;
import org.basex.core.cmd.Add;

import java.time.format.DateTimeFormatter;



public class QueryAddClipping extends BaseXQueryBase
{
	public Clipping clipping;

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

	}
}
