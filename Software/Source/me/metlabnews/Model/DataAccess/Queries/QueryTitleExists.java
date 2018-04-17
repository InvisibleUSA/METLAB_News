package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;



public class QueryTitleExists extends QueryBase
{
	private String  m_title;
	private boolean m_result;

	public QueryTitleExists(String title)
	{
		m_title = title;
	}

	@Override
	protected String createBaseXQuery()
	{
		return "/article/title = \"" + m_title + "\"";
	}

	@Override
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
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
