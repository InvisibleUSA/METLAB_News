package com.metlab.crawler;

import com.metlab.controller.BaseXController;

import static java.lang.System.exit;

public class Main
{
	public static void main(String... args) throws InterruptedException
    {
        BaseXController bxc = BaseXController.getInstance();

	    CrawlerController cc = CrawlerController.getInstance();
	    cc.addSource(new Source("Spiegel", "http://www.spiegel.de/schlagzeilen/tops/index.rss"));
	    cc.addSource(new Source("Süddeutsche", "http://rss.sueddeutsche.de/app/service/rss/alles/index.rss"));
	    cc.addSource(new Source("Zeit", "http://newsfeed.zeit.de/index"));
	    cc.addSource(new Source("Stuttgarter Zeitung", "https://www.stuttgarter-zeitung.de/news.rss.feed"));
	    cc.addSource(new Source("MAZ", "http://www.maz-online.de/rss/feed/maz_brandenburg"));
	    cc.addSource(new Source("Gamestar", "http://www.gamestar.de/news/rss/news.rss"));
	    cc.addSource(new Source("Kino.de", "https://www.kino.de/rss/neu-im-kino"));
	    cc.addSource(new Source("Sumikai", "https://sumikai.com/feed/"));
	    cc.addSource(new Source("Netzpolitik.org", "https://netzpolitik.org/feed"));
	    cc.addSource(new Source("Nachdenkseiten", "https://www.nachdenkseiten.de/?feed=rss2"));

	    cc.setDebug(true);
	    cc.setSleeptime(2 * 60 * 1000);
	    cc.start();

	    Thread.sleep(100000);


	    cc.stop();
	    bxc.stop();
	    exit(0);
    }
}
