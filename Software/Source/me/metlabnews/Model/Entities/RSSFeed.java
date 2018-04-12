package me.metlabnews.Model.Entities;

import java.util.ArrayList;
import java.util.Collection;



public class RSSFeed
{
	Source             src;
	ArrayList<Article> articles;

	RSSFeed()
	{
		articles = new ArrayList<>();
	}

	RSSFeed(Source s, Article... articles)
	{
		src = s;
		for(Article a : articles)
		{
			this.articles.add(a);
		}
	}

	public void setSource(Source s)
	{
		this.src = s;
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
		return src;
	}
}
