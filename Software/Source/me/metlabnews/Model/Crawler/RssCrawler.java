package me.metlabnews.Model.Crawler;
import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.AddArticle;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryTitleExists;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.RSSFeed;
import me.metlabnews.Model.Entities.NewsSource;

import java.io.IOException;
import java.util.ArrayList;



public class RssCrawler implements Runnable
{

	private boolean    m_running = false;
	private NewsSource m_source;

	private Thread m_t;

	RssCrawler(NewsSource source)
	{
		this.m_source = source;
	}

	static void initialize()
	{
		Logger.getInstance().register(RssCrawler.class, Logger.Channel.Crawler);
	}

	void start()
	{
		if(!m_running)
		{
			m_running = true;
			m_t = new Thread(this);
			m_t.start();
		}
	}

	@Override
	public void run()
	{
		Logger.getInstance().logDebug(this, "started crawler for \"" + m_source.getName() + "\"");
		while(m_running)
		{
			Logger.getInstance().logDebug(this, "crawling " + m_source.getName() + " --> " + m_source.getRss_link());
			try
			{
				String             doc      = Helper.getHTTPResponse(m_source.getRss_link());
				ArrayList<Article> articles = RSSFeed.parseFeed(doc, this.m_source).getArticles();
				for(Article a : articles)
				{
					boolean exists = articleExists(a);
					if(!exists)
					{
						writeToBaseX(a);
					}
				}
				try
				{
					Thread.sleep(ConfigurationManager.getInstance().getCrawlerTimeout());
				}
				catch(InterruptedException ignored)
				{
				}
			}
			catch(IOException e)
			{
				Logger.getInstance().logWarning(this, m_source.getName() + "is not reachable!");
			}
		}
		Logger.getInstance().logDebug(this, "stopped crawler on \"" + m_source.getName() + "\"");
	}

	private void writeToBaseX(Article a)
	{
		AddArticle addArticle = new AddArticle(a);
		addArticle.execute();
		Logger.getInstance().logDebug(this, addArticle.getResult());
	}

	private boolean articleExists(Article a)
	{
		QueryTitleExists qte = new QueryTitleExists(a.getTitle());
		qte.execute();
		Logger.getInstance().logDebug(this, "exists \"" + a.getTitle() + "\"? --> " + qte.getResult());
		return qte.getResult();
	}

	void stop()
	{
		Logger.getInstance().logDebug(this, "stopping crawler on \"" + m_source.getName() + "\"");
		m_running = false;
	}

	public NewsSource getSource()
	{
		return m_source;
	}
}