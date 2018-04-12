package me.metlabnews.Model.ResourceManagement;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.BaseXConnector_Marco;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector_Marco;
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
		RelationalDbConnector_Marco.getInstance().initialize();
		BaseXConnector_Marco.getInstance().initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}
