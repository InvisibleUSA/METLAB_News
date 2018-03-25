package com.metlab.crawler;

import com.metlab.controller.BaseXController;

import static java.lang.System.exit;

public class Main
{
	public static void main(String... args) throws InterruptedException
    {
        BaseXController bxc = BaseXController.getInstance();

	    CrawlerController cc = CrawlerController.getInstance();

	    cc.setDebug(true);
	    cc.setSleeptime(2 * 60 * 1000);
	    cc.start();

	    Thread.sleep(100000);


	    cc.stop();
	    bxc.stop();
	    exit(0);
    }
}
