package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;



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
		XmlTag,
		Logger,
		RDBMS,
		BaseX
	}


	/**
	 * <p>
	 * This Enum represents the priority of the logged message. It will be possible to set the
	 * internal Priority higher than the called Priority. This allows the user to filter some logs.
	 * If e.g. the Priority of the internal logger is higher than the called Priority, than the message
	 * will NOT be logged.
	 * </p>
	 */
	public enum LogPriority
	{
		DEBUG,
		WARNING,
		ERROR
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
	 * @param cntr     The Counter of the logs
	 * @param priority The priority of the logs
	 * @param msg      The Message you want to log
	 * @return A parsed String to log
	 */
	private String createLogString(Channel channel, int cntr, LogPriority priority, String msg)
	{
		return ("#" + cntr + " | " + priority.name() + " | " + getTimeStamp() + " : " + msg + "\n");
	}


	/**
	 * This Method will return the value of the filtered Priority. If e.g. the DEBUG-Priority
	 * is filtered, then every called DEBUG-calls will be ignored.
	 *
	 * @param priority the Priority (e.g. DEBUG, WARNING, ERROR)
	 * @return True = Filtered
	 */
	private boolean isPriorityAllowed(LogPriority priority)
	{
		switch(priority)
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


	/**
	 * This Message will write an error message to a file.
	 *
	 * @param channel  The specified Channel
	 * @param cntr     The internal counter
	 * @param priority The log-priority
	 * @param msg      The log-Message
	 */
	private synchronized void writeToFile(Channel channel, int cntr, LogPriority priority, String msg)
	{
		if(msg != null)
		{
			String fullFilePath;
			String fileName = getDateString() + "-" + channel.name() + "-" + "log.txt"; // e.g.: 04-05-2018-WebCrawler-log.txt

			if(!m_hasBeenInitialized)
			{
				fullFilePath = File.separator + "Logs" + File.separator; // by default: ..\Logs\..
			}
			else
			{
				fullFilePath = ConfigurationManager.getInstance().getLoggerLogFilePath() + channel.name() + File.separator + fileName;
			}

			File file = new File(fullFilePath);
			file.getParentFile().mkdirs();

			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true)))
			{
				bufferedWriter.write(createLogString(channel, cntr, priority, msg));
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
	 * @param channel  The specified Channel
	 * @param priority The log-priority
	 * @param msg      The log-Message
	 */
	private void writeToConsole(Channel channel, LogPriority priority, String msg)
	{
		System.err.println(createLogString(channel, ++m_logCounterTotal, priority, msg));
	}


	/**
	 * This Message will write ein error message to the database.
	 *
	 * @param channel  The specified Channel
	 * @param priority The log-priority
	 * @param msg      The log-Message
	 */
	private void writeToDatabase(Channel channel, LogPriority priority, String msg)
	{
		java.sql.Connection con;
		String              url      = "jdbc:mariadb://46.101.223.95/METLAB_LOGS";
		String              user     = "test";
		String              password = "test";

		try
		{
			con = DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();

			st.executeUpdate("INSERT INTO LOG VALUES " +
					                 "(NULL, " +
					                 "'" + channel.toString() + "', " +
					                 "'" + priority.toString() + "', " +
					                 "'" + msg + "'" +
					                 ")");
			con.close();
		}
		catch(Exception e)
		{
			System.err.println("Error in Logger @ writeToDatabase() : " + e.toString());
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
	 * @param channel  the Channel from where you call the log-method
	 * @param priority the priority you want to log with (DEBUG, WARNING, ERROR)
	 * @param msg      the log-message
	 */
	public void log(Channel channel, LogPriority priority, String msg)
	{
		boolean isPriorityAllowed;
		String  typeDefault;

		if(!m_hasBeenInitialized)
		{
			isPriorityAllowed = true; // log everything by default
			typeDefault = "ToConsole"; // log ToConsole by default
		}
		else
		{
			isPriorityAllowed = isPriorityAllowed(priority);
			typeDefault = ConfigurationManager.getInstance().getLogType();
		}

		if(isPriorityAllowed)
		{
			switch(typeDefault)
			{
				case "ToFile":
					writeToFile(channel, ++m_logCounterTotal, priority, msg);
					break;
				case "ToConsole":
					writeToConsole(channel, priority, msg);
					break;
				case "ToDatabase":
					writeToDatabase(channel, priority, msg);
					break;
				default:
					break;
			}
		}
	}



	private        int     m_logCounterTotal  = 0;
	private static Logger  m_instance;
	private        boolean m_hasBeenInitialized;
	private        boolean m_debugIsAllowed   = true;
	private        boolean m_warningIsAllowed = true;
	private        boolean m_errorIsAllowed   = true;
}