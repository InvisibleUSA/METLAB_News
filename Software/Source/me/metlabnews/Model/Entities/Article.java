package me.metlabnews.Model.Entities;

import com.sun.istack.Nullable;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * Java representation of an RssItem (article)
 * Fields are:
 * <ul>
 * <li>Title</li>
 * <li>Link</li>
 * <li>Description</li>
 * <li>GUID</li>
 * <li>pubDate</li>
 * <li>Source</li>
 * </ul>
 *
 * @author Benjamin Gerlach
 */
public class Article
{
	static
	{
		Logger.getInstance().register(Article.class, Logger.Channel.Entities);
	}

	@Nullable
	private String     m_title;
	@Nullable
	private String     m_link;
	@Nullable
	private String     m_description;
	@NotNull
	private NewsSource m_source;
	@Nullable
	private String     m_guid;
	@Nullable
	private Calendar   m_pubDate;
	private boolean    m_isRSS = true;

	/**
	 * constructs an complete {@link Article} with the given arguments, each one, but the source can be null
	 *
	 * @param title       the given title of the article, gets formatted through {{@link #format(String)}}
	 *                    and all xml tags removed through {{@link #removeAllTags(String)}.
	 * @param source      the given NewsSource, mustn't be null
	 * @param link        the given link to the original article
	 * @param description the articles description
	 * @param guid        the articles global unique identifier
	 * @param pubDate     the articles publish date
	 */
	public Article(String title, NewsSource source, String link, String description, String guid, Calendar pubDate)
	{
		this.m_title = format(removeAllTags(title));
		this.m_source = source;
		this.m_link = format(link);
		this.m_description = format(description);
		this.m_guid = format(guid);
		this.m_pubDate = pubDate;
	}

	/**
	 * Constructs an Article from an {@link XMLTag}
	 *
	 * @param tag XML representation of this article
	 * @throws IllegalArgumentException if the XMLTag is not a representation of an article
	 */
	public Article(XMLTag tag) throws IllegalArgumentException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		try
		{
			m_title = tag.child("title").value();
			m_link = tag.child("link").value();
			m_description = tag.child("description").value();
			m_guid = tag.child("guid").value();
			m_source = new NewsSource(tag.child("source").value(), "", "");
			m_pubDate = Calendar.getInstance();
			m_pubDate.setTime(sdf.parse(tag.child("pubDate").value()));
			Logger.getInstance().logDebug(this, "CREATED XML ARTICLE");
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

	/**
	 * Removes all XML-Tags
	 *
	 * @param s String from which to remove the XML-Tags
	 * @return the modified string
	 */
	private String removeAllTags(String s)
	{
		return s.replaceAll("<.*>", "");
	}

	/**
	 * formats a string: replacing all '&' with 'und' and replaces "\"" with "'"
	 * @param s the String to format
	 * @return the formatted string
	 */
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
						+ "\t<isRSS>" + m_isRSS + "</isRSS>\n"
						+ "\t<pubDate>" + sdf.format(m_pubDate.getTime()) + "</pubDate>\n"
						+ "</article>";
		return erg;
	}


	/**
	 * This method returns a HTML String
	 *
	 * @return a HTML String
	 */
	public String toHTMLString()
	{
		@SuppressWarnings("UnnecessaryLocalVariable")
		final String erg =
				"<h2><p>" + m_title + "</p><br></h2>"
						+ "<p>" + m_description + "</p>"
						+ "<p>Link: " + m_link + "</p><br>"
						+ "<p>Quelle: " + m_source.getName() + "</p><br>";
		return erg;
	}

	/**
	 * @return the articles source
	 */
	public NewsSource getSource()
	{
		return m_source;
	}

	/**
	 * @return the articles publishing date
	 */
	public Calendar getPubDate()
	{
		return m_pubDate;
	}

	/**
	 * @return the articles global unique identfier
	 */
	public String getguid()
	{
		return m_guid;
	}

	/**
	 * @return the articles title
	 */
	public String getTitle()
	{
		return m_title;
	}

	/**
	 * sets the guid of the article
	 * @param guid should be globally unique
	 */
	public void setGuid(String guid)
	{
		this.m_guid = guid;
	}

	/**
	 * check if article is from RSS feed (true)
	 * or from YaCy search result (false)
	 */
	public boolean isRSS()
	{
		return m_isRSS;
	}

	/**
	 * set whether article is from RSS feed (true)
	 * or from YaCy search result (false)
	 */
	public void setisRSS(boolean m_isRSS)
	{
		this.m_isRSS = m_isRSS;
	}
}
