package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.NewsSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class QueryGetSources extends MariaDBQueryBase
{
	private ArrayList<NewsSource> m_sources = new ArrayList<>();

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Quellen";
	}

	@Override
	protected void processResults(ResultSet rs)
	{
		try
		{
			while(rs.next())
			{
				String     name     = rs.getString("Name");
				String     link     = rs.getString("Link");
				String     rss_link = rs.getString("RSS_FEED");
				NewsSource source   = new NewsSource(name, link, rss_link);
				m_sources.add(source);
			}
		}
		catch(SQLException e)
		{
			Logger.getInstance().logError(this, e.getMessage());
		}
	}

	public ArrayList<NewsSource> getSources()
	{
		return m_sources;
	}
}
