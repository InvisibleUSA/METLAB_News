package me.metlabnews.Model.Entities;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Article
{

	private String     m_title;
	private String     m_link;
	private String     m_description;
	private NewsSource m_source;
	private String     m_guid;
	private Calendar   m_pubDate;

	public Article(String title, NewsSource source, String link, String description, String guid, Calendar pubDate)
	{
		this.m_title = format(removeAllTags(title));
		this.m_source = source;
		this.m_link = format(link);
		this.m_description = format(description);
		this.m_guid = format(guid);
		this.m_pubDate = pubDate;
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
						+ "\t<title><![CDATA[" + m_title + "]]></title>\n"
						+ "\t<source><![CDATA[" + m_source.getName() + "]]></source>\n"
						+ "\t<link><![CDATA[" + m_link + "]]></link>\n"
						+ "\t<description><![CDATA[" + m_description + "]]></description>\n"
						+ "\t<guid><![CDATA[" + m_guid + "]]></guid>\n"
						+ "\t<pubDate>" + sdf.format(m_pubDate.getTime()) + "</pubDate>\n"
						+ "</article>";
		return erg;
	}

	public NewsSource getSource()
	{
		return m_source;
	}

	public Calendar getPubDate()
	{
		return m_pubDate;
	}

	public String getguid()
	{
		return m_guid;
	}

	public String getTitle()
	{
		return m_title;
	}
}
