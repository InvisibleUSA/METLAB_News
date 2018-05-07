package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.CalendarParser;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;



public class RSSFeed
{
	private NewsSource source;
	private ArrayList<Article> articles = new ArrayList<>();

	public RSSFeed()
	{

	}

	public RSSFeed(NewsSource s, Article... articles)
	{
		source = s;
		this.articles.addAll(Arrays.asList(articles));
	}

	public RSSFeed(NewsSource s, ArrayList<Article> articles)
	{
		source = s;
		this.articles = articles;
	}

	public static RSSFeed parseFeed(String feed, NewsSource source)
	{
		XMLTag RSSFeedTag = new XMLTag(feed);
		XMLTag channel    = RSSFeedTag.child("channel");
		if(channel != null)
		{
			ArrayList<XMLTag>  rss_articles = channel.children("item");
			ArrayList<Article> articles     = new ArrayList<>();
			for(XMLTag curr_article : rss_articles)
			{
				String title = curr_article.child("title").value();
				if(title != null)
				{
					title = title.replace("\"", "'");
				}
				else
				{
					title = "";
				}
				String description = curr_article.child("description").value();
				if(description != null)
				{
					description = description.replace("\"", "'");
				}
				else
				{
					description = "";
				}
				String link = curr_article.child("link").value();
				if(link != null)
				{
					link = link.replace("\"", "'");
				}
				else
				{
					link = "";
				}
				String guid = curr_article.child("guid").value();
				if(guid != null)
				{
					guid = guid.replace("\"", "'");
				}
				else
				{
					guid = "";
				}
				Calendar pubDate;
				try
				{
					pubDate = CalendarParser.parseCalendar(curr_article.child("pubDate").value());
				}
				catch(NumberFormatException e)
				{
					pubDate = Calendar.getInstance();
				}
				if(!guid.equals("") && !title.equals(""))
				{
					Article a = new Article(title, source, link, description, guid, pubDate);
					articles.add(a);
				}
			}
			return new RSSFeed(source, articles);
		}
		else
		{
			Logger.getInstance().logError(RSSFeed.class, "NullPointerException on source:" + source.getName());
			return null;
		}
	}

	public void setSource(NewsSource s)
	{
		this.source = s;
	}

	public void addArticle(Article a)
	{
		this.articles.add(a);
	}

	public void setArticles(Collection<Article> articles)
	{
		this.articles.clear();
		this.articles.addAll(articles);
	}

	public ArrayList<Article> getArticles()
	{
		return articles;
	}

	public NewsSource getSource()
	{
		return source;
	}
}
