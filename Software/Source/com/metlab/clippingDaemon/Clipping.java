package com.metlab.clippingDaemon;

import com.metlab.crawler.Article;
import com.metlab.crawler.Profile;
import org.basex.core.cmd.Find;

import java.util.List;



public class Clipping
{
	private List<Article> m_articles;

	public Clipping(Profile p)
	{
		//for (p.)

		Find f = new Find("");
	}

	public void addArticle(Article a)
	{
		m_articles.add(a);
	}
}
