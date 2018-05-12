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
		CrawlerController.getInstance().start();

	}
}
