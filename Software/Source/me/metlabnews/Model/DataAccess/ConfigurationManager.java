package me.metlabnews.Model.DataAccess;



import me.metlabnews.Model.Common.Logger;

import java.io.*;
import java.util.Properties;



public class ConfigurationManager
{
	private final String m_XMLFilePath =
			(System.getProperty("user.dir") + "\\Software\\Source\\me\\metlabnews\\Model\\DataAccess\\Settings.XML");


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

	/**
	 * Prefered Type to Crawl
	 */
	private enum TypePrefered
	{
		RSSFeed,
		Website
	}

	/**
	 * This is the main-method of the XML Properties. It returns the values of the given
	 * String-keyvalue.
	 *
	 * @param keyvalue The String-key of the value
	 * @return the value of the property
	 */
	private String returnProperty(String keyvalue)
	{
		Properties properties = new Properties();
		try(InputStream inputStream = new FileInputStream(new File(m_XMLFilePath)))
		{
			properties.loadFromXML(inputStream);
			return properties.getProperty(keyvalue);
		}
		catch(Exception e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.getMessage());
			return null;
		}
	}


	// Benny
	public long getCrawlerTimeout()
	{
		try
		{
			return Long.parseLong(this.returnProperty("CrawlerTimeout"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.toString());
			return 0L;
		}
	}

	public int getMaxDocsPerDomain()
	{
		try
		{
			return Integer.parseInt(this.returnProperty("MaxDocsPerDomain"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.toString());
			return 0;
		}
	}

	public TypePrefered getTypePrefered()
	{
		try
		{
			switch(String.format(this.returnProperty("TypePrefered")))
			{
				case "RSSFeed":
					return TypePrefered.RSSFeed;
				case "Website":
					return TypePrefered.Website;
				default:
					return null;
			}
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.toString());
			return null;
		}
	}


	// Erik
	public String getBaseXLoginUsername()
	{
		return this.returnProperty("BaseXLoginUsername");
	}

	public String getBaseXLoginPassword()
	{
		return this.returnProperty("BaseXLoginPassword");
	}

	public String getBaseXPath()
	{
		return this.returnProperty("BaseXPath");
	}

	public String getSQLLoginUsername()
	{
		return this.returnProperty("SQLLoginUsername");
	}

	public String getSQLLoginPassword()
	{
		return this.returnProperty("SQLLoginPassword");
	}

	public String getMailFromAddress()
	{
		return this.returnProperty("MailFromAddress");
	}

	public String getMailLoginPassword()
	{
		return this.returnProperty("MailLoginPassword");
	}

	public String getMailSMTPServer()
	{
		return this.returnProperty("MailSMTPServer");
	}

	public long getClippingDaemonEnqueingTimeOut()
	{
		try
		{
			return Long.parseLong(this.returnProperty("ClippingDaemonEnqueingTimeOut"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.toString());
			return 0L;
		}
	}


	// Tobias

	/**
	 * Gets the File Path of the Logger
	 * @return the Path of the Logger
	 */
	public String getLoggerLogFilePath()
	{
		return this.returnProperty("LoggerLogFilePath");
	}


}