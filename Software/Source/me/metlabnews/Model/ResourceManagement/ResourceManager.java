package me.metlabnews.Model.ResourceManagement;

import me.metlabnews.Model.BusinessLogic.UserManager;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.BaseXConnector;
import me.metlabnews.Model.DataAccess.DbConnectors.MariaConnector;
import me.metlabnews.Model.DataAccess.DbConnectors.MariaConnector;
import me.metlabnews.Presentation.Presenter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class ResourceManager implements ServletContextListener
{
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

		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG, "Initializing...");
		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG, "Server Info: "
				+ sce.getServletContext().getServerInfo());

		// ConfigurationManager has to be initialized first!
		ConfigurationManager.getInstance().initialize();
		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG,
		           "ConfigurationManager initialized");

		// Logger has to be initialized second!
		Logger.getInstance().initialize();
		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG,
		           "Logger initialized");

		//MariaConnector.getInstance().initialize();
		//logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG, "MariaConnector initialized");

		//BaseXConnector.getInstance().initialize();
		//logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG,"BaseXConnector initialized");

		// Crawler.getInstance().initialize();
		//		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG,
		//		           "Crawler initialized");

		// ClippingDaemon.getInstance().initialize();
		//		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG,
		//		           "ClippingDaemon initialized");

		//UserManager.Validator.getInstance().initialize();
		UserManager userManager = new UserManager();
		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG, "UserManager initialized");

		Presenter.getInstance().initialize();
		logger.log(Logger.Channel.ResourceManager, Logger.LogPriority.DEBUG, "Presenter initialized");
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}
