package me.metlabnews.Model.Crawler;



import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.AddArticle;
import me.metlabnews.Model.DataAccess.Queries.QueryTitleExists;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.RSSFeed;
import me.metlabnews.Model.Entities.Source;
import org.basex.core.cmd.Add;
import java.util.ArrayList;



public class RSSCrawler implements Runnable
{

	private boolean m_running = false;
	private Source  m_source;

	private Thread m_t;

	public RSSCrawler(Source source)
	{
		this.m_source = source;
	}

	public void start()
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
		Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG, Logger.enum_logType.ToFile,
		                         "started crawler for \"" + m_source.getName() + "\"");
		while(m_running)
		{
			Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG,
			                         Logger.enum_logType.ToFile,
			                         "crawling " + m_source.getName() + " --> " + m_source.getRss_link());
			String doc = Helper.getHTTPResponse(m_source.getRss_link());
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
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG, Logger.enum_logType.ToFile,
		                         "stopped crawler on \"" + m_source.getName() + "\"");
	}

	private void writeToBaseX(Article a)
	{
		AddArticle addArticle = new AddArticle(a);
		addArticle.execute();
		Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG, Logger.enum_logType.ToFile,
		                         addArticle.getResult());
	}

	private boolean articleExists(Article a)
	{
		QueryTitleExists qte = new QueryTitleExists(a.getTitle());
		qte.execute();
		boolean result = qte.getResult();
		Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG, Logger.enum_logType.ToFile,
		                         "exists \"" + a.getTitle() + "\"? --> " + result);
		if(result)
		{
			return true;
		}
		return false;
	}

	public void stop()
	{
		Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG, Logger.enum_logType.ToFile,
		                         "stopping crawler on \"" + m_source.getName() + "\"");
		m_running = false;
	}

	public Source getM_source()
	{
		return m_source;
	}
}
