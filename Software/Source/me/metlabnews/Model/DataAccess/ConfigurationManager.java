package me.metlabnews.Model.DataAccess;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.*;
import java.util.Properties;



@SuppressWarnings({"ConstantConditions", "unused"})
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
	public boolean hasBeenInitialized()
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

	/**
	 * Returns the minimum length of the security password.
	 *
	 * @return an Integer of the minimum length.
	 */
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


	/**
	 * Returns true or false if the Password requires lower case Letters.
	 *
	 * @return TRUE if a low case letter is required.
	 */
	public boolean getSecurityPasswordLowerCaseLetterRequired()
	{
		String key = "Security.Password.LowerCaseLetterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	/**
	 * Returns true or false if the Password requires upper case Letters.
	 *
	 * @return TRUE if a upper case letter is required.
	 */
	public boolean getSecurityPasswordUpperCaseLetterRequired()
	{
		String key = "Security.Password.UpperCaseLetterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	/**
	 * Returns true or false if the Password requires special character Letters.
	 *
	 * @return TRUE if a special character letter is required.
	 */
	public boolean getSecurityPasswordSpecialCharacterRequired()
	{
		String key = "Security.Password.SpecialCharacterRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	/**
	 * Returns true or false if the Password requires digits.
	 *
	 * @return TRUE if a digit is required.
	 */
	public boolean getSecurityPasswordDigitRequired()
	{
		String key = "Security.Password.DigitRequired";
		return Boolean.parseBoolean(returnProperty(key));
	}


	/**
	 * Returns true or false if the Password forbids whitespace.
	 *
	 * @return TRUE if whitespace is forbidden.
	 */
	public boolean getSecurityPasswordWhitespaceForbidden()
	{
		String key = "Security.Password.WhitespaceForbidden";
		return Boolean.parseBoolean(returnProperty(key));
	}
	// endregion Security


	// region Crawler

	/**
	 * Returns the Timeout interval of the Crawler as Long type.
	 *
	 * @return The Timeout interval of the Crawler.
	 */
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


	/**
	 * Returns the maximum documents of the domain, which the crawler will crawl.
	 * This is needed to reduce the amount of space and memory.
	 *
	 * @return The maximum Documents to crawl per domain
	 */
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


	/**
	 * Returns the preferred Type of the Crawler. It can return:
	 * 
	 * {@code}
	 * RSSFeed or Website
	 * 
	 *
	 * @return RSSFeed or Website as TypePrefered ENUM.
	 */
	public TypePreferred getCrawlerTypePreferred()
	{
		String key = "Crawler.TypePreferred";
		try
		{
			switch(returnProperty(key))
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

	/**
	 * Returns the enqueuing timeout for the Clipping Daemon.
	 *
	 * @return the enqueuing timeout for the Clipping Daemon.
	 */
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

	/**
	 * Returns the path of the BaseX
	 *
	 * @return the path of the BaseX
	 */
	public String getBaseXPath()
	{
		String key = "BaseX.Path";
		return returnProperty(key);
	}


	/**
	 * Returns the name of the BaseX database
	 *
	 * @return the name of the BaseX database
	 */
	public String getBaseXDbName()
	{
		String key = "BaseX.DbName";
		return returnProperty(key);
	}


	/**
	 * Returns the port of the BaseX database
	 *
	 * @return the port of the BaseX database
	 */
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


	/**
	 * Returns the username of the BaseX database
	 *
	 * @return the username of the BaseX database to login and queue
	 */
	public String getBaseXUsername()
	{
		String key = "BaseX.Username";
		return returnProperty(key);
	}


	/**
	 * Returns the password of the BaseX database
	 *
	 * @return the password of the BaseX database to login
	 */
	public String getBaseXPassword()
	{
		String key = "BaseX.Password";
		return returnProperty(key);
	}
	// endregion DocDBMS


	// region RDBMS

	/**
	 * Returns the driver of the RDBMS database
	 *
	 * @return the driver of the RDBMS database
	 */
	public String getRdbmsDriver()
	{
		String key = "RDBMS.Driver";
		return returnProperty(key);
	}


	/**
	 * Returns the remote URL of the RDBMS database
	 *
	 * @return the remote URL of the RDBMS database
	 */
	public String getRdbmsRemoteUrl()
	{
		String key = "RDBMS.RemoteURL";
		return returnProperty(key);
	}


	/**
	 * Returns the local URL of the RDBMS database
	 *
	 * @return the local URL of the RDBMS database
	 */
	public String getRdbmsLocalUrl()
	{
		String key = "RDBMS.LocalURL";
		return returnProperty(key);
	}


	/**
	 * Returns the usage of the RDBMS database
	 *
	 * @return the usage of the RDBMS database
	 */
	public boolean getRdbmsUseLocalDB()
	{
		String key = "RDBMS.UseLocalDB";
		return Boolean.parseBoolean(returnProperty(key));
	}


	/**
	 * Returns the username of the RDBMS database
	 *
	 * @return the username of the RDBMS database
	 */
	public String getRdbmsUsername()
	{
		String key = "RDBMS.Username";
		return returnProperty(key);
	}


	/**
	 * Returns the password of the RDBMS database
	 *
	 * @return the password of the RDBMS database
	 */
	public String getRdbmsPassword()
	{
		String key = "RDBMS.Password";
		return returnProperty(key);
	}
	// endregion RDBMS


	// region Mail

	/**
	 * Returns the E-Mail address of the sender. (usually it is metlabnews@gmail.com)
	 *
	 * @return the E-Mail of the sender
	 */
	public String getMailFromAddress()
	{
		String key = "Mail.FromAddress";
		return returnProperty(key);
	}


	/**
	 * Returns the password of the sender E-Mail
	 *
	 * @return the password of the sender E-Mail
	 */
	public String getMailPassword()
	{
		String key = "Mail.Password";
		return returnProperty(key);
	}


	/**
	 * Returns the SMTP Server of the sender E-Mail
	 *
	 * @return the SMTP Server of the sender E-Mail
	 */
	public String getMailSMTPServer()
	{
		String key = "Mail.SMTPServer";
		return returnProperty(key);
	}


	/**
	 * Returns the port of the sender E-Mail Host
	 *
	 * @return the port of the sender E-Mail Host
	 */
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


	/**
	 * Returns the log destination of the logger.
	 * This can e.g. be ToFile or ToConsole
	 *
	 * @return the log destination of the logger.
	 */
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
	 * Preferred Type to Crawl
	 */
	public enum TypePreferred
	{
		RSSFeed,
		Website
	}

	/**
	 * Preferred LogDestination
	 */
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


	private Properties m_properties;
	private boolean m_hasBeenInitialized = false;
	private static ConfigurationManager m_instance;
	private final String m_XMLFilePath = setXMLFilePath();
}