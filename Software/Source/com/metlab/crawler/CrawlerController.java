package com.metlab.crawler;

import java.util.ArrayList;



public class CrawlerController
{
	private static CrawlerController cc;
	ArrayList<Crawler> crawler = new ArrayList<>();
	ArrayList<Source>  sources = new ArrayList<>();

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

	public void addSource(Source source)
	{
		sources.add(source);
		Crawler newCrawler = new Crawler(source);
		crawler.add(newCrawler);
	}

	public void stop()
	{
		for(Crawler c : crawler)
		{
			c.stop();
		}
	}

	public void setDebug(boolean debug)
	{
		for(Crawler c : crawler)
		{
			c.setDebug(debug);
		}
	}

	public void setSleeptime(int sleeptime)
	{
		for(Crawler c : crawler)
		{
			c.setSleeptime(sleeptime);
		}
	}

	public void start()
	{
		for(Crawler c : crawler)
		{
			new Thread(c).start();
		}
	}
}
