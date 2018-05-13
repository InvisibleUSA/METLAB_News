package me.metlabnews.Model.DataAccess.Queries.BaseX;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Clipping;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class QueryGetProfilesByEmail extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetClippings.class, Logger.Channel.DocDBMS);
	}

	public String subscriberEmail = "";

	private ArrayList<Clipping> m_results = new ArrayList<>();

	public List<Clipping> getResults()
	{
		return Collections.unmodifiableList(m_results);
	}


	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $profile in /profile\n" +
				" where $profile/owner = '" + subscriberEmail + "'\n" +
				" return $profile";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag res = new XMLTag("<profiles>" + str + "</profiles>");
		for(XMLTag tmp : res.children("profile"))
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
