package me.metlabnews.Model.Crawler;



import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.Source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class WebCrawler
{
	private Source m_source;

	public WebCrawler(Source source)
	{
		this.m_source = source;
	}

	public void start()
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
			Logger.getInstance().log(Logger.enum_channel.Crawler, Logger.enum_logPriority.DEBUG,
			                         Logger.enum_logType.ToConsole,
			                         Helper.getHTTPResponse(startURL));
		}
	}

	public void stop()
	{
		if(isRunning())
		{

		}
	}

	public boolean isRunning()
	{
		XMLTag profileSummary = new XMLTag(
				Helper.getHTTPResponse("http://localhost:8090/CrawlProfileEditor_p.xml"));
		ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
		for(XMLTag tag : profiles)
		{
			if(tag.child("name").value().equals(m_source.getLink()))
			{
				return true;
			}
		}
		return false;
	}

	public Source getSource()
	{
		return m_source;
	}
}
