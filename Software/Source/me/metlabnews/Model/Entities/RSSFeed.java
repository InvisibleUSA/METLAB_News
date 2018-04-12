package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.CalendarParser;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;



public class RSSFeed
{
	Source             source;
	ArrayList<Article> articles;

	public RSSFeed()
	{
		articles = new ArrayList<>();
	}

	public RSSFeed(Source s, Article... articles)
	{
		source = s;
		for(Article a : articles)
		{
			this.articles.add(a);
		}
	}

	public RSSFeed(Source s, ArrayList<Article> articles)
	{
		source = s;
		this.articles = articles;
	}

	public static RSSFeed parseFeed(String feed, Source source)
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
				String description = curr_article.child("description").value();
				if(description != null)
				{
					description = description.replace("\"", "'");
				}
				String link = curr_article.child("link").value();
				if(link != null)
				{
					link = link.replace("\"", "'");
				}
				String guid = curr_article.child("guid").value();
				if(guid != null)
				{
					guid = guid.replace("\"", "'");
				}
				Calendar pubDate = CalendarParser.parseCalendar(curr_article.child("pubDate").value());
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
			Logger.getInstance().log(Logger.Channel.Crawler, Logger.LogPriority.ERROR, Logger.LogType.ToFile,
			                         "NullPointerException on source:" + source.getName());
			return null;
		}
	}

	public void setSource(Source s)
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

	public Source getSource()
	{
		return source;
	}
}
