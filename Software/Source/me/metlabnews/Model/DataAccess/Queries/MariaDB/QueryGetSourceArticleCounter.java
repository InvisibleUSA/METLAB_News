package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.NewsSource;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryGetSourceArticleCounter extends MariaDBQueryBase {

	private int        m_numArticles;
	private NewsSource m_source;

	public QueryGetSourceArticleCounter(NewsSource source) {
		m_source = source;
	}

	@Override
	protected Object[] createSQLQuery() {
		return new String[] {"SELECT rss_articlecounter FROM Quellen WHERE Name='" + m_source.getName() + "'"};
	}

	@Override
	protected void processResults(ResultSet rs) {
		try {
			if(rs.next()) {
				m_numArticles = rs.getInt("rss_articlecounter");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public int getNumArticles() {
		return m_numArticles;
	}
}
