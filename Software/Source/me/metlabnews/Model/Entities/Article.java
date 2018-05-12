package me.metlabnews.Model.Entities;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Article
{
	static
	{
		Logger.getInstance().register(Article.class, Logger.Channel.Entities);
	}

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

	public Article(XMLTag tag) throws IllegalArgumentException
	{
		SimpleDateFormat sdf      = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		try
		{
			m_title = tag.child("title").value();
			m_link = tag.child("link").value();
			m_description = tag.child("description").value();
			m_guid = tag.child("guid").value();
			m_source = new NewsSource(tag.child("source").value(), "", "");
			m_pubDate = Calendar.getInstance();
			m_pubDate.setTime(sdf.parse(tag.child("pubDate").value()));
		}
		catch(NullPointerException e)
		{
			Logger.getInstance().logError(this, "Not a valid article. " + e.toString());
			throw new IllegalArgumentException("Parameter tag does not accurately represent a article.");
		}
		catch(ParseException e)
		{
			Logger.getInstance().logError(this, "Invalid Date format: " + e.toString());
		}
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

	public void setGuid(String guid) {
		this.m_guid = guid;
	}
}
