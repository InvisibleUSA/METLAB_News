package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.Clipping;
import org.basex.core.Command;
import org.basex.core.cmd.Add;

import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;



public class QuerryAddClipping extends QueryBase
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
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
