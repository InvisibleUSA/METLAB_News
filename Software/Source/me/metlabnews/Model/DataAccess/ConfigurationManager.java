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
		try
		{
			InputStream inputStream = new FileInputStream(new File(m_XMLFilePath));
			m_properties.loadFromXML(inputStream);
			inputStream.close();
		}
		catch(FileNotFoundException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         m_XMLFilePath + " not found:\n"
			                         + e.toString());
		}
		catch(InvalidPropertiesFormatException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         e.toString());
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
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
			return Long.parseLong(this.returnProperty("Crawler.Timeout"));
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

	public int getCrawlerMaxDocsPerDomain()
	{
		try
		{
			return Integer.parseInt(this.returnProperty("Crawler.MaxDocsPerDomain"));
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

	public TypePreferred getCrawlerTypePreferred()
	{
		try
		{
			switch(String.format(this.returnProperty("Crawler.TypePreferred")))
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
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
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
			return Long.parseLong(this.returnProperty("ClippingDaemon.EnqueuingTimeOut"));
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

	// endregion ClippingDaemon


	// region BaseX

	public String getBaseXPath()
	{
		return this.returnProperty("BaseX.Path");
	}

	public String getBaseXUsername()
	{
		return this.returnProperty("BaseX.Username");
	}

	public String getBaseXPassword()
	{
		return this.returnProperty("BaseX.Password");
	}

	// endregion BaseX


	// region RDBMS

	public String getRdbmsDriver()
	{
		return this.returnProperty("RDBMS.Driver");
	}

	public String getRdbmsRemoteUrl()
	{
		return this.returnProperty("RDBMS.RemoteURL");
	}

	public String getRdbmsLocalUrl()
	{
		return this.returnProperty("RDBMS.LocalURL");
	}

	public boolean getRdmsUseLocalDb()
	{
		return Boolean.parseBoolean(this.returnProperty("RDBMS.UseLocalDB"));
	}

	public String getRdbmsUsername()
	{
		return this.returnProperty("RDBMS.Username");
	}

	public String getRdbmsPassword()
	{
		return this.returnProperty("RDBMS.Password");
	}

	public String getRdbmsSqlDialect()
	{
		return this.returnProperty("RDBMS.SQLDialect");
	}

	// endregion RDBMS


	// region Mail

	public String getMailFromAddress()
	{
		return this.returnProperty("Mail/FromAddress");
	}

	public String getMailPassword()
	{
		return this.returnProperty("Mail.Password");
	}

	public String getMailSMTPServer()
	{
		return this.returnProperty("Mail.SMTPServer");
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
		return (System.getProperty("user.dir")) + this.returnProperty("Logger.LogFilePath");
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
		switch(priority)
		{
			case "DEBUG":
				return Boolean.parseBoolean(this.returnProperty("Logger.FilterDEBUG"));
			case "WARNING":
				return Boolean.parseBoolean(this.returnProperty("Logger.FilterWARNING"));
			case "ERROR":
				return Boolean.parseBoolean(this.returnProperty("Logger.FilterERROR"));
			default:
				return true;
		}
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
		String value =  m_properties.getProperty(key);
		if(value == null)
		{
			Logger.getInstance().log(Logger.enum_channel.ConfigurationManager,
			                         Logger.enum_logPriority.ERROR,
			                         Logger.enum_logType.ToFile,
			                         "Key " + key + " not found in !"
					                         + m_XMLFilePath);
		}
		return value;
	}



	private static ConfigurationManager m_instance;
	private final String m_XMLFilePath = (System.getProperty("user.dir") + "\\Resources\\Settings.XML");
	private Properties m_properties;
}