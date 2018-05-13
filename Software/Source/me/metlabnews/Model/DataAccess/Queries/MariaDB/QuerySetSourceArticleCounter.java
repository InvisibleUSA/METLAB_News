package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.NewsSource;

import java.sql.ResultSet;



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
		return new String[] {
				"UPDATE Quellen SET rss_articlecounter='" + m_numArticles + "' WHERE Name='" + m_source.getName() + "'"};
	}

	@Override
	protected void processResults(ResultSet rs) {

	}
}
