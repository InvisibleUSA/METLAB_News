package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;



/**
 * <p>
 * This Class is for logging the whole error's and debug outputs of the application.
 * </p>
 * <p>
 * You can also log user controls and registers to the destination. The log-files are stored
 * in the application folder in ...\Logs\
 * <p>
 * The File and the subfolder are created automatically.
 * </p>
 * <p>
 * Careful: You need first to register the logger with the following Code:
 * <p>
 * {@code
 * Logger.getInstance.register(CLASSNAME.class, Logger.Channel.ChannelName)
 * }
 * </p>
 * </p>
 * </p>
 * Example of logging-usage:
 * <p>
 * {@code
 * Logger.getInstance.logError(this, "enter your log-msg here...");
 * }
 * </p>
 *
 * @author Tobias Reis
 * @version 2.1
 */
public class Logger implements IResource
{
	static
	{
		Logger.getInstance().register(Logger.class, Channel.Logger);
	}

	/**
	 * Singleton call of this instance.
	 * <p>
	 * Example:
	 * </p>
	 * Logger.getInstance().log(Channel channel, Priority priority, String message);
	 *
	 * @return instance of this Class with all its methods
	 */
	public static Logger getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new Logger();
		}
		return m_instance;
	}

	private Logger()
	{

	}


	@Override
	public void initialize()
	{
		if(ConfigurationManager.getInstance().isM_hasBeenInitialized())
		{
			LogDestination = ConfigurationManager.getInstance().getLogDestination();

			if(m_hasBeenInitialized = (enableChannel() && enableLevel()))
			{
				logActivity(this, "Logger has been initialized...");
			}
			else
			{
				logError(this, "logger has NOT been initialized !");
			}
			FullFilePath = ConfigurationManager.getInstance().getLoggerLogFilePath();
		}
		else

		{
			logError(this, "ConfigurationManager is NOT initialized. Logger will NOT bei initialized!");
			m_hasBeenInitialized = ConfigurationManager.getInstance().isM_hasBeenInitialized();
		}
	}

	@Override
	public void close()
	{
		logActivity(this, "Logger has been closed.");
	}


	/**
	 * <p>
	 * This Enum is for the channels to log the output message to the specific channel.
	 * Every class or module will have it's own Channel where you can log from.
	 * The channels will help to separate the log files into different folders.
	 * </p>
	 */
	public enum Channel
	{
		NONE,
		ConfigurationManager,
		ClippingDaemon,
		Crawler,
		UI,
		Presenter,
		ResourceManager,
		UserManager,
		Mail,
		XMLTag,
		Logger,
		RDBMS,
		DocDBMS,
		YaCy,
		UNREGISTERED_CHANNEL
	}


	/**
	 * This enum represents the status of the Channel. You can enable and disable
	 * each single Channels by a HashMap.
	 * <p>
	 * Unregistered = Channel by default disabled
	 * </p>
	 */
	public enum ChannelFlag
	{
		ENABLED,
		DISABLED,
	}


	/**
	 * This enum represents the status of the LogLevel. You can enable and disable
	 * each single LogLevel by a HashMap.
	 * <p>
	 * Unregistered = Level by default disabled
	 * </p>
	 */
	public enum LevelFlag
	{
		ENABLED,
		DISABLED,
	}


	/**
	 * <p>
	 * This Enum represents the priority of the logged message. It will be possible to set the
	 * internal Priority higher than the called Priority. This allows the user to filter some logs.
	 * If e.g. the Priority of the internal logger is higher than the called Priority, than the message
	 * will NOT be logged.
	 * </p>
	 */
	public enum LogLevel
	{
		NONE,
		DEBUG,
		WARNING,
		ERROR,
		REGISTRATION,
		ACTIVITY,
		INFO
	}


	/**
	 * This method returns a simple TimeStamp for the logger.
	 *
	 * @return Timestamp in format: "dd.MM.yyyy [HH:mm:ss]"
	 */
	private String getTimeStamp()
	{
		return new SimpleDateFormat("[HH:mm:ss]").format(Calendar.getInstance().getTime());
	}


	/**
	 * This method returns a Date to check if a file already exists
	 *
	 * @return A String of Date with the format: "dd-MM-yyyy"
	 */
	private String getDateString()
	{
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}


	/**
	 * This method will return the final string which is logged to the target.
	 * <p>Example:</p>
	 * {@code
	 * #6 [23:28:31] [ACTIVITY - Logger @ UNREGISTERED_CHANNEL]: Logger has been DISABLED for Channel: 'UNREGISTERED_CHANNEL'
	 * }
	 *
	 * @param level   The priority of the logs
	 * @param channel The specific log type
	 * @param msg     The Message you want to log
	 * @param counter The Counter of the logs
	 * @return A parsed String to log
	 */
	private String createLogString(Object sender, LogLevel level, Channel channel, String msg, int counter)
	{
		return ("#" + counter + " " + getTimeStamp() + " [" + level.name() + " - " + sender.getClass().getSimpleName() + " @ " + channel.name() + "]: " + msg);
	}


	/**
	 * This Method will return the value of the forbidden LogLevel. If e.g. the DEBUG-Priority
	 * is forbidden, then every called DEBUG-calls will be ignored.
	 *
	 * @param level the Priority (e.g. DEBUG, WARNING, ERROR)
	 * @return true is forbidden
	 */
	private boolean isLevelForbidden(LogLevel level)
	{
		boolean local  = false;
		boolean global = false;

		if(m_hasBeenInitialized && level != null)
		{
			switch(m_globalLevelFlag.get(level))
			{
				case DISABLED:
					global = true;
					break;
				case ENABLED:
					break;
			}

			switch(m_localLevelFlag.get(level))
			{
				case ENABLED:
					break;
				case DISABLED:
					local = true;
					break;
			}
		}
		return (global || local);
	}


	/**
	 * This method returns TRUE if the Channel is forbidden and FALSE if it is not forbidden.
	 *
	 * @param channel The Channel, e.g.: Crawler, UI, ...
	 * @return true = FORBIDDEN // false = not forbidden
	 */
	private boolean isChannelForbidden(Channel channel)
	{
		if(m_hasBeenInitialized && channel != null)
		{
			switch(m_channelFlag.get(channel))
			{
				case ENABLED:
					return false;
				case DISABLED:
					return true;
			}
		}
		return false;
	}


	/**
	 * <p>
	 * This method disables pairs of Channels and Levels.
	 * If you want to disable DEBUG-logging for Crawler-Channel,
	 * then you can call this method like this:</p>
	 * <p>{@code Logger.getInstance().disable(Channel.Crawler, LogLevel.DEBUG)}</p>
	 * <p>You can also disable single Channels or Level if you use the .NONE - Type of the Enum.</p>
	 *
	 * @param channel The specific channel you want to disable logging
	 * @param level   The specific level you want to disable logging
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void disable(Channel channel, LogLevel level)
	{
		if(level.equals(LogLevel.NONE) && !channel.equals(Channel.NONE))
		{
			disableChannel(channel);
		}
		if(channel.equals(Channel.NONE) && !level.equals(LogLevel.NONE))
		{
			disableLevel(level);
		}

		if(!level.equals(LogLevel.NONE) && !channel.equals(Channel.NONE))
		{
			disableLevel(level);
			disableChannel(channel);
		}
	}


	/**
	 * In this Method you can DISABLE a single Level to force it NOT to log.
	 * <p>Attention: ERROR-level is ALWAYS enabled!</p>
	 *
	 * @param level the Level, e.g. DEBUG, WARNING, ...
	 */
	private void disableLevel(LogLevel level)
	{
		String success = "Logger has been DISABLED for level: '" + level.name() + "'";
		String failure = "Logger DISABLED failed for level: '" + level.name() + "' with Message: ";
		try
		{
			log(this, LogLevel.ACTIVITY, Channel.Logger, success);
			m_localLevelFlag.put(level, LevelFlag.DISABLED);
		}
		catch(Exception e)
		{
			log(this, LogLevel.ERROR, Channel.Logger, failure + e.toString());
		}
	}


	/**
	 * In this Method you can DISABLE a single Channel to force it NOT to log.
	 *
	 * @param channel the Channel, e.g. Crawler, UI, ...
	 */
	private void disableChannel(Channel channel)
	{
		String success = "Logger has been DISABLED for Channel: '" + channel.name() + "'";
		String failure = "Logger DISABLED failed for Channel: '" + channel.name() + "' with Message: ";
		try
		{
			log(this, LogLevel.ACTIVITY, channel, success);
			m_channelFlag.put(channel, ChannelFlag.DISABLED);
		}
		catch(Exception e)
		{
			log(this, LogLevel.ERROR, channel, failure + e.toString());
		}
	}


	/**
	 * This method is called by the variable 'm_hasBeenInitialized' in the initialize() method.
	 * The Function will return true, if all Channels were correctly putted into the HashTable.
	 * The Channels are by default ENABLED.
	 *
	 * @return true, if all Channels were correctly putted into the HashTable. False if not.
	 */
	private boolean enableChannel()
	{
		boolean res = false;
		for(Channel c : Channel.values())
		{
			try
			{
				m_channelFlag.put(c, ChannelFlag.ENABLED);
				res = true;
			}
			catch(Exception e)
			{
				logError(this, "FATAL Error putting Key '" + c.name() + "' to '" + ChannelFlag.ENABLED.name());
				return false;
			}
		}
		return res;
	}


	/**
	 * This method is called by the variable 'm_hasBeenInitialized' in the initialize() method.
	 * The Function will return true, if all Channels were correctly putted into the HashTable.
	 * The Channels are by default ENABLED.
	 *
	 * @return true, if all Channels were correctly putted into the HashTable. False if not.
	 */
	private boolean enableLevel()
	{
		boolean res = false;
		for(LogLevel level : LogLevel.values())
		{
			try
			{
				if(!ConfigurationManager.getInstance().isLevelDisabled(level.name()))
				{
					m_globalLevelFlag.put(level, LevelFlag.ENABLED);
					m_localLevelFlag.put(level, LevelFlag.ENABLED);
					res = true;
				}
				else
				{
					m_globalLevelFlag.put(level, LevelFlag.DISABLED);
					m_localLevelFlag.put(level, LevelFlag.DISABLED);
				}
			}
			catch(Exception e)
			{
				logError(this, "FATAL Error putting Key '" + level.name() + "' to '" + LevelFlag.ENABLED.name());
				return false;
			}
		}
		return res;
	}


	/**
	 * This method returns a String for a FileName, based on the Channel name.
	 * <p>Example</p>
	 * <p>{@code 20-04-2018-Crawler-log.txt}</p>
	 *
	 * @param channel The specific Channel
	 * @return A String of FileName, e.g.: 20-04-2018-Crawler-log.txt
	 */
	private String getFileName(Channel channel)
	{
		return getDateString() + "-"
				+ channel.name() + "-"
				+ "log.txt";
	}


	/**
	 * This method returns a FilePath-String, based on the Channel name.
	 * <p>Example</p>
	 * <p>{@code /home/ln/IdeaProjects/METLAB_News}</p>
	 *
	 * @param channel The specific Channel
	 * @return A String of FilePath, e.g.: /home/ln/IdeaProjects/METLAB_News
	 */
	private String createFilePath(Channel channel)
	{
		if(!m_hasBeenInitialized)
		{
			return System.getProperty(
					"user.dir") + File.separator
					+ "Logs" + File.separator
					+ channel.name() + File.separator
					+ getFileName(channel);
		}
		else
		{
			return FullFilePath
					+ channel.name() + File.separator
					+ getFileName(channel);
		}
	}


	/**
	 * <p>This Message will write an error message to a file.</p>
	 * <p>
	 * If the file and/or the directory is NOT found, then the method will
	 * automatically create this for you. The default directory is in METLAB_News/Logs/...
	 * </p>
	 * <p>
	 * Filenames are e.g.: 04-05-2018-WebCrawler-log.txt
	 * </p>
	 *
	 * @param sender  The source of the log
	 * @param level   The log-priority
	 * @param channel The specific channel
	 * @param msg     The log-Message
	 */
	private synchronized void writeToFile(Object sender, LogLevel level, Channel channel, String msg)
	{
		Object o = this;
		msg = msg + System.lineSeparator();

		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(createFilePath(channel))
		{{
			if(getParentFile().mkdirs())
			{
				logActivity(o, "Created File: " + getAbsolutePath());
			}
		}}, true)))
		{
			bufferedWriter.write(createLogString(sender, level, channel, msg, ++m_logCounter));
		}
		catch(IOException e)
		{
			writeToConsole(o, level, channel, "IO Exception! Error in Logger: " + e.getMessage());
		}
	}


	/**
	 * This Message will write an error message to the Console.
	 *
	 * @param sender   The source of the log
	 * @param logLevel The log-priority
	 * @param channel  The specific channel
	 * @param msg      The log-Message
	 */
	private void writeToConsole(Object sender, LogLevel logLevel, Channel channel, String msg)
	{
		System.err.println(createLogString(sender, logLevel, channel, msg, ++m_logCounter));
	}


	/**
	 * <p>
	 * This method will log with the level 'Debug'.
	 * </p>
	 * <p>
	 * It calls the private function log(Object sender, LogLevel level, Channel channel, String msg)
	 * </p>
	 * <p>
	 * You only need to call the following code:
	 * {@code}
	 * <p>
	 * Logger.getInstance.logDebug(this, "your message here...");
	 * </p>
	 * </p>
	 *
	 * @param sender The source. ALWAYS call it with 'this'
	 * @param msg    The log-message
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logDebug(Object sender, String msg)
	{
		log(sender, LogLevel.DEBUG, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}


	/**
	 * <p>
	 * This method will log with the level 'Error'.
	 * </p>
	 * <p>
	 * It calls the private function log(Object sender, LogLevel level, Channel channel, String msg)
	 * </p>
	 * <p>
	 * You only need to call the following code:
	 * {@code}
	 * <p>
	 * Logger.getInstance.logDebug(this, "your message here...");
	 * </p>
	 * </p>
	 *
	 * @param sender The source. ALWAYS call it with 'this'
	 * @param msg    The log-message
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logError(Object sender, String msg)
	{
		log(sender, LogLevel.ERROR, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}


	/**
	 * <p>
	 * This method will log with the level 'Warning'.
	 * </p>
	 * <p>
	 * It calls the private function log(Object sender, LogLevel level, Channel channel, String msg)
	 * </p>
	 * <p>
	 * You only need to call the following code:
	 * {@code}
	 * <p>
	 * Logger.getInstance.logDebug(this, "your message here...");
	 * </p>
	 * </p>
	 *
	 * @param sender The source. ALWAYS call it with 'this'
	 * @param msg    The log-message
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logWarning(Object sender, String msg)
	{
		log(sender, LogLevel.WARNING, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}


	/**
	 * <p>
	 * This method will log with the level 'Activity'.
	 * </p>
	 * <p>
	 * It calls the private function log(Object sender, LogLevel level, Channel channel, String msg)
	 * </p>
	 * <p>
	 * You only need to call the following code:
	 * {@code}
	 * <p>
	 * Logger.getInstance.logDebug(this, "your message here...");
	 * </p>
	 * </p>
	 *
	 * @param sender The source. ALWAYS call it with 'this'
	 * @param msg    The log-message
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logActivity(Object sender, String msg)
	{
		log(sender, LogLevel.ACTIVITY, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}


	/**
	 * <p>
	 * This method will log with the level 'Info'.
	 * </p>
	 * <p>
	 * It calls the private function log(Object sender, LogLevel level, Channel channel, String msg)
	 * </p>
	 * <p>
	 * You only need to call the following code:
	 * {@code}
	 * <p>
	 * Logger.getInstance.logInfo(this, "your message here...");
	 * </p>
	 * </p>
	 *
	 * @param sender The source. ALWAYS call it with 'this'
	 * @param msg    The log-message
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logInfo(Object sender, String msg)
	{
		log(sender, LogLevel.INFO, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}


	/**
	 * <p>
	 * This method will register the Class from where you call it.
	 * It uses a HashTable to put the different Classes to categories.
	 * </p>
	 * <p>
	 * The categories are e.g. 'Logger', 'Crawler', 'UI', 'ConfigurationManager', etc.
	 * </p>
	 * For more categories check the documentation of Channel: {@link Channel}
	 * <p>ALWAYS call the register method with the keyword 'this'.</p>
	 * Example:
	 * {@code}
	 * <p>Logger.getInstance().register(Logger.Channel.Crawler, this);</p>
	 *
	 * @param sender  ALWAYS use 'CLASSNAME.class' in calls. It is the source of the called class.
	 * @param channel The Channel you want to put this class into
	 */
	@SuppressWarnings("WeakerAccess")
	public void register(Class sender, Channel channel)
	{
		if(!m_classList.containsKey(sender.getCanonicalName()))
		{
			String res = null;
			String pre = " '";
			String pst = "' ";
			try
			{
				m_classList.put(sender.getCanonicalName(), channel);
				res = "Registration successful:";
			}
			catch(Exception e)
			{
				res = "Registration FAILED: " + e.toString();
			}
			finally
			{
				String msg = res + pre + sender.getCanonicalName() + pst + "->" + pre + channel.name() + pst + "| HashTable size: " + m_classList.size();
				log(this, LogLevel.REGISTRATION, m_classList.get(sender.getCanonicalName()), msg);
			}
		}
		else
		{
			logWarning(this, "Class is already registered: " + sender.getCanonicalName() + " with Channel: " + channel);
		}
	}


	/**
	 * This Method logs the specific message to a log-file.
	 * The file is found in the specific channel-folder with the name of the
	 * current date (e.g. 05-04-2018-Crawler-log.txt)
	 * Newline is automatically appended when logging toFile and toConsole
	 * </p>
	 * Example:
	 * <p>
	 * {@code
	 * Logger.getInstance.log(Logger.Channel.WebCrawler,
	 * Logger.Priority.DEBUG,
	 * "Enter your log-message here...");
	 * }
	 *
	 * @param sender  the Channel from where you call the log-method
	 * @param level   the priority you want to log with (DEBUG, WARNING, ERROR)
	 * @param channel the specific channel
	 * @param msg     the log-message
	 */
	@SuppressWarnings("WeakerAccess")
	private void log(Object sender, LogLevel level, Channel channel, String msg)
	{
		if(channel == null)
		{
			channel = Channel.UNREGISTERED_CHANNEL;
		}

		boolean loggerLogic = ((level.equals(LogLevel.ERROR))
				|| ((!isLevelForbidden(level) && !m_localLevelFlag.contains(level))
				&& !isChannelForbidden(channel)));

		if(loggerLogic)
		{
			switch(LogDestination)
			{
				case "ToFile":
					writeToFile(sender, level, channel, msg);
					break;
				case "ToConsole":
					writeToConsole(sender, level, channel, msg);
			}
		}
	}


	private static Logger                          m_instance;
	private        boolean                         m_hasBeenInitialized = false;
	private        int                             m_logCounter         = 0;
	private        String                          LogDestination       = "ToConsole";
	private        Hashtable<Object, Channel>      m_classList          = new Hashtable<>();
	private        Hashtable<Channel, ChannelFlag> m_channelFlag        = new Hashtable<>();
	private        Hashtable<LogLevel, LevelFlag>  m_globalLevelFlag    = new Hashtable<>();
	private        Hashtable<LogLevel, LevelFlag>  m_localLevelFlag     = new Hashtable<>();
	private        String                          FullFilePath         = (System.getProperty(
			"user.dir")) + File.separator + "Logs" + File.separator;
}