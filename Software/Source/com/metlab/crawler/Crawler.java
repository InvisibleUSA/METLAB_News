package com.metlab.crawler;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Calendar;



public class Crawler implements Runnable{
    String[] rssSourceLinke = {
    		"http://www.spiegel.de/schlagzeilen/tops/index.rss"
    };

    public Crawler(){

    }

    @Override
    public void run(){
        for(String link: rssSourceLinke){
        	String doc = getHTTPResponse(link);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        try
	        {
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document feed = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
		        XMLToString(feed.getChildNodes().item(0), 0);
		        ExtractArticles(feed);
	        }
	        catch(SAXException e)
	        {
		        e.printStackTrace();
	        }
	        catch(IOException e)
	        {
		        e.printStackTrace();
	        }
	        catch(ParserConfigurationException e)
	        {
		        e.printStackTrace();
	        }
        }
    }

    private void ExtractArticles(Document top){
    	NodeList articles = top.getElementsByTagNameNS("*", "*item*");
    	for(int i = 0; i< articles.getLength(); i++){
		    Node     curr_article = articles.item(i);
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
					    case "title" :
					    	title = curr_field.getNodeValue();
					        break;
					    case "description":
					    	description = curr_field.getNodeValue();
					    	break;
					    case "link":
					    	link = curr_field.getNodeValue();
					    	break;
					    case "guid":
					    	guid = curr_field.getNodeValue();
					        break;
					    case "pubDate":
					    	pubDate = parseCalendar(curr_field.getNodeValue());
					    	break;
				    }
			    }
		    }
		    Article a = new Article(title, link, description, guid, pubDate);
	    }
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
		System.out.print("new BaseXServerController();</" + last.getNodeName() + ">");
    }

    private String getHTTPResponse(String url){
	    try{

		    InputStream is = new URL(url).openStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    String response = readAll(rd);

		    return response;
	    }catch(IOException e){
		    e.printStackTrace();
	    }
	    return "";
    }

	private String readAll(Reader rd) throws IOException{
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1){
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
