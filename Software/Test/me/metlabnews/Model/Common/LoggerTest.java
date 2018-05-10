package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;



class LoggerTest
{

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}

	@Test
	void register()
	{
		Logger.getInstance().register(LoggerTest.class, Logger.Channel.JUnitTest);
	}

	@Test
	void logDebug()
	{
		Logger.getInstance().logDebug(this, "JUnit Test Message to log in DEBUG !");
	}

	@Test
	void logError()
	{
		Logger.getInstance().logError(this, "JUnit Test Message to log in ERROR !");
	}

	@Test
	void logWarning()
	{
		Logger.getInstance().logWarning(this, "JUnit Test Message to log in WARNING !");
	}

	@Test
	void logActivity()
	{
		Logger.getInstance().logActivity(this, "JUnit Test Message to log in ACTIVITY !");
	}

	@Test
	void logInfo()
	{
		Logger.getInstance().logInfo(this, "JUnit Test Message to log in INFO !");
	}

	@Test
	void hasCreatedDirectoryProperly()
	{
		String dirPath = (System.getProperty(
				"user.dir")) + File.separator + "Logs" + File.separator + Logger.Channel.JUnitTest.name();
		assertTrue(new File(dirPath).exists());
	}

}