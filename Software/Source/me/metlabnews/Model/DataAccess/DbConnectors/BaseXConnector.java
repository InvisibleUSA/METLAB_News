package me.metlabnews.Model.DataAccess.DbConnectors;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.core.cmd.Check;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.Open;
import org.basex.server.ClientSession;

import java.io.IOException;
import java.util.Properties;



/**
 * class used indirectly by QueryBase to access the BaseX database. Responsible for starting the BaseX server
 * and requesting data from it.
 *
 * @author Erik
 * @version 1.0
 */
class BaseXConnector
{
	private       String          m_username       = "admin";
	private       String          m_password       = "admin";
	final private String          m_host           = "localhost";
	private       String          m_path;
	private       String          m_dbName         = "ClippingDB";
	private       int             m_port           = 1984;
	private       ClientSession[] m_sessions;
	private       boolean[]       m_sessionInUse;
	private       int             m_currSessionNum = 0;

	/**
	 * Starts the BaseX Server in a separate process and initializes ClientSessions as well as settings
	 * Errors are logged to DataBase channel
	 */
	BaseXConnector()
	{
		Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.DEBUG, "Starting BaseX");
		loadConfig();
		try
		{
			startServer();

			connectClients();
			Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.DEBUG, query(new Check(m_dbName)));
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.ERROR, "BaseX Error: " + e.toString());
		}
	}

	private void startServer() throws IOException
	{
		Properties props = System.getProperties();
		props.setProperty("org.basex.path", m_path);

		BaseXServer.start(m_port, "-n" + m_host + " -p" + m_port + " -S");
	}

	private void loadConfig()
	{
		ConfigurationManager cm = ConfigurationManager.getInstance();
		m_password = cm.getBaseXPassword();
		m_username = cm.getBaseXUsername();
		m_path = cm.getBaseXPath();
	}

	private synchronized int getSession()
	{
		for(int iteration = 0; iteration < 10; iteration++)
		{
			m_currSessionNum = (m_currSessionNum + 1) % 10;
			if(!m_sessionInUse[m_currSessionNum])
			{
				m_sessionInUse[m_currSessionNum] = true;
				return m_currSessionNum;
			}
		}
		return -1;
	}

	/**
	 * executes a query using an unused ClientSession
	 *
	 * @param q query to execute
	 * @return XML-Data the query returned
	 * @throws IOException happens e.g. when DB is down or no client session is available
	 */
	@Deprecated
	String query(String q) throws IOException
	{
		int sessionnum = getSession();
		if(sessionnum == -1)
		{
			throw new IOException("All client sessions in use");
		}
		ClientSession session = m_sessions[sessionnum];

		String result = session.query(q).execute();

		m_sessionInUse[sessionnum] = false;
		return result;
	}

	/**
	 * executes a command using an unused ClientSession
	 *
	 * @param cmd command to execute
	 * @return XML-Data the query returned
	 * @throws IOException happens e.g. when DB is down or no client session is available
	 */
	String query(Command cmd) throws IOException
	{
		int sessionnum = getSession();
		if(sessionnum == -1)
		{
			throw new IOException("All client sessions in use");
		}
		ClientSession session = m_sessions[sessionnum];

		String result = session.execute(cmd);

		m_sessionInUse[sessionnum] = false;
		return result;
	}

	private void connectClients() throws IOException
	{
		m_sessions = new ClientSession[10];
		m_sessionInUse = new boolean[10];
		for(int i = 0; i < 10; i++)
		{
			m_sessions[i] = new ClientSession(m_host, m_port, m_username, m_password);
			m_sessions[i].execute(new Open(m_dbName));
			m_sessionInUse[i] = false;
		}
	}

	private void disconnect() throws IOException
	{
		for(ClientSession cs : m_sessions)
		{
			cs.execute(new Close());
		}
	}

	/**
	 * stops the server that runs in a separate process and closes all sessions connected to it
	 * Errors are logged to DataBase channel
	 */
	public void stop()
	{
		try
		{
			disconnect();
			for(int i = 0; i < 10; i++)
			{
				m_sessions[i].close();
			}
			BaseXServer.stop(m_port, m_port);
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.ERROR, "BaseX Error: " + e.toString());
			
		}
	}
}
