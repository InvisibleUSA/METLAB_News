package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetSources;
import me.metlabnews.Model.Entities.NewsSource;

import java.util.ArrayList;
import java.util.List;



/**
 * <p>controls and manages all crawler instances</p>
 * <p>
 * it takes the sources from an sql database and starts a
 * crawler for each source. The crawler type depends on the preferred type
 * set in the settings.xml
 * </p>
 *
 * @author Benjamin Gerlach
 */
public class CrawlerController implements Runnable
{

	private static CrawlerController m_cc;
	private        List<RssCrawler>  m_RssCrawler = new ArrayList<>();
	private        List<WebCrawler>  m_Webcrawler = new ArrayList<>();

	private Thread  m_t;
	private boolean m_running = true;

	private CrawlerController() {

	}

	/**
	 * Singleton call
	 *
	 * @return the instance of {@Link CrawlerController}
	 */
	public static CrawlerController getInstance() {
		if(m_cc == null) {
			m_cc = new CrawlerController();
		}
		return m_cc;
	}

	/**
	 * registers CrawlerController.class, RssCrawler.class and WebCrawler.class to the Crawler Log Channel
	 */
	public static void initialize() {
		Logger.getInstance().register(CrawlerController.class, Logger.Channel.Crawler);
		Logger.getInstance().register(RssCrawler.class, Logger.Channel.Crawler);
		Logger.getInstance().register(WebCrawler.class, Logger.Channel.Crawler);
	}

	/**
	 * stops all running crawlers
	 */
	public void stop() {
		for(RssCrawler c : m_RssCrawler) {
			c.stop();
		}
		for(WebCrawler w : m_Webcrawler) {
			w.stop();
		}
		m_running = false;
	}

	public void start() {
		m_t = new Thread(this);
		m_t.start();
	}

	/**
	 * Checks sql-database for sources
	 * and starts a crawler for that source
	 * the type of crawler is determined by
	 * the existing links in the DB and by
	 * the setting "Crawler.TypePreferred"
	 */
	@Override
	public void run() {
		while(m_running) {
			QueryGetSources qgs = new QueryGetSources();
			qgs.execute();
			ArrayList<NewsSource> sources = qgs.getSources();
			//go through all sources
			for(NewsSource curr_src : sources) {
				Logger.getInstance().logDebug(this,
				                              curr_src.getName() + " link: " + curr_src.getLink() + " rss_link: " + curr_src.getRss_link());
				//if there is only a rss link --> RssCrawler
				//both --> check TypePreferred
				if((!curr_src.getRss_link().isEmpty() && curr_src.getLink().isEmpty())
						|| (!curr_src.getRss_link().isEmpty() && curr_src.getLink().isEmpty() && ConfigurationManager.getInstance().getCrawlerTypePreferred() == ConfigurationManager.TypePreferred.RSSFeed)
						) {
					RssCrawler curr_RSS_crawler = null;
					//look for a m_RssCrawler on that source
					for(RssCrawler c : m_RssCrawler) {
						if(curr_src.getName().equals(c.getSource().getName())
								&& curr_src.getLink().equals(c.getSource().getLink())
								&& curr_src.getRss_link().equals(c.getSource().getRss_link())) {
							curr_RSS_crawler = c;
						}
					}
					//if there is no m_RssCrawler for that source --> create one
					if(curr_RSS_crawler == null) {
						curr_RSS_crawler = new RssCrawler(curr_src);
						m_RssCrawler.add(curr_RSS_crawler);
						curr_RSS_crawler.start();
					}
				}
				else if((!curr_src.getLink().isEmpty() && curr_src.getRss_link().isEmpty())
						|| (!curr_src.getRss_link().isEmpty() && curr_src.getLink().isEmpty() && ConfigurationManager.getInstance().getCrawlerTypePreferred() == ConfigurationManager.TypePreferred.Website)
						) {
					boolean exists = false;
					for(WebCrawler wc : m_Webcrawler) {
						if(wc.getSource().getName().equals(curr_src.getName())) {
							exists = true;
							break;
						}
					}
					if(!exists) {
						WebCrawler wc = new WebCrawler(curr_src);
						m_Webcrawler.add(wc);
						wc.start();
					}
				}
			}
			//go through all crawlers and kill crawlers with dead sources
			ArrayList<RssCrawler> RSStoremove = new ArrayList<>();
			for(RssCrawler c : m_RssCrawler) {
				boolean hasValidSource = false;
				for(NewsSource curr_src : sources) {
					if(curr_src.getName().equals(c.getSource().getName())
							&& curr_src.getLink().equals(c.getSource().getLink())
							&& curr_src.getRss_link().equals(c.getSource().getRss_link())) {
						hasValidSource = true;
					}
				}
				if(!hasValidSource) {
					RSStoremove.add(c);
					c.stop();
				}
			}
			m_RssCrawler.removeAll(RSStoremove);

			ArrayList<WebCrawler> Webtoremove = new ArrayList<>();
			for(WebCrawler wc : m_Webcrawler) {
				boolean hasValidSource = false;
				for(NewsSource curr_src : sources) {
					if(curr_src.getName().equals(wc.getSource().getName())
							&& curr_src.getLink().equals(wc.getSource().getLink())
							&& curr_src.getRss_link().equals(wc.getSource().getRss_link())) {
						hasValidSource = true;
					}
				}
				if(!hasValidSource) {
					Webtoremove.add(wc);
					wc.stop();
				}
			}
			m_Webcrawler.removeAll(Webtoremove);

			try {
				Thread.sleep(ConfigurationManager.getInstance().getCrawlerTimeout());
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}