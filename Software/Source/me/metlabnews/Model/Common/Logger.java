package me.metlabnews.Model.Common;

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
	private int m_logCounterTotal = 0;
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
		ClippingDaemon,
		Crawler,
		UI,
		Mail,
		XmlTag,
		Logger,
		DataBase
	}


	/**
	 * This Enum is for logging to Console or to File
	 */
	public enum enum_logType
	{
		ToFile,
		ToConsole
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
	 * This Method will write the message to a .txt-file
	 *
	 * @param msg the Message you want to write to the file
	 */
	private void writeToFile(enum_channel channel, int cntr, enum_logPriority priority, String msg)
	{
		if(msg != null)
		{
			String fileName     = this.getDateString() + "-" + channel.name() + "-" + "log.txt";
			String fullFilePath = "C:\\SWE-LOGS\\" + channel.name() + "\\" + fileName;

			File file = new File(fullFilePath);
			file.getParentFile().mkdirs();

			try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true)))
			{
				bw.write("#" + cntr + " | " + priority.name() + " | " + this.getTimeStamp() + " : " + msg + "\n");
			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
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
			// TO DO
			// ...
			// ...



			switch(type)
			{
				case ToFile:
					this.writeToFile(channel, ++this.m_logCounterTotal, priority, msg);
					break;
				case ToConsole:
					// not implemented - coming soon.... TO DO
					break;
			}
		}
	}


}