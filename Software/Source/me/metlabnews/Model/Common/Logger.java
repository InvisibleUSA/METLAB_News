package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * This Class is for logging the whole error's and debug outputs of the application.
 * It will be written to a local .txt file.
 * The File and the Subfolders are created automatically.
 */
public class Logger
{
	private        int    m_logCounterTotal = 0;
	/**
	 * Counter Variable
	 */
	private int m_logCounterTotal = 0;


	/**
	 * Member Variable for Singleton
	 */
	private static Logger instance;


	/**
	 * Private Constructor of Singleton
	 */
	private Logger()
	{
	}


	/**
	 * Singleton call
	 * @return instance of this Class
	 */
	public static synchronized Logger getInstance()
	{
		if(Logger.instance == null)
		{
			Logger.instance = new Logger();
		}
		return Logger.instance;
	}


	/**
	 * This Enum is for the channels to log the output message to the specific channel.
	 */
	public enum enum_channel
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
	public enum enum_logType
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
	public enum enum_logPriority
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
	private String createLogString(enum_channel channel, int cntr, enum_logPriority priority, String msg)
	{
		return ("#" + cntr + " | " + priority.name() + " | " + this.getTimeStamp() + " : " + msg + "\n");
	}


	/**
	 * This Method will return the value of the filtered Priority. If e.g. the DEBUG-Priority
	 * is filtered, then every called DEBUG-calls will be ignored.
	 *
	 * @param priority the Priority (e.g. DEBUG, WARNING, ERROR)
	 * @return True = Filtered
	 */
	private boolean isPriorityFiltered(enum_logPriority priority)
	{
		switch(priority)
		{
			case DEBUG:
				return ConfigurationManager.getInstance().getFilteredPriorities("DEBUG");
			case WARNING:
				return ConfigurationManager.getInstance().getFilteredPriorities("WARNING");
			case ERROR:
				return ConfigurationManager.getInstance().getFilteredPriorities("ERROR");
			default:
				return true;
		}
	}


	/**
	 * This Message will write ein Error-Message to a File.
	 * @param channel The specified Channel
	 * @param cntr The internal counter
	 * @param priority The log-priority
	 * @param msg The log-Message
	 */
	private void writeToFile(enum_channel channel, int cntr, enum_logPriority priority, String msg)
	{
		if(msg != null)
		{
			String fileName     = this.getDateString() + "-" + channel.name() + "-" + "log.txt";
			String fullFilePath = ConfigurationManager.getInstance().getLoggerLogFilePath() + channel.name() + "\\" + fileName;

			File file = new File(fullFilePath);
			file.getParentFile().mkdirs();

			try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true)))
			{
				bw.write(this.createLogString(channel, cntr, priority, msg));
			}
			catch(IOException e)
			{
				System.err.println(e.getMessage());
			}
		}
	}


	/**
	 * This Message will write ein Error-Message to the Console.
	 * @param channel The specified Channel
	 * @param cntr The internal counter
	 * @param priority The log-priority
	 * @param msg The log-Message
	 */
	private void writeToConsole(enum_channel channel, int cntr, enum_logPriority priority, String msg)
	{
		if(msg != null)
		{
			System.err.println(this.createLogString(channel, ++this.m_logCounterTotal, priority, msg));
		}
	}


	/**
	 * This Message will write ein Error-Message to the Database.
	 * @param channel The specified Channel
	 * @param cntr The internal counter
	 * @param priority The log-priority
	 * @param msg The log-Message
	 */
	private void writeToDatabase(enum_channel channel, int cntr, enum_logPriority priority, String msg)
	{
		// TODO
		// not implemented
	}


	/***
	 * This Method logs the specific message to a log-file.
	 * The file is found in the specific channel-folder with the name of the
	 * current date (e.g. 05-04-2018-Crawler-log.txt)
	 * @param channel       the Channel from where you call the log-method
	 * @param msg           the log-message
	 * @param priority   the priority you want to log with (DEBUG, WARNING, ERROR)
	 */
	public void log(enum_channel channel, enum_logPriority priority, enum_logType type, String msg)
	{
		if(msg != null)
		{
			if(!this.isPriorityFiltered(priority))
			{
				switch(type)
				{
					case ToFile:
						this.writeToFile(channel, ++this.m_logCounterTotal, priority, msg);
						break;
					case ToConsole:
						this.writeToConsole(channel, ++this.m_logCounterTotal, priority, msg);
						break;
					case ToDatabase:
						this.writeToDatabase(channel, ++this.m_logCounterTotal, priority, msg);
						break;
				}
			}
		}
	}



}