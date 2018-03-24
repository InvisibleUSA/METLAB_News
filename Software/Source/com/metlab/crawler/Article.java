package com.metlab.crawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Article
{
	private String   title;
	private String   link;
	private String   description;
	private Source   source;
	private String   guid;
	private Calendar pubDate;

	public Article(String title, Source source, String link, String description, String guid, Calendar pubDate)
	{
		this.title = format(removeAllTags(title));
		this.source = source;
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
		s = s.replace("&", "und");
		s = s.trim();
		s = s.replace("\"", "'");
		return s;
	}

	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		@SuppressWarnings("UnnecessaryLocalVariable")
		final String erg =
				//"<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n" +
				"<article>\n"
						+ "\t<title><![CDATA[" + title + "]]></title>\n"
						+ "\t<source><![CDATA[" + source.getName() + "]]></source>\n"
						+ "\t<link><![CDATA[" + link + "]]></link>\n"
						+ "\t<description><![CDATA[" + description + "]]></description>\n"
						+ "\t<guid><![CDATA[" + guid + "]]></guid>\n"
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
		@SuppressWarnings("UnnecessaryLocalVariable")
		final String f = title
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
				.replace(">", "")
				.replace("„", "")
				.replace("“", "")
				.replace(".", "");
		return f;
	}

	public String getDateFormatted()
	{
		return pubDate.get(Calendar.DAY_OF_MONTH) + "." + (pubDate.get(Calendar.MONTH) + 1) + "." + pubDate.get(
				Calendar.YEAR);
	}

	public String getTitle()
	{
		return title;
	}
}
