package me.metlabnews.Model.Entities;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * <p>Java representation of an RssItem (article)</p>
 * <p>
 * Fields are:
 * <ul>
 * <li>Title</li>
 * <li>Link</li>
 * <li>Description</li>
 * <li>GUID</li>
 * <li>pubDate</li>
 * <li>Source</li>
 * </ul>
 * </p>
 *
 * @author Benjamin Gerlach
 */
public class Article
{
	static {
		Logger.getInstance().register(Article.class, Logger.Channel.Entities);
	}

	private String     m_title;
	private String     m_link;
	private String     m_description;
	private NewsSource m_source;
	private String     m_guid;
	private Calendar   m_pubDate;
	private boolean    m_isRSS = true;

	public Article(String title, NewsSource source, String link, String description, String guid, Calendar pubDate) {
		this.m_title = format(removeAllTags(title));
		this.m_source = source;
		this.m_link = format(link);
		this.m_description = format(description);
		this.m_guid = format(guid);
		this.m_pubDate = pubDate;
	}

	/**
	 * Constructs an Article from an {@Link XMLTag}
	 *
	 * @param tag XML representation of this article
	 * @throws IllegalArgumentException if the XMLTag is not a representation of an article
	 */
	public Article(XMLTag tag) throws IllegalArgumentException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		try {
			m_title = tag.child("title").value();
			m_link = tag.child("link").value();
			m_description = tag.child("description").value();
			m_guid = tag.child("guid").value();
			m_source = new NewsSource(tag.child("source").value(), "", "");
			m_pubDate = Calendar.getInstance();
			m_pubDate.setTime(sdf.parse(tag.child("pubDate").value()));
		}
		catch(NullPointerException e) {
			Logger.getInstance().logError(this, "Not a valid article. " + e.toString());
			throw new IllegalArgumentException("Parameter tag does not accurately represent a article.");
		}
		catch(ParseException e) {
			Logger.getInstance().logError(this, "Invalid Date format: " + e.toString());
		}
	}

	/**
	 * Removes all XML-Tags
	 *
	 * @param s String from which to remove the XML-Tags
	 * @return the modified string
	 */
	private String removeAllTags(String s) {
		return s.replaceAll("<.*>", "");
	}

	private String format(String s) {
		s = s.replace("&", "und");
		s = s.trim();
		s = s.replace("\"", "'");
		return s;
	}

	public String toString() {
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

	public NewsSource getSource() {
		return m_source;
	}

	public Calendar getPubDate() {
		return m_pubDate;
	}

	public String getguid() {
		return m_guid;
	}

	public String getTitle() {
		return m_title;
	}

	public void setGuid(String guid) {
		this.m_guid = guid;
	}

	/**
	 * check if article is from RSS feed (true)
	 * or from YaCy search result (false)
	 */
	public boolean isRSS() {
		return m_isRSS;
	}

	/**
	 * set whether article is from RSS feed (true)
	 * or from YaCy search result (false)
	 */
	public void setisRSS(boolean m_isRSS) {
		this.m_isRSS = m_isRSS;
	}
}
