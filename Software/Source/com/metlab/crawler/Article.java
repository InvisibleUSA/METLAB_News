package com.metlab.crawler;

import java.util.Calendar;



public class Article
{
	String   title;
	String   link;
	String   description;
	String   guid;
	Calendar pubDate;

	public Article(String title, String link, String description, String guid, Calendar pubDate)
	{
		this.title = title;
		this.link = link;
		this.description = description;
		this.guid = guid;
		this.pubDate = pubDate;
	}
}