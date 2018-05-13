package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



/**
 * This query checks if an RSS article with this title already exists
 * used by crawler to check if article should be added to basex
 *
 * returns: m_result - title exists
 *
 * @author Benjamin Gerlach
 */
public class QueryTitleExists extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryTitleExists.class, Logger.Channel.DocDBMS);
	}
	private String  m_title;
	private boolean m_result;

	public QueryTitleExists(String title)
	{
		m_title = title;
	}

	@Override
	protected Command createBaseXQuery()
	{
		return new XQuery("/article/title = \"" + m_title + "\"");
	}

	@Override
	protected void processResults(String str)
	{
		m_result = str.equals("true");
	}

	public boolean getResult()
	{
		return m_result;
	}
}
