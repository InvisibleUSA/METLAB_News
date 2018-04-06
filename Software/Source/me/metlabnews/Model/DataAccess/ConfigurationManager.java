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
}
