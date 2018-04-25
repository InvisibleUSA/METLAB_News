package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;

public class Test
{
	public static void main(String[] args)
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
		CrawlerController.getInstance().start();
	}
}
