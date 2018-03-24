package com.metlab.crawler;

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



public class Crawler implements Runnable{

	private boolean           debug   = false;
	private boolean           running = true;
	private Source source;

	private int sleeptime = 1000;

	public Crawler(Source source)
	{
		this.source = source;
	}

	public Crawler(int sleeptime, Source source)
	{
		this.sleeptime = sleeptime;
		this.source = source;
	}
    @Override
    public void run(){
	    while(running)
	    {
		    if(debug)
		    {
			    System.out.println("crawling " + source.getName() + " --> " + source.getLink());
		    }
		    String                 doc       = getHTTPResponse(source.getLink());
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    try
		    {
			    DocumentBuilder    dBuilder = dbFactory.newDocumentBuilder();
			    Document           feed     = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
			    ArrayList<Article> articles = extractArticles(feed);
			    for(Article a : articles)
			    {
				    boolean exists = articleExists(a);
				    if(!exists)
				    {
					    writeToBaseX(a, source);
				    }
			    }
		    }
		    catch(SAXException | IOException | ParserConfigurationException e)
		    {
			    e.printStackTrace();
		    }
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
	 * @param top: XMLDocument in which to search for articles
	 */
	private ArrayList<Article> extractArticles(Document top)
	{
		ArrayList<Node>    rss_articles = searchForArticle(top.getFirstChild());
		ArrayList<Article> articles     = new ArrayList<>();
		for(Node curr_article : rss_articles)
		{
			String   title       = "";
			String   description = "";
			String   link        = "";
			String   guid        = "";
			Calendar pubDate     = Calendar.getInstance();
			NodeList args        = curr_article.getChildNodes();
			for(int j = 0; j < args.getLength(); j++)
			{
				Node curr_field = args.item(j);
				if(curr_field.getNodeType() == Node.ELEMENT_NODE)
				{
					switch(curr_field.getNodeName())
					{
						case "title":
							title = getValueFromNode(curr_field).replace("\"", "'");
							break;
						case "description":
							description = getValueFromNode(curr_field).replace("\"", "'");
							break;
						case "link":
							link = getValueFromNode(curr_field).replace("\"", "'");
							break;
						case "guid":
							guid = getValueFromNode(curr_field).replace("\"", "'");
							break;
						case "pubDate":
							pubDate = parseCalendar(getValueFromNode(curr_field));
							break;
					}
				}
			}
			if(!guid.equals("") && !title.equals(""))
			{
				Article a = new Article(title, source, link, description, guid, pubDate);
				articles.add(a);
			}
		}
		return articles;
	}

	public void stop()
	{
		running = false;
		Thread.currentThread().interrupt();
	}

	/**
	 * @param node the node to get the value from
	 * @return text-value from the node
	 */
	private String getValueFromNode(Node node)
	{
		if(node.getNodeValue() != null)
		{
			return node.getNodeValue();
		}
		else if(node.hasChildNodes())
		{
			if(node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE || node.getChildNodes().item(
					0).getNodeType() == Node.CDATA_SECTION_NODE)
			{
				return node.getChildNodes().item(0).getNodeValue();
			}
		}
		return "";
	}

	/**
	 * @param node the node to check for the tag-name "item"
	 * @return list of notes that match "item"
	 */
	private ArrayList<Node> searchForArticle(Node node)
	{
		ArrayList<Node> articles = new ArrayList<>();
		if(node.hasChildNodes())
		{
			for(int i = 0; i < node.getChildNodes().getLength(); i++)
			{
				Node currNode = node.getChildNodes().item(i);
				if(currNode.getNodeName().equals("item"))
				{
					articles.add(currNode);
				}
				else
				{
					articles.addAll(searchForArticle(currNode));
				}
			}
		}
		return articles;
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
	 * prints out an XML document. Isn't working perfectly and only for debugging purposes
	 *
	 * @param last  top node which will be displayed
	 * @param level always call with 0, used to get the right tab-count
	 */
	private void XMLToString(Node last, int level){
		System.out.println("");
		NodeList nodes = last.getChildNodes();
		for(int i = 0; i< nodes.getLength(); i++){
			Node curr = nodes.item(i);
			for(int l = 0; l < level; l++)
			{
				System.out.print("\t");
			}
			if(!curr.getNodeName().equals(
					"#text") && !curr.getNodeName().equals("#cdata-section") && !curr.getNodeName().equals("#comment")){
				System.out.print("<" + curr.getNodeName());
				if(curr.getAttributes() != null)
				{
					if(curr.getAttributes().getLength() > 0)
					{
						for(int j = 0; j < curr.getAttributes().getLength(); j++)
						{
							System.out.print(" " + curr.getAttributes().item(j).getNodeName() + "=\"" + curr.getAttributes().item(
									j).getNodeValue() + "\"");
						}
					}
				}
				System.out.print(">");
			}
			if(curr.hasChildNodes()){
				XMLToString(curr, level+1);
			}else{
				if(curr.getNodeValue() != null)
				{
					if(!curr.getNodeValue().equals(""))
					{
						System.out.print(curr.getNodeValue());
					}
				}
			}
		}
		System.out.println("");
		for(int i = 0; i<level-1; i++){ System.out.print("\t");}
		System.out.print("</" + last.getNodeName() + ">");
	}

	/**
	 * @param url the url from which to get an document
	 * @return the extracted document as a String
	 */
	private String getHTTPResponse(String url){
	    try{

		    InputStream is = new URL(url).openStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder sb = new StringBuilder();
		    int cp;
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
}
