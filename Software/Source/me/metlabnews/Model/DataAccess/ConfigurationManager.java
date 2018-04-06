package me.metlabnews.Model.DataAccess;



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
		return 0;
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


	// Tobias
	public String getLoggerLogFilePath()
	{
		return "";
	}


}
