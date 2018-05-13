package me.metlabnews.Model.ResourceManagement;

import me.metlabnews.Model.BusinessLogic.UserManager;
import me.metlabnews.Model.ClippingDaemon.ClippingDaemon;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Crawler.CrawlerController;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryAddOrganisation;
import me.metlabnews.Presentation.Presenter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class ResourceManager implements ServletContextListener
{
	static
	{
		Logger.getInstance().register(ResourceManager.class, Logger.Channel.ResourceManager);
	}

	public ResourceManager()
	{
	}


	/**
	 * Entry point of the application
	 * @param sce ServletContextEvent
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		Logger logger = Logger.getInstance();

		logger.logActivity(this, "Initializing...");
		logger.logActivity(this, "Server Info: " + sce.getServletContext().getServerInfo());

		// ConfigurationManager has to be initialized first!
		ConfigurationManager.getInstance().initialize();
		logger.logActivity(this, "ConfigurationManager initialized");

		// Logger has to be initialized second!
		Logger.getInstance().initialize();
		logger.logActivity(this, "Logger initialized");

		// here you can disable single channels of the logger
		/*
		Logger.getInstance().disable(Logger.Channel.Crawler,
		                             Logger.LogLevel.DEBUG); // This will disable DEBUGS from CRAWLER
		Logger.getInstance().disable(Logger.Channel.NONE, Logger.LogLevel.WARNING);  // This will disable ALL WARNINGS
		Logger.getInstance().disable(Logger.Channel.Crawler,
		                             Logger.LogLevel.NONE);  // This will disable Crawler-Channel
		*/
		//Logger.getInstance().disable(Logger.Channel.XMLTag, Logger.LogLevel.DEBUG);
		// initialize DatabaseAccess
		new QueryAddOrganisation();

		// initialize ClippingDaemon
		//ClippingDaemon cd = new ClippingDaemon();
		//cd.initialize();
		//logger.logDebug(this, "ClippingDaemon initialized");

		//MariaConnector.getInstance().initialize();
		//logger.logDebug(this, "MariaConnector initialized");

		//BaseXConnector.getInstance().initialize();
		//logger.logDebug(this,"BaseXConnector initialized");

		CrawlerController.getInstance().initialize();
		logger.logDebug(this, "Crawler initialized");



		UserManager.Validator.getInstance().initialize();
		//UserManager.getInstance().initialize();
		logger.logDebug(this, "UserManager initialized");

		Presenter.getInstance().initialize();
		logger.logDebug(this, "Presenter initialized");

		CrawlerController.getInstance().start();
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}
