package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.Source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class QueryGetSources extends QueryBase
{
	ArrayList<Source> m_sources = new ArrayList<>();

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Quellen";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		try
		{
			while(rs.next())
			{
				String name     = rs.getString("Name");
				String link     = rs.getString("Link");
				String rss_link = rs.getString("RSS_FEED");
				Source source   = new Source(name, link, rss_link);
				m_sources.add(source);
			}
		}
		catch(SQLException e)
		{
			Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile, e.getMessage());
		}
	}

	public ArrayList<Source> getSources()
	{
		return m_sources;
	}
}
