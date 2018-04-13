package me.metlabnews.Model.DataAccess;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;



public class ConfigurationManager implements IResource
{
	/**
	 * Singleton call
	 *
	 * @return instance of this Class
	 */
	public static synchronized ConfigurationManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ConfigurationManager();
		}
		return m_instance;
	}


	@Override
	public void initialize()
	{
		try(InputStream inputStream = new FileInputStream(new File(m_XMLFilePath)))
		{
			m_properties.loadFromXML(inputStream);
			m_hasBeenInitialized = true;
		}
		catch(FileNotFoundException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         m_XMLFilePath + " not found:\n"
					                         + e.toString());
		}
		catch(InvalidPropertiesFormatException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
		}
	}

	@Override
	public void close()
	{

	}


	// region Crawler

	public long getCrawlerTimeout()
	{
		try
		{
			return Long.parseLong(returnProperty("Crawler.Timeout"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
			return 0L;
		}
	}

	public int getCrawlerMaxDocsPerDomain()
	{
		try
		{
			return Integer.parseInt(returnProperty("Crawler.MaxDocsPerDomain"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
			return 0;
		}
	}

	public TypePreferred getCrawlerTypePreferred()
	{
		try
		{
			switch(String.format(returnProperty("Crawler.TypePreferred")))
			{
				case "RSSFeed":
					return TypePreferred.RSSFeed;
				case "Website":
					return TypePreferred.Website;
				default:
					return null;
			}
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
			return null;
		}
	}

	// endregion Crawler


	// region ClippingDaemon

	public long getClippingDaemonEnqueuingTimeOut()
	{
		try
		{
			return Long.parseLong(returnProperty("ClippingDaemon.EnqueuingTimeOut"));
		}
		catch(NumberFormatException e)
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         e.toString());
			return 0L;
		}
	}

	// endregion ClippingDaemon


	// region BaseX

	public String getBaseXPath()
	{
		return returnProperty("BaseX.Path");
	}

	public String getBaseXUsername()
	{
		return returnProperty("BaseX.Username");
	}

	public String getBaseXPassword()
	{
		return returnProperty("BaseX.Password");
	}

	// endregion BaseX


	// region RDBMS

	public String getRdbmsDriver()
	{
		return returnProperty("RDBMS.Driver");
	}

	public String getRdbmsRemoteUrl()
	{
		return returnProperty("RDBMS.RemoteURL");
	}

	public String getRdbmsLocalUrl()
	{
		return returnProperty("RDBMS.LocalURL");
	}

	public boolean getRdbmsUseLocalDB()
	{
		return Boolean.parseBoolean(returnProperty("RDBMS.UseLocalDB"));
	}

	public String getRdbmsUsername()
	{
		return returnProperty("RDBMS.Username");
	}

	public String getRdbmsPassword()
	{
		return returnProperty("RDBMS.Password");
	}

	public String getRdbmsSqlDialect()
	{
		return returnProperty("RDBMS.SQLDialect");
	}

	// endregion RDBMS


	// region Mail

	public String getMailFromAddress()
	{
		return returnProperty("Mail.FromAddress");
	}

	public String getMailPassword()
	{
		return returnProperty("Mail.Password");
	}

	public String getMailSMTPServer()
	{
		return returnProperty("Mail.SMTPServer");
	}

	// endregion Mail


	// region Logger

	/**
	 * Gets the File Path of the Logger
	 *
	 * @return the Path of the Logger
	 */
	public String getLoggerLogFilePath()
	{
		return (System.getProperty("user.dir")) + returnProperty("Logger.LogFilePath");
	}


	public String getLogType()
	{
		return (System.getProperty("user.dir")) + returnProperty("Logger.LogType");
	}


	/**
	 * This Method checks the Settings file and returns true or false if the specified Setting
	 * is filtered. Then all the filtered Prioritys will be ignored.
	 *
	 * @param priority The Priority
	 * @return true or false
	 */
	public boolean getFilteredPriorities(String priority)
	{
		return Boolean.parseBoolean(returnProperty("Logger.Filter" + priority + ""));
	}

	// endregion Logger



	/**
	 * Private Constructor of Singleton
	 */
	private ConfigurationManager()
	{
		m_properties = new Properties();
	}


	/**
	 * Prefered Type to Crawl
	 */
	private enum TypePreferred
	{
		RSSFeed,
		Website
	}


	/**
	 * This is the main-method of the XML Properties. It returns the values of the given
	 * String-key.
	 *
	 * @param key The String-key of the value
	 * @return the value of the property
	 */
	private String returnProperty(String key)
	{
		if(m_hasBeenInitialized)
		{
			String value = m_properties.getProperty(key);
			if(value == null)
			{
				Logger.getInstance().log(Logger.Channel.ConfigurationManager,
				                         Logger.logPriority.ERROR,
				                         "Key '" + key + "' not found in: "
						                         + m_XMLFilePath);
			}
			return value;
		}
		else
		{
			Logger.getInstance().log(Logger.Channel.ConfigurationManager,
			                         Logger.logPriority.ERROR,
			                         "Initialization Error returning property for key '" + key + "'. " +
					                         "Logger has not been initialized!");
			return null;
		}
	}


	private boolean m_hasBeenInitialized = false;
	private static ConfigurationManager m_instance;
	private final String m_XMLFilePath = (System.getProperty(
			"user.dir") + "" + File.separator + "Resources" + File.separator + "Settings.XML");
	private Properties m_properties;
}