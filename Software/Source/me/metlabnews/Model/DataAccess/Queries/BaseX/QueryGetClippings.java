package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Clipping;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * This query loads all clippings made for the selected profileID.
 * Executing this query results in a number of queries being executed in the background,
 * because profiles only contain articleIDs from which the actual article needs to be queried.
 *
 * @author Erik Hennig
 */
public class QueryGetClippings extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetClippings.class, Logger.Channel.DocDBMS);
	}

	public String profileID = "";

	private ArrayList<Clipping> m_results = new ArrayList<>();

	public List<Clipping> getResults()
	{
		return Collections.unmodifiableList(m_results);
	}

	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $clipping in /clipping\n" +
				" where $clipping/profileID = '" + profileID + "'\n" +
				" order by $clipping/generationtime\n" +
				" return $clipping";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag res = new XMLTag("<clippings>" + str + "</clippings>");
		for(XMLTag tmp : res.children("clipping"))
		{
			try
			{
				Clipping c = new Clipping(tmp);
				m_results.add(c);
			}
			catch(IllegalArgumentException e)
			{
				m_logger.logError(this, "Database contains invalid res.");
			}
		}
	}
}
