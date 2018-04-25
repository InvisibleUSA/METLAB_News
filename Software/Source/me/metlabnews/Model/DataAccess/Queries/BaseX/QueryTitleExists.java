package me.metlabnews.Model.DataAccess.Queries.BaseX;

import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



public class QueryTitleExists extends BaseXQueryBase
{
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
		if(str.equals("true"))
		{
			m_result = true;
		}
		else
		{
			m_result = false;
		}
	}

	public boolean getResult()
	{
		return m_result;
	}
}
