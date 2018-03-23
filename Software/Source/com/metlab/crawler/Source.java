package com.metlab.crawler;

public class Source
{
	private final String name;
	private final String link;

	public Source(String name, String link)
	{
		this.name = name;
		this.link = link;
	}

	public String getName()
	{
		return name;
	}

	public String getLink()
	{
		return link;
	}
}
