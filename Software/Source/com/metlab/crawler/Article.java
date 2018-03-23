package com.metlab.crawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Article
{
	private String   title;
	private String   link;
	private String   description;
	private String   guid;
	private Calendar pubDate;

	public Article(String title, String link, String description, String guid, Calendar pubDate)
	{
		this.title = removeAllTags(format(title));
		this.link = format(link);
		this.description = format(description);
		this.guid = format(guid);
		this.pubDate = pubDate;
	}

	private String removeAllTags(String s)
	{
		return s.replaceAll("<.*>", "");
	}

	private String format(String s)
	{
		s = s.replace("/>", ">");
		s = s.replace(">", "/>");
		s = s.replace("&", "und");
		s = s.replaceAll("</.*>", "");
		s = s.trim();
		s = s.replace("\"", "'");
		s = s.replace("“", "'");
		s = s.replace("„", "'");
		return s;
	}

	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		String erg =
				"<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n"
						+ "<article>\n"
						+ "\t<title>" + title + "</title>\n"
						+ "\t<link>" + link + "</link>\n"
						+ "\t<description>" + description + "</description>\n"
						+ "\t<guid>" + guid + "</guid>\n"
						+ "\t<pubDate>" + sdf.format(pubDate.getTime()) + "</pubDate>\n"
						+ "</article>";
		return erg;
	}

	public Calendar getPubDate()
	{
		return pubDate;
	}

	public String getguid()
	{
		return guid;
	}

	public String getFileName()
	{
		String f = title
				.replace(" ", "")
				.replace(":", "")
				.replace("/", "")
				.replace("+", "")
				.replace("\\", "")
				.replace("!", "")
				.replace("\"", "")
				.replace("$", "")
				.replace("%", "")
				.replace("&", "")
				.replace("?", "")
				.replace("*", "")
				.replace("<", "")
				.replace(">", "");
		return f;
	}

	public String getTitle()
	{
		return title;
	}
}
