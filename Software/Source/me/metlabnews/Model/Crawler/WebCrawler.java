package me.metlabnews.Model.Crawler;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
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

	//TODO has to be replaced with config options
	private static String m_yacy_pw      = "123metlab123";
	private static String m_yacy_user    = "admin";
	private static String m_yacy_address = "http://metlabnews.me:8090";

	WebCrawler(NewsSource source) {
		this.m_source = source;
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
				Helper.getHTTPResponse(startURL, m_yacy_user, m_yacy_pw);
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
				Helper.getHTTPResponse(url, m_yacy_user, m_yacy_pw);
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
			XMLTag profileSummary = new XMLTag(
					Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml", m_yacy_user, m_yacy_pw)
			);
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
			XMLTag profileSummary = new XMLTag(
					Helper.getHTTPResponse(m_yacy_address + "/CrawlProfileEditor_p.xml", m_yacy_user, m_yacy_pw)
			);
			ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
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

	public NewsSource getSource() {
		return m_source;
	}
}
