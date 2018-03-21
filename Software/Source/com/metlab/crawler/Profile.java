package com.metlab.crawler;

import java.util.ArrayList;
import java.util.List;



public class Profile
{
	private String profileName;
	private List<String> keywords    = new ArrayList<String>();
	private List<String> sourceLinks = new ArrayList<String>();

	public Profile(String name, ArrayList<String> keywords, ArrayList<String> sourceLinks)
	{
		profileName = name;
		this.sourceLinks = sourceLinks;
		this.keywords = keywords;
	}

	public Profile(String name)
	{
		profileName = name;
	}

	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}

	public void addSource(String sourceLink)
	{
		sourceLinks.add(sourceLink);
	}
}
