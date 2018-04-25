package me.metlabnews.Model.ResourceManagement;

import me.metlabnews.Model.BusinessLogic.UserManager;
import me.metlabnews.Model.ClippingDaemon.ClippingDaemon;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryAddOrganisation;
import me.metlabnews.Presentation.Presenter;
import sun.rmi.runtime.Log;

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

		logger.logDebug(this, "Initializing...");
		logger.logDebug(this, "Server Info: "
				+ sce.getServletContext().getServerInfo());

		// ConfigurationManager has to be initialized first!
		ConfigurationManager.getInstance().initialize();
		logger.logDebug(this, "ConfigurationManager initialized");

		// Logger has to be initialized second!
		Logger.getInstance().initialize();
		logger.logDebug(this, "Logger initialized");

		// initialize DatabaseAccess
		new QueryAddOrganisation();

		// initialize ClippingDaemon
		ClippingDaemon cd = new ClippingDaemon();
		cd.initialize();
		logger.logDebug(this, "ClippingDaemon initialized");

		//MariaConnector.getInstance().initialize();
		//logger.logDebug(this, "MariaConnector initialized");

		//BaseXConnector.getInstance().initialize();
		//logger.logDebug(this,"BaseXConnector initialized");

		// Crawler.getInstance().initialize();
		//logger.logDebug(this,"Crawler initialized");



		//UserManager.Validator.getInstance().initialize();
		UserManager userManager = new UserManager();
		logger.logDebug(this, "UserManager initialized");

		Presenter.getInstance().initialize();
		logger.logDebug(this, "Presenter initialized");
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}
