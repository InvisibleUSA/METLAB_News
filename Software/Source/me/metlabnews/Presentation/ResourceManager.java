package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.UserManager;

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
		System.out.println("[MESSAGE] Presentation.ResourceManager.contextInitialized()");
		System.out.println("          Server Info: " + sce.getServletContext().getServerInfo());
		UserManager userManager = UserManager.create();
		Presenter.create(userManager);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}
}