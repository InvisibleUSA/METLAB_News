package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.NewsSource;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;



public class WebCrawlerTest
{
	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
		CrawlerController.initialize();
	}

	@Test
	public void test() throws InterruptedException
	{
		init();
		WebCrawler test = new WebCrawler(new NewsSource("Gamestar", "gamestar.de", ""));
		test.start();
		assert test.isRunning();
		Thread.sleep(10000);
		test.stop();
		assert !test.isRunning();
	}

	@Test
	public void start()
	{
		init();
		WebCrawler test = new WebCrawler(new NewsSource("Gamestar", "gamestar.de", ""));
		test.start();
		assert test.isRunning();
	}

	@Test
	public void stop()
	{
		init();
		WebCrawler test = new WebCrawler(new NewsSource("Gamestar", "gamestar.de", ""));
		test.stop();
		assert !test.isRunning();
	}
}
