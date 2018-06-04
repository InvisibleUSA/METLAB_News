package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.NewsSource;

import java.sql.*;



/**
 * sets the article counter for a given source
 *
 * @author Benjamin Gerlach
 */
public class QuerySetSourceArticleCounter extends MariaDBQueryBase {

	private int        m_numArticles;
	private NewsSource m_source;

	public QuerySetSourceArticleCounter(int numArticles, NewsSource source) {
		m_source = source;
		m_numArticles = numArticles;
	}

	@Override
	protected Object[] createSQLQuery() {
		return new Object[] {
				"UPDATE Quellen SET rss_articlecounter = ? WHERE Name = ?", m_numArticles, m_source.getName()};
	}

	@Override
	protected void processResults(Connection conn, Object[] q) {
		try
		{
			PreparedStatement ps = conn.prepareStatement((String)q[0]);
			for(int i = 1; i < q.length; i++)
			{
				if(q[i] instanceof String)
				{
					ps.setString(i, (String)q[i]);
				}
				else if(q[i] instanceof Integer)
				{
					ps.setInt(i, (int)q[i]);
				}
				else if(q[i] instanceof Date)
				{
					ps.setDate(i, (Date)q[i]);
				}
			}
			ps.executeQuery();
		}
		catch (SQLException e)
		{
			return;
		}
	}


}
