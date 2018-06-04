package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.NewsSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



/**
 * Get the sources from SQL
 *
 * @author Benjamin Gerlach
 */
public class QueryGetSources extends MariaDBQueryBase
{
	private ArrayList<NewsSource> m_sources = new ArrayList<>();

	static
	{
		Logger.getInstance().register(QueryGetSources.class, Logger.Channel.RDBMS);
	}

	@Override
	protected Object[] createSQLQuery() {
		return new String[] {"SELECT * FROM Quellen"};
	}

	@Override
	protected void processResults(ResultSet rs) {
		try {
			while(rs.next()) {
				String     name     = rs.getString("Name");
				String     link     = rs.getString("Link");
				String     rss_link = rs.getString("RSS_FEED");
				NewsSource source   = new NewsSource(name, link, rss_link);
				m_sources.add(source);
				rs.close();
			}
		}
		catch(SQLException e) {
			Logger.getInstance().logError(this, e.getMessage());
		}
	}

	public ArrayList<NewsSource> getSources() {
		return m_sources;
	}
}
