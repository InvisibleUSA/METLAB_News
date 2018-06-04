package me.metlabnews.Model.Crawler;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.NewsSource;

import java.io.IOException;
import java.util.ArrayList;



/**
 * this class is the representation of a WebCrawler
 * 
 * it is only a bridge to the YaCy web-interface and
 * does not do any of the work, therefore it does
 * not implement the runnable interface
 * 
 *
 * @author Benjamin Gerlach
 */
public class WebCrawler
{
	private NewsSource m_source;

	private final String  m_yacy_pw;
	private final String  m_yacy_user;
	private final String  m_yacy_address;
	private final boolean m_yacy_is_protected;

	/**
	 * creates a new webcrawler with the given source
	 *
	 * @param source this crawlers {@link NewsSource}
	 */
	WebCrawler(NewsSource source) {
		this.m_source = source;
		this.m_yacy_pw = ConfigurationManager.getInstance().getCrawlerYaCyPassword();
		this.m_yacy_user = ConfigurationManager.getInstance().getCrawlerYaCyUser();
		this.m_yacy_address = ConfigurationManager.getInstance().getCrawlerYaCyAddress();
		this.m_yacy_is_protected = ConfigurationManager.getInstance().getCrawlerYaCyIsProtected();
	}

	/**
	 * starts a crawler on the given source via YaCy WebAPI
	 * <a href='http://www.yacy-websuche.de/wiki/index.php/Dev:APICrawler'>YaCy Dev API for Crawler_p.html</a>
	 */
	void start() {
		boolean isRunning = isRunning();
		Logger.getInstance().logInfo(this, "YaCy current status: is running? -> " + isRunning);
		if(!isRunning)
		{
			String startURL = m_yacy_address + "/Crawler_p.html?" +
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
			try {
				if(m_yacy_is_protected)
				{
					Helper.getHTTPResponse(startURL, m_yacy_user, m_yacy_pw);
				}
				else
				{
					Helper.getHTTPResponse(startURL);
				}
				Logger.getInstance().logInfo(this, "Started new YaCy crawler for " + m_source.getName());
			}
			catch(IOException e) {
				Logger.getInstance().logWarning(this, "YaCy isn't running... or: " + e.getMessage());
			}
		}
	}

	/**
	 * stops a crawler on the given source via YaCy WebAPI
	 */
	void stop() {
		Logger.getInstance().logInfo(this, "Stopping Webcrawler on " + m_source.getName() + "...");

		if(isRunning()) {
			String handle = getHandle();
			String url    = m_yacy_address + "/Crawler_p.html?terminate=Terminate&handle=" + handle;
			try {
				if(m_yacy_is_protected)
				{
					Helper.getHTTPResponse(url, m_yacy_user, m_yacy_pw);
				}
				else
				{
					Helper.getHTTPResponse(url);
				}
			}
			catch(IOException e) {
				Logger.getInstance().logWarning(this, "YaCy isn't running... or: " + e.getMessage());
			}
		}
	}

	/**
	 * Checks if a crawler is running by searching the crawl profiles from YaCy
	 * <a href='http://www.yacy-websuche.de/wiki/index.php/Dev:APICrawlProfileEditor'>YaCy Dev API for CrawlProfileEditor_p.html</a>
	 *
	 * @return true if a crawler is running on that source
	 */
	public boolean isRunning() {
		try {
			String response;
			if(m_yacy_is_protected)
			{
				response = Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml", m_yacy_user, m_yacy_pw);
			}
			else
			{
				response = Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml");
			}
			XMLTag            profileSummary = new XMLTag(response);
			ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
			for(XMLTag tag : profiles) {
				if(tag.child("name").value().equals(m_source.getLink())) {
					return true;
				}
			}
		}
		catch(IOException e) {
			Logger.getInstance().logWarning(this, "YaCy isn't running... or: " + e.getMessage());
		}
		return false;
	}

	/**
	 * to stop a crawler one needs the handle YaCy assigned to this profile
	 * <a href='http://www.yacy-websuche.de/wiki/index.php/Dev:APICrawlProfileEditor'>YaCy Dev API for CrawlProfileEditor_p.html</a>
	 *
	 * @return the YaCy handle for the source
	 */
	public String getHandle() {
		try {
			String response;
			if(m_yacy_is_protected)
			{
				response = Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml", m_yacy_user, m_yacy_pw);
			}
			else
			{
				response = Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml");
			}
			XMLTag            profileSummary = new XMLTag(response);
			ArrayList<XMLTag> profiles       = profileSummary.children("crawlProfile");
			for(XMLTag tag : profiles) {
				if(tag.child("name").value().equals(m_source.getLink())) {
					return tag.child("handle").value();
				}
			}
		}
		catch(IOException e) {
			Logger.getInstance().logWarning(this, "YaCy isn't running... or: " + e.getMessage());
		}
		return null;
	}

	/**
	 * @return the source this webcrawler runs on
	 */
	public NewsSource getSource() {
		return m_source;
	}
}
