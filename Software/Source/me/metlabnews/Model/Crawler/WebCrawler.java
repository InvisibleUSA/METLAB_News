package me.metlabnews.Model.Crawler;
import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.NewsSource;

import java.io.IOException;
import java.util.ArrayList;



public class WebCrawler
{
	private NewsSource m_source;

	WebCrawler(NewsSource source)
	{
		this.m_source = source;
	}

	static void initialize()
	{
		Logger.getInstance().register(CrawlerController.class, Logger.Channel.Crawler);
	}

	void start()
	{
		if(!isRunning())
		{
			String startURL = "http://localhost:8090/Crawler_p.html?" +
					"crawlingDomMaxPages=100000&" +
					"range=subpath&" +
					"intention=&" +
					"sitemapURL=&" +
					"crawlingQ=on&" +
					"crawlingMode=url&" +
					"mustnotmatch=&" +
					"crawlingFile%24file=&" +
					"crawlingstart=Neuen%20Crawl%20starten&" +
					"mustmatch=.*&" +
					"xsstopw=on&" +
					"indexMedia=off&" +
					"crawlingIfOlderUnit=hour&" +
					"cachePolicy=iffresh&" +
					"indexText=on&" +
					"crawlingIfOlderCheck=on&" +
					"crawlingDomFilterDepth=1&" +
					"crawlingDomFilterCheck=on&" +
					"crawlingIfOlderNumber=1&" +
					"crawlingDepth=4" +
					"&crawlingURL=" + m_source.getLink();
			try
			{
				Logger.getInstance().logDebug(this, Helper.getHTTPResponse(startURL));
			}
			catch(IOException e)
			{
				Logger.getInstance().logError(this, e.getMessage());
			}
		}
	}

	void stop()
	{
		if(isRunning())
		{
			//TODO
		}
	}

	public boolean isRunning()
	{
		try
		{
			XMLTag profileSummary = new XMLTag(
					Helper.getHTTPResponse("http://localhost:8090/CrawlProfileEditor_p.xml")
			);
			ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
			for(XMLTag tag : profiles)
			{
				if(tag.child("name").value().equals(m_source.getLink()))
				{
					return true;
				}
			}
		}
		catch(IOException e)
		{
			Logger.getInstance().logWarning(this, "YaCy isn't running");
		}
		return false;
	}

	public NewsSource getSource()
	{
		return m_source;
	}
}
