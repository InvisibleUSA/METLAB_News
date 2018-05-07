package me.metlabnews.Model.DataAccess.Queries.YaCy;

import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.NewsSource;
import me.metlabnews.Model.Entities.RSSFeed;

import java.util.ArrayList;



public class QuerySearchArticle extends YaCyQueryBase
{
	private int                m_maximumRecords = 10;
	private NewsSource         m_source         = null;
	private String[]           m_searchTerms    = {};
	private ArrayList<Article> m_articles;

	public QuerySearchArticle(NewsSource source, String... searchTerms)
	{
		m_searchTerms = searchTerms;
		m_source = source;
	}

	public QuerySearchArticle(String... searchTerms)
	{
		m_searchTerms = searchTerms;
	}

	public void setMaximumRecords(int maximumRecords)
	{
		m_maximumRecords = maximumRecords;
	}

	public ArrayList<Article> getArticles()
	{
		return m_articles;
	}

	@Override
	protected String createYaCyQuery()
	{
		String query = "";
		for(int i = 0; i < m_searchTerms.length; i++)
		{
			query += m_searchTerms[i] + "%20";
		}
		if(m_source != null)
		{
			query += "site:" + m_source.getLink();
		}
		String url = "http://localhost:8090/yacysearch.rss?query=" + query + "&maximumRecords=" + m_maximumRecords;
		System.out.println(url);
		return url;
	}

	@Override
	protected void processResults(String result)
	{
		System.out.println(result);
		RSSFeed search_res = RSSFeed.parseFeed(result,
		                                       new NewsSource("YaCy Search", "localhost:8090", "localhost:8090"));
		if(search_res != null)
		{
			m_articles = search_res.getArticles();
		}
	}
}
