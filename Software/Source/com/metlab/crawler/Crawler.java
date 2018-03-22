package com.metlab.crawler;

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



public class Crawler implements Runnable{
	String[] rssSourceLinks = {
    		"http://www.spiegel.de/schlagzeilen/tops/index.rss"
    };

    public Crawler(){

    }

    @Override
    public void run(){
	    for(String link : rssSourceLinks)
	    {
        	String doc = getHTTPResponse(link);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        try
	        {
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document feed = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
		        //XMLToString(feed.getChildNodes().item(0), 0);
		        ArrayList<Article> articles = ExtractArticles(feed);
		        for(Article a : articles)
		        {
			        System.out.println(a.title);
		        }
	        }
	        catch(SAXException | IOException | ParserConfigurationException e)
	        {
		        e.printStackTrace();
	        }
        }
    }

	/**
	 * Extracts Articles from an XML Document.
	 * An article begins with an <item> tag and should contain title, description, link
	 *
	 * @param top: XMLDocument in which to search for articles
	 */
	private ArrayList<Article> ExtractArticles(Document top)
	{
		ArrayList<Node>    rss_articles = searchForArticle(top.getFirstChild());
		ArrayList<Article> articles     = new ArrayList<Article>();
		for(int i = 0; i < rss_articles.size(); i++)
		{
			Node     curr_article = rss_articles.get(i);
		    String   title = "";
		    String   description = "";
		    String   link = "";
		    String   guid = "";
		    Calendar pubDate = Calendar.getInstance();
		    NodeList args = curr_article.getChildNodes();
		    for(int j = 0; j<args.getLength(); j++){
		    	Node curr_field = args.item(j);
		    	if(curr_field.getNodeType() == Node.ELEMENT_NODE){
		    		switch(curr_field.getNodeName()){
					    case "title":
						    title = getValueFromNode(curr_field);
					        break;
					    case "description":
						    description = getValueFromNode(curr_field);
					    	break;
					    case "link":
						    link = getValueFromNode(curr_field);
					    	break;
					    case "guid":
						    guid = getValueFromNode(curr_field);
					        break;
					    case "pubDate":
						    pubDate = parseCalendar(getValueFromNode(curr_field));
					    	break;
				    }
			    }
		    }
		    Article a = new Article(title, link, description, guid, pubDate);
			articles.add(a);
		}
		return articles;
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
			if(node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE)
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
		ArrayList<Node> articles = new ArrayList<Node>();
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

	private Calendar parseCalendar(String pubDate)
	{
		return Calendar.getInstance();
	}

	private void XMLToString(Node last, int level){
	    System.out.println("");
    	NodeList nodes = last.getChildNodes();
    	for(int i = 0; i< nodes.getLength(); i++){
    		Node curr = nodes.item(i);
		    for(int l = 0; l<level; l++){System.out.print("\t");}
		    if(!curr.getNodeName().equals("#text") && !curr.getNodeName().equals("#cdata-section") && !curr.equals("#comment")){
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
		    String response = sb.toString();
		    return response;
	    }catch(IOException e){
		    e.printStackTrace();
	    }
	    return "";
    }
}
