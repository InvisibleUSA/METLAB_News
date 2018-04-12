package me.metlabnews.Model.Crawler;



import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.RSSFeed;
import me.metlabnews.Model.Entities.Source;
import org.basex.core.cmd.Add;
import java.util.ArrayList;



public class RSSCrawler //implements Runnable
{
	/*
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
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,"started crawler for \"" + m_source.getName() + "\"");
		while(m_running)
		{
			Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,"crawling " + m_source.getName() + " --> " + m_source.getRss_link());
			String doc = Helper.getHTTPResponse(m_source.getRss_link());
			ArrayList<Article> articles = RSSFeed.parseFeed(doc, this.m_source).getArticles();
			for(Article a : articles)
			{
				boolean exists = articleExists(a);
				if(!exists)
				{
					writeToBaseX(a, m_source);
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
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,"stopped crawler on \"" + m_source.getName() + "\"");
	}

	private void writeToBaseX(Article a, Source source)
	{
		BaseXController bsx  = BaseXController.getInstance();
		String          date = a.getDateFormatted();
		Add add = new Add("Artikel/" + source.getName() + "/" + date + "/" + a.getFileName(),
		                  a.toString());
		String res1 = add.toString();
		String res2 = bsx.execute(add);
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile, res1);
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile, res2);
	}

	private boolean articleExists(Article a)
	{
		BaseXController bsx    = BaseXController.getInstance();
		String          xquery = "/article/title = \"" + a.getM_title() + "\"";
		String          result = bsx.query(xquery);
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,"exists \"" + a.getTitle() + "\"? --> " + result);
		if(result.equals("true"))
		{
			return true;
		}
		return false;
	}

	public void stop()
	{
		Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,"stopping crawler on \"" + m_source.getName() + "\"");
		m_running = false;
	}

	public Source getM_source()
	{
		return m_source;
	}
	*/
}
