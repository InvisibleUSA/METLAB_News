package me.metlabnews.Model.DataAccess;

import org.junit.Assert;
import org.junit.Test;



public class ConfigurationManagerTest
{

	@Test
	public void initialize()
	{
		ConfigurationManager.getInstance().initialize();
		Assert.assertTrue(ConfigurationManager.getInstance().isM_hasBeenInitialized());
	}

	@Test
	public void getBaseXUsername()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getBaseXUsername().equals("admin"));
	}

	@Test
	public void getBaseXPassword()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getBaseXPassword().equals("admin"));
	}

	@Test
	public void getRdbmsUsername()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getRdbmsUsername().equals("test"));
	}

	@Test
	public void getRdbmsPassword()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getRdbmsPassword().equals("test"));
	}

	@Test
	public void getMailPassword()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getMailPassword().equals("gewgdagftzmeftwt"));
	}

	@Test
	public void getMailSMTPServer()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getMailSMTPServer().equals("smtp.gmail.com"));
	}

	@Test
	public void getLogDestination()
	{
		Assert.assertTrue(ConfigurationManager.getInstance().getLogDestination().equals("ToFile"));
	}
}
