package me.metlabnews.Model.Entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Article
{
	private String   m_title;
	private String   m_link;
	private String   m_description;
	private Source   m_source;
	private String   m_guid;
	private Calendar m_pubDate;

	public Article(String title, Source source, String link, String description, String guid, Calendar pubDate)
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
						+ "\t<m_title><![CDATA[" + m_title + "]]></m_title>\n"
						+ "\t<m_source><![CDATA[" + m_source.getName() + "]]></m_source>\n"
						+ "\t<m_link><![CDATA[" + m_link + "]]></m_link>\n"
						+ "\t<m_description><![CDATA[" + m_description + "]]></m_description>\n"
						+ "\t<m_guid><![CDATA[" + m_guid + "]]></m_guid>\n"
						+ "\t<m_pubDate>" + sdf.format(m_pubDate.getTime()) + "</m_pubDate>\n"
						+ "</article>";
		return erg;
	}

	public Source getSource()
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

	public String getFileName()
	{
		final String f = m_title
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
		return m_pubDate.get(Calendar.DAY_OF_MONTH) + "." + (m_pubDate.get(Calendar.MONTH) + 1) + "." + m_pubDate.get(
				Calendar.YEAR);
	}

	public String getTitle()
	{
		return m_title;
	}
}
