package me.metlabnews.Model.DataAccess;



import java.io.*;
import java.util.Properties;



public class ConfigurationManager
{
	private static ConfigurationManager instance;


	/**
	 * Private Constructor of Singleton
	 */
	private ConfigurationManager()
	{
	}


	/**
	 * Singleton call
	 *
	 * @return instance of this Class
	 */
	public static synchronized ConfigurationManager getInstance()
	{
		if(ConfigurationManager.instance == null)
		{
			ConfigurationManager.instance = new ConfigurationManager();
		}
		return ConfigurationManager.instance;
	}


	private enum TypePrefered
	{
		RSSFeed,
		Website
	}


	// Benny
	public long getCrawlerTimeout()
	{
		Properties props = new Properties();
		try
		{

			File        configFile  = new File("Settings.XML");
			InputStream inputStream = new FileInputStream(configFile);
			props.loadFromXML(inputStream);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(props.getProperty("TEST"));
		return 0L;
	}

	public int getMaxDocsPerDomain()
	{
		return 0;
	}

	public TypePrefered getTypePrefered()
	{
		return TypePrefered.RSSFeed;
	}


	// Erik
	public String getBaseXLoginUsername()
	{
		return "";
	}

	public String getBaseXLoginPassword()
	{
		return "";
	}

	public String getBaseXPath()
	{
		return "";
	}

	public String getSQLLoginUsername()
	{
		return "";
	}

	public String getSQLLoginPassword()
	{
		return "";
	}

	public String getMailFromAddress()
	{
		return "";
	}

	public String getMailLoginPassword()
	{
		return "";
	}

	public String getMailSMTPServer()
	{
		return "";
	}

	public long getClippingDaemonEnqueingTimeOut()
	{
		return 0L;
	}


	// Tobias
	public String getLoggerLogFilePath()
	{
		return "";
	}


}