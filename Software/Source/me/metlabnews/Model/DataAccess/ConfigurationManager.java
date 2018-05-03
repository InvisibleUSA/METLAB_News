package me.metlabnews.Model.DataAccess;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.*;
import java.util.Properties;



public class ConfigurationManager implements IResource
{
	static
	{
		Logger.getInstance().register(ConfigurationManager.class, Logger.Channel.ConfigurationManager);
	}

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


	/**
	 * Returns if the Configuration Manager has been correctly initialized
	 *
	 * @return true if it has been initialized
	 */
	public boolean isM_hasBeenInitialized()
	{
		return m_hasBeenInitialized;
	}


	@Override
	public void initialize()
	{
		try(InputStream inputStream = new FileInputStream(new File(m_XMLFilePath)))
		{
			m_properties.loadFromXML(inputStream);
			m_hasBeenInitialized = true;
			Logger.getInstance().logActivity(this, "ConfigurationManager has been initialized...");
		}
		catch(FileNotFoundException e)
		{
			Logger.getInstance().logError(this, m_XMLFilePath + " not found:\n" + e.toString());
		}
		catch(IOException e)
		{
			Logger.getInstance().logError(this, e.toString());
		}
	}


	@Override
	public void close()
	{

	}


	private void setNumberFormatException(NumberFormatException e, String key)
	{
		String errorMsg = "IOException in Configuration Manager." +
				" Please check Settings.XML and depending getter-method for Key: '" + key + "'. Error Message: ";
		Logger.getInstance().logError(this, errorMsg + e.toString());
	}


	// region Security

	public int getSecurityPasswordMinimumLength()
	{
		String key = "Security.Password.MinimumLength";
		try
		{
			return Integer.parseInt(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0;
		}
	}


	public boolean getSecurityPasswordLowerCaseLetterRequired()
	{
		String key = "Security.Password.LowerCaseLetterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	public boolean getSecurityPasswordUpperCaseLetterRequired()
	{
		String key = "Security.Password.UpperCaseLetterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	public boolean getSecurityPasswordSpecialCharacterRequired()
	{
		String key = "Security.Password.SpecialCharacterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	public boolean getSecurityPasswordDigitRequired()
	{
		String key = "Security.Password.DigitRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	public boolean getSecurityPasswordWhitespaceForbidden()
	{
		String key = "Security.Password.WhitespaceForbidden";
		return Boolean.parseBoolean(returnProperty(key));
	}

	// endregion Security


	// region Crawler

	public long getCrawlerTimeout()
	{
		String key = "Crawler.Timeout";
		try
		{
			return Long.parseLong(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0L;
		}
	}

	public int getCrawlerMaxDocsPerDomain()
	{
		String key = "Crawler.MaxDocsPerDomain";
		try
		{
			return Integer.parseInt(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0;
		}
	}

	public TypePreferred getCrawlerTypePreferred()
	{
		String key = "Crawler.TypePreferred";
		try
		{
			switch(String.format(returnProperty(key)))
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
			setNumberFormatException(e, key);
			return null;
		}
	}

	// endregion Crawler


	// region ClippingDaemon

	public long getClippingDaemonEnqueuingTimeOut()
	{
		String key = "ClippingDaemon.EnqueuingTimeOut";
		try
		{
			return Long.parseLong(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0L;
		}
	}

	// endregion ClippingDaemon


	// region DocDBMS

	public String getBaseXPath()
	{
		String key = "BaseX.Path";
		return returnProperty(key);
	}

	public String getBaseXDbName()
	{
		String key = "BaseX.DbName";
		return returnProperty(key);
	}

	public int getBaseXPort()
	{
		String key = "BaseX.Port";
		try
		{
			return Integer.parseInt(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0;
		}
	}

	public String getBaseXUsername()
	{
		String key = "BaseX.Username";
		return returnProperty(key);
	}

	public String getBaseXPassword()
	{
		String key = "BaseX.Password";
		return returnProperty(key);
	}

	// endregion DocDBMS


	// region RDBMS

	public String getRdbmsDriver()
	{
		String key = "RDBMS.Driver";
		return returnProperty(key);
	}

	public String getRdbmsRemoteUrl()
	{
		String key = "RDBMS.RemoteURL";
		return returnProperty(key);
	}

	public String getRdbmsLocalUrl()
	{
		String key = "RDBMS.LocalURL";
		return returnProperty(key);
	}

	public boolean getRdbmsUseLocalDB()
	{
		String key = "RDBMS.UseLocalDB";
		return Boolean.parseBoolean(returnProperty(key));
	}

	public String getRdbmsUsername()
	{
		String key = "RDBMS.Username";
		return returnProperty(key);
	}

	public String getRdbmsPassword()
	{
		String key = "RDBMS.Password";
		return returnProperty(key);
	}

	// endregion RDBMS


	// region Mail

	public String getMailFromAddress()
	{
		String key = "Mail.FromAddress";
		return returnProperty(key);
	}


	public String getMailPassword()
	{
		String key = "Mail.Password";
		return returnProperty(key);
	}


	public String getMailSMTPServer()
	{
		String key = "Mail.SMTPServer";
		return returnProperty(key);
	}


	public int getMailSMTPPort()
	{
		String key = "Mail.SMTPPort";
		try
		{
			return Integer.parseInt(returnProperty(key));
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return 0;
		}
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
		String key = "Logger.LogFilePath";
		return (System.getProperty("user.dir")) + returnProperty(key);
	}


	public String getLogDestination()
	{
		String key = "Logger.LogDestination";
		try
		{
			switch(returnProperty(key))
			{
				case "ToFile":
					return LogDestination.ToFile.name();
				case "ToConsole":
					return LogDestination.ToConsole.name();
				default:
					return null;
			}
		}
		catch(NumberFormatException e)
		{
			setNumberFormatException(e, key);
			return null;
		}
	}


	/**
	 * This Method checks the Settings file and returns true or false if the specified Setting
	 * is filtered. Then all the filtered Prioritys will be ignored.
	 *
	 * @param priority The Priority
	 * @return true or false
	 */
	public boolean isLevelDisabled(String priority)
	{
		String key = "Logger.isDisabled_" + priority;
		return Boolean.parseBoolean(returnProperty(key));
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


	private enum LogDestination
	{
		ToFile,
		ToConsole,
	}


	/**
	 * This is the main-method of the XML Properties. It returns the values of the given
	 * String-key. If the key is not found, null is returned
	 *
	 * @param key The String-key of the value
	 * @return the value of the property, null if not found
	 */
	private String returnProperty(String key)
	{
		if(m_hasBeenInitialized)
		{
			String value = m_properties.getProperty(key);
			if(value == null)
			{
				Logger.getInstance().logError(this, "Key '" + key + "' not found in: "
						+ m_XMLFilePath);
			}
			return value;
		}
		else
		{
			Logger.getInstance().logError(this, "Initialization Error returning property for key '" + key + "'. "
					+ this.getClass().getSimpleName() + " has not been initialized!");
			return null;
		}
	}

	private String setXMLFilePath()
	{
		if(System.getProperty("user.dir").contains("Software"))
		{
			return (System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "Settings.XML");
		}
		return (System.getProperty(
				"user.dir") + File.separator + "Software" + File.separator + "Resources" + File.separator + "Settings.XML");
	}


	private        boolean              m_hasBeenInitialized = false;
	private static ConfigurationManager m_instance;
	private final  String               m_XMLFilePath        = setXMLFilePath();
	private        Properties           m_properties;
}