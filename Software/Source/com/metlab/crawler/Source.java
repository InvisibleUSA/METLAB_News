package com.metlab.crawler;

public class Source
{
	private final String name;
	private final String link;
	private final String rss_link;

	public Source(String name, String link, String rss_link)
	{
		this.name = name;
		this.link = link;
		this.rss_link = rss_link;
	}

	public String getName()
	{
		if(name != null)
		{
			return name;
		}
		return "";
	}

	public String getLink()
	{
		if(link != null)
		{
			return link;
		}
		return "";
	}

	public String getRss_link()
	{
		if(rss_link != null)
		{
			return rss_link;
		}
		return "";
	}
}