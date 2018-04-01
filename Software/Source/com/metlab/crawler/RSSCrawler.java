package com.metlab.crawler;

import com.metlab.clippingDaemon.Tag;
import com.metlab.controller.BaseXController;
import org.basex.core.cmd.Add;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;



public class RSSCrawler implements Runnable
{

	private boolean debug   = false;
	private boolean running = false;
	private Source source;

	private int sleeptime = 1000;

	Thread t;

	public RSSCrawler(Source source)
	{
		this.source = source;
	}

	public RSSCrawler(int sleeptime, Source source)
	{
		this.sleeptime = sleeptime;
		this.source = source;
	}

	public void start()
	{
		if(!running)
		{
			running = true;
			t = new Thread(this);
			t.start();
		}
	}
    @Override
    public void run(){
	    System.out.println("started crawler for \"" + source.getName() + "\"");
	    while(running)
	    {
		    System.out.println("crawling " + source.getName() + " --> " + source.getRss_link());
		    String                 doc       = getHTTPResponse(source.getRss_link());
		    //try
		    //{
		    Tag                rss_feed = new Tag(doc);
		    ArrayList<Article> articles = extractArticles(rss_feed);
			    for(Article a : articles)
			    {
				    boolean exists = articleExists(a);
				    if(!exists)
				    {
					    writeToBaseX(a, source);
				    }
			    }
		    /*}
		    catch(SAXException | IOException | ParserConfigurationException e)
		    {
			    System.out.println("Errored on Source \"" + source.getName() + "\"");
			    System.out.println("link: \"" + source.getLink() + "\"");
			    System.out.println("RSS_link: \"" + source.getRss_link() + "\"");
			    System.out.println(doc);
			    e.printStackTrace();
		    }*/
		    try
		    {
			    Thread.sleep(sleeptime);
		    }
		    catch(InterruptedException e)
		    {
			    e.printStackTrace();
		    }
	    }
	    System.out.println("stopped crawler on \"" + source.getName() + "\"");
    }

	private void writeToBaseX(Article a, Source source)
	{
		BaseXController bsx  = BaseXController.getInstance();
		String          date = a.getDateFormatted();
		Add             add  = new Add("Artikel/" + source.getName() + "/" + date + "/" + a.getFileName(),
		                               a.toString());
		String          res1 = add.toString();
		String          res2 = bsx.execute(add);
		if(debug)
		{
			System.out.println(res1);
			System.out.println(res2);
		}
	}

	private boolean articleExists(Article a)
	{
		BaseXController bsx    = BaseXController.getInstance();
		String          xquery = "/article/title = \"" + a.getTitle() + "\"";
		String          result = bsx.query(xquery);
		if(debug)
		{
			System.out.println("exists \"" + a.getTitle() + "\"? --> " + result);
		}
		if(result.equals("true"))
		{
			return true;
		}
		return false;
	}

	/**
	 * Extracts Articles from an XML Document.
	 * An article begins with an <item> tag and should contain title, description, link, guid and pubDate
	 * But it must contain guid and a title
	 *
	 * @param feed: XMLDocument as Tag-Class in which to search for articles
	 */
	private ArrayList<Article> extractArticles(Tag feed)
	{
		Tag channel = feed.child("channel");
		if(channel != null)
		{
			ArrayList<Tag>     rss_articles = channel.children("item");
			ArrayList<Article> articles     = new ArrayList<>();
			for(Tag curr_article : rss_articles)
			{
				String title = curr_article.child("title").value();
				if(title != null)
				{
					title = title.replace("\"", "'");
				}
				String description = curr_article.child("description").value();
				if(description != null)
				{
					description = description.replace("\"", "'");
				}
				String link = curr_article.child("link").value();
				if(link != null)
				{
					link = link.replace("\"", "'");
				}
				String guid = curr_article.child("guid").value();
				if(guid != null)
				{
					guid = guid.replace("\"", "'");
				}
				Calendar pubDate = parseCalendar(curr_article.child("pubDate").value());
				if(!guid.equals("") && !title.equals(""))
				{
					Article a = new Article(title, source, link, description, guid, pubDate);
					articles.add(a);
				}
			}
			return articles;
		}
		else
		{
			System.out.println("NullPointerException on source:" + source.getName());
			return null;
		}
	}

	public void stop()
	{
		System.out.println("stopping crawler on \"" + source.getName() + "\"");
		running = false;
	}

	/**
	 * @param pubDate date to parse in format "ddd, DD MMM YYY HH:MM:SS zhhmm"
	 *                d = Day in Letters (Thu)
	 *                D = Day in decimals (23)
	 *                M = Month in Letters (JUN)
	 *                Y = Year in decimals (2008)
	 *                H = hour in decimals (20)
	 *                M = minute in decimals (34)
	 *                S = seconds in decimals (20)
	 *                h = offset hours
	 *                m = offset minutes
	 *                z = +|-
	 *                zhhmm = +0100
	 * @return Calendar representing given date
	 */
	private Calendar parseCalendar(String pubDate) throws NumberFormatException
	{
		pubDate = pubDate.substring(pubDate.indexOf(",") + 2);
		String[] fields     = pubDate.split(" ");
		int      dayInMonth = Integer.parseInt(fields[0]);
		int      month      = parseMonth(fields[1]);
		int      year       = Integer.parseInt(fields[2]);
		String[] time       = fields[3].split(":");
		int      hour       = Integer.parseInt(time[0]);
		int      min        = Integer.parseInt(time[1]);
		int      sec        = Integer.parseInt(time[2]);
		Calendar c          = Calendar.getInstance();
		c.set(year, month, dayInMonth, hour, min, sec);

		if(fields[4].length() == 5)
		{
			int offsetHour   = Integer.parseInt(fields[4].substring(1, 3));
			int offsetMin    = Integer.parseInt(fields[4].substring(3, 5));
			int offsetMillis = (60 * offsetHour + offsetMin) * 60 * 1000;
			if(fields[4].charAt(0) == '+')
			{
				c.set(Calendar.ZONE_OFFSET, offsetMillis);
			}
			else if(fields[4].charAt(0) == '-')
			{
				c.set(Calendar.ZONE_OFFSET, -offsetMillis);
			}
		}
		else if(fields[4].length() == 3)
		{
			c.setTimeZone(TimeZone.getTimeZone(fields[4]));
		}
		return c;
	}

	private int parseMonth(String month)
	{
		switch(month)
		{
			case "Jan":
				return 0;
			case "Feb":
				return 1;
			case "Mar":
				return 2;
			case "Apr":
				return 3;
			case "May":
				return 4;
			case "Jun":
				return 5;
			case "Jul":
				return 6;
			case "Aug":
				return 7;
			case "Sep":
				return 8;
			case "Oct":
				return 9;
			case "Nov":
				return 10;
			case "Dec":
				return 11;
			default:
				return -1;
		}
	}

	/**
	 * @param url the url from which to get an document
	 * @return the extracted document as a String
	 */
	private String getHTTPResponse(String url){
	    try{
		    URL            doc_url = new URL(url);
		    InputStream    is      = doc_url.openStream();
		    BufferedReader rd      = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder  sb      = new StringBuilder();
		    int            cp;
		    while((cp = rd.read()) != -1)
		    {
			    sb.append((char)cp);
		    }
		    return sb.toString();
	    }catch(IOException e){
		    e.printStackTrace();
	    }
	    return "";
    }

	public void setDebug(boolean newVal)
	{
		debug = newVal;
	}

	public void setSleeptime(int sleeptime)
	{
		this.sleeptime = sleeptime;
	}

	public Source getSource()
	{
		return source;
	}
}
