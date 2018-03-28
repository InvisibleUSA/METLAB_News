package com.metlab.crawler;

import com.metlab.controller.BaseXController;

import static java.lang.System.exit;

public class Main
{
	public static void main(String... args)
    {
	    CrawlerController cc = CrawlerController.getInstance();

	    cc.setDebug(false);
	    cc.setSleeptime(2 * 10 * 1000);
	    cc.start();
    }
}
