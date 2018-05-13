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

	WebCrawler(NewsSource source) {
		this.m_source = source;
	}

	/**
	 * starts a crawler on the given source via YaCy WebAPI
	 * <a href='http://www.yacy-websuche.de/wiki/index.php/Dev:APICrawler'>YaCy Dev API for Crawler_p.html</a>
	 */
	void start() {
		if(!isRunning()) {
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
			try {
				Helper.getHTTPResponse(startURL);
				Logger.getInstance().logInfo(this, "Started new YaCy crawler for " + m_source.getName());
			}
			catch(IOException e) {
				Logger.getInstance().logError(this, e.getMessage());
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
			String url    = "http://localhost:8090/Crawler_p.html?terminate=Terminate&handle=" + handle;
			try {
				Helper.getHTTPResponse(url);
			}
			catch(IOException e) {
				e.printStackTrace();
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
					Helper.getHTTPResponse("http://localhost:8090/CrawlProfileEditor_p.xml")
			);
			ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
			for(XMLTag tag : profiles) {
				if(tag.child("name").value().equals(m_source.getLink())) {
					return true;
				}
			}
		}
		catch(IOException e) {
			Logger.getInstance().logWarning(this, "YaCy isn't running");
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
					Helper.getHTTPResponse("http://localhost:8090/CrawlProfileEditor_p.xml")
			);
			ArrayList<XMLTag> profiles = profileSummary.children("crawlProfile");
			for(XMLTag tag : profiles) {
				if(tag.child("name").value().equals(m_source.getLink())) {
					return tag.child("handle").value();
				}
			}
		}
		catch(IOException e) {
			Logger.getInstance().logWarning(this, "YaCy isn't running");
		}
		return null;
	}

	public NewsSource getSource() {
		return m_source;
	}
}
