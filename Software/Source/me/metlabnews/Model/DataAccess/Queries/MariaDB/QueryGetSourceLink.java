package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;

import me.metlabnews.Model.Common.Logger;

import java.sql.SQLException;



public class QueryGetSourceLink extends MariaDBQueryBase
{

	private String m_sourceLink;
	private String m_sourceName;

	static
	{
		Logger.getInstance().register(QueryGetSourceLink.class, Logger.Channel.RDBMS);
	}

	public QueryGetSourceLink(String sourceName)
	{
		m_sourceName = sourceName;
	}

	public void setSourceName(String sourceName)
	{
		m_sourceName = sourceName;
	}

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT Link FROM Quellen WHERE Name='" + m_sourceName + "'"};
	}

	@Override
	protected void processResults(ResultSet rs)
	{
		try
		{
			if(rs.next())
			{
				m_sourceLink = rs.getString("Link");
			}
		}
		catch(SQLException e)
		{
			Logger.getInstance().logError(this, "SQL Exception in GetSourceLink");
		}
	}

	public String getSourceLink()
	{
		return m_sourceLink;
	}
}
