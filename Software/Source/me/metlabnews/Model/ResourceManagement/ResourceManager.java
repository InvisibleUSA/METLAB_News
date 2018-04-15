package me.metlabnews.Model.ResourceManagement;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.BaseXConnector;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class ResourceManager implements ServletContextListener
{
	public ResourceManager()
	{
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		System.out.println("[MESSAGE] Initializing...");
		System.out.println("          Server Info: " + sce.getServletContext().getServerInfo());

		// ConfigurationManager has to be initialized first!
		ConfigurationManager.getInstance().initialize();
		// ConfigurationManager has to be initialized second!
		Logger.getInstance().initialize();
		RelationalDbConnector.getInstance().initialize();
		BaseXConnector.getInstance().initialize();
		// Crawler.getInstance().initialize();
		// ClippingDaemon.getInstance().initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}
