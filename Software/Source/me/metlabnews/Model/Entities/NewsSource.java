package me.metlabnews.Model.Entities;

import com.sun.istack.Nullable;

import javax.validation.constraints.NotNull;



/**
 * <p>A class to represent a source to crawl</p>
 * <p>This class has a name, a weblink and an link to an rss feed</p>
 */
public class NewsSource
{
	@NotNull
	private final String name;
	@Nullable
	private final String link;
	@Nullable
	private final String rss_link;

	/**
	 * Creates a new NewsSource with the given name, link and rss_link
	 *
	 * @param name     the name of this source
	 * @param link     the link to start a webcrawler on, can be null
	 * @param rss_link the link to an rss feed, can be null
	 */
	public NewsSource(String name, String link, String rss_link)
	{
		this.name = name;
		this.link = link;
		this.rss_link = rss_link;
	}

	/**
	 * @return the name of the source
	 */
	public String getName()
	{
		if(name != null)
		{
			return name;
		}
		return "";
	}

	/**
	 * @return the link to the start website
	 */
	public String getLink()
	{
		if(link != null)
		{
			return link;
		}
		return "";
	}

	/**
	 * @return the link to this sources rss feed
	 */
	public String getRss_link()
	{
		if(rss_link != null)
		{
			return rss_link;
		}
		return "";
	}
}
