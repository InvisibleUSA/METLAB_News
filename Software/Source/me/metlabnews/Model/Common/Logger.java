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
 * This Class is for logging the whole error's and debug outputs of the application.
 * <p>
 * You can also log user controls and registers to database. The log-files are stored
 * in the application folder in ...\Logs\
 * The File and the subfolder are created automatically.
 * </p>
 * Example:
 * <p>
 * {@code
 * Logger.getInstance.log(Logger.Channel.WebCrawler,
 * Logger.Priority.DEBUG,
 * "Enter your log-message here...");
 * }
 *
 * @author Tobias Reis
 * @version 1.4
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
		ConfigurationManager config = ConfigurationManager.getInstance();
		m_debugIsAllowed = config.getFilteredPriorities("DEBUG");
		m_warningIsAllowed = config.getFilteredPriorities("WARNING");
		m_errorIsAllowed = config.getFilteredPriorities("ERROR");

		m_hasBeenInitialized = true;
	}

	@Override
	public void close()
	{

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
		ConfigurationManager,
		ClippingDaemon,
		Crawler,
		UI,
		Presenter,
		ResourceManager,
		Mail,
		XMLTag,
		Logger,
		RDBMS,
		BaseX,
		UNREGISTERED_CHANNEL
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
		DEBUG,
		WARNING,
		ERROR,
		REGISTRATION,
		ACTIVITY
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
	 * <p>
	 * Example:
	 * </p>
	 * {@code
	 * #1 | ERROR | [17:02:02] : java.lang.NumberFormatException: null
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
		return ("#" + counter + " [" + level.name() + "] " + sender.getClass().getSimpleName() + " in " + channel.name() + " " + getTimeStamp() + ": " + msg + "\n");
	}


	/**
	 * This Method will return the value of the forbidden Priority. If e.g. the DEBUG-Priority
	 * is forbidden, then every called DEBUG-calls will be ignored.
	 *
	 * @param type the Priority (e.g. DEBUG, WARNING, ERROR)
	 * @return true is forbidden
	 */
	private boolean isLevelForbidden(LogLevel type)
	{
		if(!m_hasBeenInitialized) // return default values from code if not initialized
		{
			switch(type)
			{
				case DEBUG:
					return m_debugIsAllowed;
				case WARNING:
					return m_warningIsAllowed;
				case ERROR:
					return m_errorIsAllowed;
				default:
					return false;
			}
		}
		else
		{
			switch(type)
			{
				case DEBUG:
					return ConfigurationManager.getInstance().getFilteredPriorities(type.name());
				case WARNING:
					return m_warningIsAllowed;
				case ERROR:
					return m_errorIsAllowed;
				default:
					return false;
			}
		}
	}


	/**
	 * This Message will write an error message to a file.
	 *
	 * @param sender   The source of the log
	 * @param logLevel The log-priority
	 * @param channel  The specific channel
	 * @param msg      The log-Message
	 */
	private synchronized void writeToFile(Object sender, LogLevel logLevel, Channel channel, String msg)
	{
		if(msg != null && channel != null)
		{
			String fullFilePath;
			String fileName = getDateString() + "-" + channel.name() + "-" + "log.txt"; // e.g.: 04-05-2018-WebCrawler-log.txt

			if(!m_hasBeenInitialized)
			{
				fullFilePath = System.getProperty(
						"user.dir") + File.separator + "Logs" + File.separator + channel.name() + File.separator + fileName; // by default: ..\Logs\..
			}
			else
			{
				fullFilePath = ConfigurationManager.getInstance().getLoggerLogFilePath() + channel.name() + File.separator + fileName;
			}


			File file = new File(fullFilePath);
			if(file.getParentFile().mkdirs())
			{
				logActivity(this, "Created directory: " + fullFilePath);
			}

			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true)))
			{
				bufferedWriter.write(createLogString(sender, logLevel, channel, msg, ++m_logCounterTotal));
			}
			catch(IOException e)
			{
				System.err.println("IO Exception! Error in Logger: " + e.getMessage());
			}
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
		System.err.println(createLogString(sender, logLevel, channel, msg, ++m_logCounterTotal));
	}


	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logDebug(Object sender, String msg)
	{
		log(sender, LogLevel.DEBUG, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logError(Object sender, String msg)
	{
		log(sender, LogLevel.ERROR, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logWarning(Object sender, String msg)
	{
		log(sender, LogLevel.WARNING, m_classList.get(sender.getClass().getCanonicalName()), msg);
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	public void logActivity(Object sender, String msg)
	{
		log(sender, LogLevel.ACTIVITY, m_classList.get(sender.getClass().getCanonicalName()), msg);
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
	public void register(java.lang.Class sender, Channel channel)
	{
		if(sender != null)
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
				String msg = res + pre + sender.getCanonicalName() + pst + "->" + pre + channel.name() + pst + "| HashTableSize: " + m_classList.size();
				log(this, LogLevel.REGISTRATION, m_classList.get(sender.getCanonicalName()), msg);
			}
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
	private void log(Object sender, LogLevel level, Channel channel, String msg)
	{
		boolean isLevelForbidden = false; // log everything by default
		String  logType;

		if(channel == null)
		{
			channel = Channel.UNREGISTERED_CHANNEL;
		}

		if(!m_hasBeenInitialized)
		{
			logType = "ToConsole"; // log ToConsole by default
		}
		else
		{
			isLevelForbidden = isLevelForbidden(level); // if typeForbidden = true -> NO logging
			logType = ConfigurationManager.getInstance().getLogDestination();
		}

		if(!isLevelForbidden)
		{
			switch(logType)
			{
				case "ToFile":
					writeToFile(sender, level, channel, msg);
					break;
				case "ToConsole":
					writeToConsole(sender, level, channel, msg);
					break;
				default:
					break;
			}
		}
	}


	private static Logger                     m_instance;
	private        boolean                    m_hasBeenInitialized;
	private        int                        m_logCounterTotal  = 0;
	private        boolean                    m_debugIsAllowed   = true;
	private        boolean                    m_warningIsAllowed = true;
	private        boolean                    m_errorIsAllowed   = true;
	private        Hashtable<Object, Channel> m_classList        = new Hashtable<>();
}