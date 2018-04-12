package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Entities.Source;



public class Test
{
	public static void main(String[] args)
	{
		new WebCrawler(new Source("test", "http://spiegel.de/", "")).start();
	}
}
