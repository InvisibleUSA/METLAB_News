package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;


// this.getClass().getCanonicalName()


/**
 * This Class is for logging the whole error's and debug outputs of the application.
 * It will be written to a local .txt file.
 * The File and the Subfolders are created automatically.
 */
public class Logger implements IResource
{
	/**
	 * Singleton call
	 *
	 * @return instance of this Class
	 */
	public static synchronized Logger getInstance()
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
	 * This Enum is for the channels to log the output message to the specific channel.
	 */
	public enum Channel
	{
		ConfigurationManager,
		ClippingDaemon,
		Crawler,
		UI,
		Mail,
		XmlTag,
		Logger,
		DataBase
	}


	/**
	 * This Enum is for the source you want to log the Message in.
	 * You can decide between:
	 * - To File (saved under: (System.getProperty("user.dir"))//Logs//...)
	 * - To Console
	 * - To Database
	 */
	public enum LogType
	{
		ToFile,
		ToConsole,
		ToDatabase
	}


	/**
	 * This Enum represents the Priority of the logged message. It will be possible to set the
	 * internal Priority higher than the called Priority. This allows the user to filter some logs.
	 * If e.g. the Priority of the internal logger is higher than the called Priority, than the message
	 * will NOT be logged.
	 */
	public enum LogPriority
	{
		DEBUG,
		WARNING,
		ERROR
	}


	/**
	 * This Method returns a simple TimeStamp for the logger.
	 *
	 * @return Timestamp in Format: "dd.MM.yyyy [HH:mm:ss]"
	 */
	private String getTimeStamp()
	{
		return new SimpleDateFormat("[HH:mm:ss]").format(Calendar.getInstance().getTime());
	}


	/**
	 * This Method returns a Date to check if a file already exists
	 */
	private String getDateString()
	{
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}


	/**
	 * this Method returns the number of the total logged elements as Number
	 */
	private int getLogCounterTotal()
	{
		return m_logCounterTotal;
	}


	/**
	 * This Method will return the final string which is logged to the target
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
	 * This Message will write an Error-Message to a File.
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
			String fileName     = this.getDateString() + "-" + channel.name() + "-" + "log.txt";
			String fullFilePath = ConfigurationManager.getInstance().getLoggerLogFilePath() + channel.name() + "\\" + fileName;

			File file = new File(fullFilePath);
			file.getParentFile().mkdirs();

			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true)))
			{
				bufferedWriter.write(createLogString(channel, cntr, priority, msg));
			}
			catch(IOException e)
			{
				System.err.println("Error in Logger: " + e.getMessage());
			}
		}
	}


	/**
	 * This Message will write ein Error-Message to the Console.
	 *
	 * @param channel  The specified Channel
	 * @param cntr     The internal counter
	 * @param priority The log-priority
	 * @param msg      The log-Message
	 */
	private void writeToConsole(Channel channel, int cntr, LogPriority priority, String msg)
	{
		if(msg != null)
		{
			System.err.println(this.createLogString(channel, ++this.m_logCounterTotal, priority, msg));
		}
	}


	/**
	 * This Message will write ein Error-Message to the Database.
	 *
	 * @param channel  The specified Channel
	 * @param cntr     The internal counter
	 * @param priority The log-priority
	 * @param msg      The log-Message
	 */
	// TODO: WHAT THE FUCKING FUCK?!
	private void writeToDatabase(Channel channel, int cntr, LogPriority priority, String msg)
	{
		java.sql.Connection con      = null;
		PreparedStatement   pst      = null;
		ResultSet           rs       = null;
		String              url      = "jdbc:mariadb://46.101.223.95/METLAB_LOGS";
		String              user     = "test";
		String              password = "test";

		try
		{
			con = DriverManager.getConnection(url, user, password);
			Statement st = (Statement)con.createStatement();

			st.executeUpdate("INSERT INTO LOG VALUES " +
					                 "(NULL, " +
					                 "'" + String.format(this.getTimeStamp().toString()) + "', " +
					                 "'" + String.format(channel.toString()) + "', " +
					                 "'" + String.format(priority.toString()) + "', " +
					                 "'" + String.format(msg) + "'" +
					                 ")");
			con.close();
		}

		catch(Exception e)
		{
			System.err.println(e);
		}
	}


	/***
	 * This Method logs the specific message to a log-file.
	 * The file is found in the specific channel-folder with the name of the
	 * current date (e.g. 05-04-2018-Crawler-log.txt)
	 * Newline is automatically appended when logging toFile and toConsole
	 * @param channel       the Channel from where you call the log-method
	 * @param priority   the priority you want to log with (DEBUG, WARNING, ERROR)
	 * @param msg           the log-message
	 */
	public void log(Channel channel, LogPriority priority, String msg)
	{
		if(msg != null)
		{
			if(isPriorityAllowed(priority))
			{
				switch(String.format(ConfigurationManager.getInstance().getLogType()))
				{
					case "ToFile":
						this.writeToFile(channel, ++m_logCounterTotal, priority, msg);
						break;
					case "ToConsole":
						this.writeToConsole(channel, ++m_logCounterTotal, priority, msg);
						break;
					case "ToDatabase":
						this.writeToDatabase(channel, ++m_logCounterTotal, priority, msg);
						break;
					default:
						break;
				}
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