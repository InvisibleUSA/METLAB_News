package me.metlabnews.Model.DataAccess.Queries.YaCy;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.NewsSource;
import me.metlabnews.Model.Entities.RSSFeed;

import java.util.ArrayList;
import java.util.Collection;



/**
 * search an article with YaCy
 * the result is an rss feed with the first (defined in m_maximumRecords) results from rss
 */
public class QuerySearchArticle extends YaCyQueryBase
{
	static {
		Logger.getInstance().register(QuerySearchArticle.class, Logger.Channel.YaCy);
	}

	private int                m_maximumRecords = 10;
	//determines if results where terms are near should be higher ranked
	private boolean            m_rankNearHigher = true;
	//determines if results are sorted by date
	//by default false because it really just sorts the results by date
	//therefore there are really crappy results in the first 10
	//at least for the things that could be in ads, because they get recognized...
	private boolean            m_sortByDate     = false;
	private NewsSource         m_source         = null;
	private String[]           m_searchTerms    = {};
	private ArrayList<Article> m_articles;

	public QuerySearchArticle(NewsSource source, String... searchTerms) {
		m_searchTerms = searchTerms;
		m_source = source;
	}

	public QuerySearchArticle(String... searchTerms) {
		m_searchTerms = searchTerms;
	}

	public void setSeatchTerms(String... searchTerms)
	{
		m_searchTerms = searchTerms;
	}

	/**
	 * @param searchTerms must be an Collection with only Strings in it
	 */
	public void setSearchTerms(Collection searchTerms)
	{
		m_searchTerms = new String[searchTerms.size()];
		int      i     = 0;
		Object[] terms = searchTerms.toArray();
		for(Object currTerm : terms)
		{
			if(currTerm instanceof String)
			{
				m_searchTerms[i] = (String)terms[i];
			}
			else
			{
				m_searchTerms[i] = "";
			}
			i++;
		}
	}

	public void setSource(NewsSource source)
	{
		m_source = source;
	}

	public void setMaximumRecords(int maximumRecords) {
		m_maximumRecords = maximumRecords;
	}

	public void setSortByDate(boolean sortByDate) {
		m_sortByDate = sortByDate;
	}

	public ArrayList<Article> getArticles() {
		return m_articles;
	}

	@Override
	protected String createYaCyQuery() {
		String query = "";
		for(int i = 0; i < m_searchTerms.length; i++) {
			if(i > 1 && m_rankNearHigher) {
				//the search results where the terms are nearer together will be ranked higher
				query += "%20/near%20";
			}
			query += m_searchTerms[i] + "%20";
		}
		if(m_sortByDate) {
			//sort search results by date
			query += "/date%20";
		}
		if(m_source != null) {
			query += "site:" + m_source.getLink();
		}
		String url = ConfigurationManager.getInstance().getCrawlerYaCyAddress() + "/yacysearch.rss?query=" + query + "&maximumRecords=" + m_maximumRecords;
		Logger.getInstance().logDebug(this, url);
		return url;
	}

	@Override
	protected void processResults(String result) {
		RSSFeed search_res = RSSFeed.parseFeed(result,
		                                       new NewsSource("YaCy", "localhost:8090", "localhost:8090"));
		if(search_res != null) {
			m_articles = search_res.getArticles();
			m_articles.forEach((a) -> a.setisRSS(false));
		}
	}
}
