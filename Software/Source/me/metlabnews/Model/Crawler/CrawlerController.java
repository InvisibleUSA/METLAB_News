package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.QueryGetSources;
import me.metlabnews.Model.Entities.Source;

import java.util.ArrayList;
import java.util.List;



public class CrawlerController implements Runnable
{

	private static CrawlerController m_cc;
	List<RSSCrawler> m_RSSCrawler = new ArrayList<>();
	List<WebCrawler> m_Webcrawler = new ArrayList<>();

	private Thread  m_t;
	private boolean m_running = true;

	private CrawlerController()
	{

	}

	public static CrawlerController getInstance()
	{
		if(m_cc == null)
		{
			m_cc = new CrawlerController();
		}
		return m_cc;
	}

	public void stop()
	{
		for(RSSCrawler c : m_RSSCrawler)
		{
			c.stop();
		}
		for(WebCrawler w : m_Webcrawler)
		{
			w.stop();
		}
		m_running = false;
	}

	public void start()
	{
		m_t = new Thread(this);
		m_t.start();
	}

	@Override
	public void run()
	{
		while(m_running)
		{
			QueryGetSources qgs = new QueryGetSources();
			qgs.execute();
			ArrayList<Source> sources = qgs.getSources();
			//go through all sources
			for(Source curr_src : sources)
			{
				//if there is a rss link --> RSSCrawler else --> Webcrawler
				if(!curr_src.getRss_link().isEmpty())
				{
					RSSCrawler curr_RSS_crawler = null;
					//look for a m_RSSCrawler on that source
					for(RSSCrawler c : m_RSSCrawler)
					{
						if(curr_src.getName().equals(c.getM_source().getName())
								&& curr_src.getLink().equals(c.getM_source().getLink())
								&& curr_src.getRss_link().equals(c.getM_source().getRss_link()))
						{
							curr_RSS_crawler = c;
						}
					}
					//if there is no m_RSSCrawler for that source --> create one
					if(curr_RSS_crawler == null)
					{
						curr_RSS_crawler = new RSSCrawler(curr_src);
						m_RSSCrawler.add(curr_RSS_crawler);
						curr_RSS_crawler.start();
					}
				}
				else if(!curr_src.getLink().isEmpty())
				{
					boolean exists = false;
					for(WebCrawler wc : m_Webcrawler)
					{
						if(wc.getSource().getName().equals(curr_src))
						{
							exists = true;
							break;
						}
					}
					if(!exists)
					{
						WebCrawler wc = new WebCrawler(curr_src);
						m_Webcrawler.add(wc);
						wc.start();
					}
				}
			}
			//go through all crawlers and kill crawlers with dead sources
			ArrayList<RSSCrawler> RSStoremove = new ArrayList<>();
			for(RSSCrawler c : m_RSSCrawler)
			{
				boolean hasValidSource = false;
				for(Source curr_src : sources)
				{
					if(curr_src.getName().equals(c.getM_source().getName())
							&& curr_src.getLink().equals(c.getM_source().getLink())
							&& curr_src.getRss_link().equals(c.getM_source().getRss_link()))
					{
						hasValidSource = true;
					}
				}
				if(!hasValidSource)
				{
					RSStoremove.add(c);
					c.stop();
				}
			}
			m_RSSCrawler.removeAll(RSStoremove);

			ArrayList<WebCrawler> Webtoremove = new ArrayList<>();
			for(WebCrawler wc : m_Webcrawler)
			{
				boolean hasValidSource = false;
				for(Source curr_src : sources)
				{
					if(curr_src.getName().equals(wc.getSource().getName())
							&& curr_src.getLink().equals(wc.getSource().getLink())
							&& curr_src.getRss_link().equals(wc.getSource().getRss_link()))
					{
						hasValidSource = true;
					}
				}
				if(!hasValidSource)
				{
					Webtoremove.add(wc);
					wc.stop();
				}
			}
			m_Webcrawler.removeAll(Webtoremove);

			try
			{
				Thread.sleep(ConfigurationManager.getInstance().getCrawlerTimeout());
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}