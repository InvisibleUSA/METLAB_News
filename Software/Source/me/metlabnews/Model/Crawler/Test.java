package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.YaCy.QuerySearchArticle;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.NewsSource;

import java.util.ArrayList;



public class Test
{
	public static void main(String[] args)
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
		CrawlerController.initialize();
		//CrawlerController.getInstance().start();
		/*
		QuerySearchArticle qsa = new QuerySearchArticle("avengers", "infinity", "war");
		boolean res = qsa.execute();
		System.out.println(res);
		ArrayList<Article> articles = qsa.getArticles();
		for(int i = 0; i<articles.size(); i++)
		{
			System.out.println(articles.get(i).toString());
		}*/
		QuerySearchArticle qsa = new QuerySearchArticle(new NewsSource("Gamestar", "www.gamestar.de", ""), "avengers",
		                                                "infinity", "war");
		qsa.execute();
		ArrayList<Article> articles = qsa.getArticles();
		for(Article a : articles) {
			System.out.println(a.toString());
		}
	}
}
