package com.metlab.crawler;

import com.metlab.frontend.controller.SQLController;

import java.util.ArrayList;
import java.util.List;



public class CrawlerController implements Runnable
{
	private static CrawlerController cc;
	List<RSSCrawler> RSSCrawler = new ArrayList<>();

	private Thread t;
	private boolean running   = true;
	private boolean debug     = false;
	private int     sleeptime = 60000;

	private CrawlerController()
	{

	}

	public static CrawlerController getInstance()
	{
		if(cc == null)
		{
			cc = new CrawlerController();
		}
		return cc;
	}

	public void stop()
	{
		for(RSSCrawler c : RSSCrawler)
		{
			c.stop();
		}
		stop();
	}

	public void setDebug(boolean debug)
	{
		for(RSSCrawler c : RSSCrawler)
		{
			c.setDebug(debug);
		}
		this.debug = debug;
	}

	public void setSleeptime(int sleeptime)
	{
		for(RSSCrawler c : RSSCrawler)
		{
			c.setSleeptime(sleeptime);
		}
		this.sleeptime = sleeptime;
	}

	public void start()
	{
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run()
	{
		while(running)
		{
			ArrayList<Source> sources = SQLController.getInstance().getSources();
			//go through all sources
			for(Source curr_src : sources)
			{
				RSSCrawler curr_RSS_crawler = null;
				//look for a RSSCrawler on that source
				for(RSSCrawler c : RSSCrawler)
				{
					if(curr_src.getName().equals(c.getSource().getName())
							&& curr_src.getLink().equals(c.getSource().getLink())
							&& curr_src.getRss_link().equals(c.getSource().getRss_link()))
					{
						curr_RSS_crawler = c;
					}
				}
				//if there is no RSSCrawler for that source --> create one
				if(curr_RSS_crawler == null)
				{
					curr_RSS_crawler = new RSSCrawler(curr_src);
					curr_RSS_crawler.setSleeptime(sleeptime);
					curr_RSS_crawler.setDebug(debug);
					RSSCrawler.add(curr_RSS_crawler);
					curr_RSS_crawler.start();
				}
			}
			//go through all crawlers and kill crawlers with dead sources
			ArrayList<RSSCrawler> toremove = new ArrayList<>();
			for(RSSCrawler c : RSSCrawler)
			{
				boolean hasValidSource = false;
				for(Source curr_src : sources)
				{
					if(curr_src.getName().equals(c.getSource().getName())
							&& curr_src.getLink().equals(c.getSource().getLink())
							&& curr_src.getRss_link().equals(c.getSource().getRss_link()))
					{
						hasValidSource = true;
					}
				}
				if(!hasValidSource)
				{
					toremove.add(c);
					c.stop();
				}
			}
			RSSCrawler.removeAll(toremove);
			try
			{
				Thread.sleep(sleeptime);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
