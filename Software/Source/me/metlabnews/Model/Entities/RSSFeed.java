package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Crawler.CalendarParser;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;



/**
 * @author Benjamin Gerlach
 */
public class RSSFeed
{
	static
	{
		Logger.getInstance().register(RSSFeed.class, Logger.Channel.Entities);
	}
	private NewsSource         source;
	private ArrayList<Article> articles = new ArrayList<>();

	public RSSFeed() {

	}

	public RSSFeed(NewsSource s, Article... articles) {
		source = s;
		this.articles.addAll(Arrays.asList(articles));
	}

	public RSSFeed(NewsSource s, ArrayList<Article> articles) {
		source = s;
		this.articles = articles;
	}

	/**
	 * @param feed   the RSSFeed as a String
	 * @param source the origin of the RSSFeed
	 * @return the parsed {@link RSSFeed}
	 */
	public static RSSFeed parseFeed(String feed, NewsSource source) {
		XMLTag RSSFeedTag = new XMLTag(feed);
		XMLTag channel    = RSSFeedTag.child("channel");
		if(channel != null) {
			ArrayList<XMLTag>  rss_articles = channel.children("item");
			ArrayList<Article> articles     = new ArrayList<>();
			for(XMLTag curr_article : rss_articles) {
				String title = curr_article.child("title").value();
				if(title != null) {
					title = title.replace("\"", "'");
				}
				else {
					title = "";
				}
				String description;
				XMLTag descriptionChild = curr_article.child("description");
				if(descriptionChild != null)
				{
					description = descriptionChild.value().replace("\"", "'");
				}
				else {
					description = "";
				}
				String link;
				XMLTag linkChild = curr_article.child("link");
				if(linkChild != null)
				{
					link = linkChild.value().replace("\"", "'");
				}
				else {
					link = "";
				}
				String guid;
				XMLTag guidChild = curr_article.child("guid");
				if(guidChild != null)
				{
					guid = guidChild.value().replace("\"", "'");
				}
				else {
					guid = "";
				}
				Calendar pubDate;
				try {
					pubDate = CalendarParser.parseCalendar(curr_article.child("pubDate").value());
				}
				catch(NumberFormatException e) {
					pubDate = Calendar.getInstance();
				}
				if(!guid.equals("") && !title.equals("")) {
					Article a = new Article(title, source, link, description, guid, pubDate);
					articles.add(a);
				}
			}
			return new RSSFeed(source, articles);
		}
		else {
			Logger.getInstance().logError(RSSFeed.class, "NullPointerException on source:" + source.getName());
			return null;
		}
	}

	public void setSource(NewsSource s) {
		this.source = s;
	}

	public void addArticle(Article a) {
		this.articles.add(a);
	}

	public void setArticles(Collection<Article> articles) {
		this.articles.clear();
		this.articles.addAll(articles);
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public NewsSource getSource() {
		return source;
	}
}
