package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddArticle;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryTitleExists;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetSourceArticleCounter;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QuerySetSourceArticleCounter;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.RSSFeed;
import me.metlabnews.Model.Entities.NewsSource;

import java.io.IOException;
import java.util.ArrayList;



/**
 * This class crawls RssFeeds and saves them in the BaseX-Database
 *
 * @author Benjamin Gerlach
 */
public class RssCrawler implements Runnable
{

	private boolean    m_running = false;
	private NewsSource m_source;

	private Thread m_t;

	/**
	 * RssCrawler crawls RssFeeds in its own thread
	 * to start with the crawl call start()
	 *
	 * @param source has to be a source with valid RSSLink
	 */
	RssCrawler(NewsSource source)
	{
		this.m_source = source;
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

	/**
	 * executes one crawl and sleeps for the Crawler.Timeout setting
	 */
	@Override
	public void run()
	{
		Logger.getInstance().logInfo(this, "started crawler for \"" + m_source.getName() + "\"");
		while(m_running)
		{
			Logger.getInstance().logInfo(this, "crawling " + m_source.getName() + " --> " + m_source.getRss_link());
			try
			{
				String             doc      = Helper.getHTTPResponse(m_source.getRss_link());
				Logger.getInstance().logDebug(this, doc);
				ArrayList<Article> articles = RSSFeed.parseFeed(doc, this.m_source).getArticles();
				QueryGetSourceArticleCounter count = new QueryGetSourceArticleCounter();
				for(Article a : articles) {
					count.source = m_source;
					if(count.execute()) {
						String guid = m_source.getName() + count.getNumArticles();
						a.setGuid(guid);
						boolean exists = articleExists(a);
						if(!exists)
						{
							writeToBaseX(a);
							new QuerySetSourceArticleCounter(count.getNumArticles() + 1, m_source).execute();
						}
					}
					else
					{
						Logger.getInstance().logError(this,
						                              "failed to add article :\"" + a.getTitle() + "\" because sql query failed");
					}
				}
			}
			catch(IOException e)
			{
				Logger.getInstance().logWarning(this, m_source.getName() + " is not reachable!");
			}
			catch(NullPointerException e)
			{
				Logger.getInstance().logWarning(this, m_source.getName() + "s feed is not valid!");
				Logger.getInstance().logError(this, e.getMessage() + "\n" + e.getCause());
			}
			try
			{
				Thread.sleep(ConfigurationManager.getInstance().getCrawlerTimeout());
			}
			catch(InterruptedException ignored)
			{

			}
		}
		Logger.getInstance().logInfo(this, "stopped crawler on \"" + m_source.getName() + "\"");
	}

	/**
	 * writes the given article to BaseX
	 *
	 * @param a the given article
	 */
	private void writeToBaseX(Article a)
	{
		QueryAddArticle addArticle = new QueryAddArticle(a);
		addArticle.execute();
		Logger.getInstance().logDebug(this, addArticle.getResult());
	}

	/**
	 * @param a the article to check existence for
	 * @return true if the article exists, false if not
	 */
	private boolean articleExists(Article a)
	{
		QueryTitleExists qte = new QueryTitleExists(a.getTitle());
		if(qte.execute())
		{
			Logger.getInstance().logDebug(this, "exists \"" + a.getTitle() + "\"? --> " + qte.getResult());
			return qte.getResult();
		}
		else
		{
			//if not sure because query failed return true
			return true;
		}
	}

	/**
	 * stops the RssCrawler, but not immediately
	 */
	void stop()
	{
		Logger.getInstance().logInfo(this, "stopping crawler on \"" + m_source.getName() + "\"");
		m_running = false;
	}

	public NewsSource getSource()
	{
		return m_source;
	}
}