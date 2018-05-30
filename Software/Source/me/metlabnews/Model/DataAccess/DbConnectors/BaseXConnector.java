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
import java.time.LocalDateTime;
import java.util.Properties;



/**
 * class used indirectly by BaseXQueryBase to access the DocDBMS database. Responsible for starting the DocDBMS server
 * and requesting data from it.
 *
 * @author Erik
 * @version 1.0
 */
public class BaseXConnector
{
	static
	{
		Logger.getInstance().register(BaseXConnector.class, Logger.Channel.DocDBMS);
	}

	private       String          m_username        = "admin";
	private       String          m_password        = "admin";
	final private String          m_host            = "127.0.0.1";
	private       String          m_path;
	private       String          m_dbName          = "ClippingDB";
	private       int             m_port            = 1984;
	private       ClientSession[] m_sessions;
	private       boolean[]       m_sessionInUse;
	private       int             m_currSessionNum  = 0;
	private       BaseXServer     m_server;
	private       boolean         m_isServerRunning = true;

	private static final int CONNECTIONS = 10;

	/**
	 * Starts the DocDBMS Server in a separate process and initializes ClientSessions as well as settings
	 * Errors are logged to DataBase channel
	 */
	BaseXConnector()
	{
		Logger.getInstance().logDebug(this, "Starting DocDBMS");
		loadConfig();
		try
		{
			startServer();
			Logger.getInstance().logDebug(this, new Check(m_dbName).execute(m_server.context));
			connectClients();
		}
		catch(IOException e)
		{
			Logger.getInstance().logError(this, "DocDBMS Error: " + e.toString());
			m_isServerRunning = false;
		}
	}

	private void startServer() throws IOException
	{
		Properties props = System.getProperties();
		props.setProperty("org.basex.path", m_path);

		m_server = new BaseXServer();
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
		for(int iteration = 0; iteration < CONNECTIONS; iteration++)
		{
			m_currSessionNum = (m_currSessionNum + 1) % CONNECTIONS;
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
		if(!m_isServerRunning)
		{
			throw new IOException("Server not started");
		}
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
	public String query(Command cmd) throws IOException
	{
		if(!m_isServerRunning)
		{
			throw new IOException("Server not started");
		}
		int sessionnum = getSession();
		if(sessionnum == -1)
		{
			throw new IOException("All client sessions in use");
		}
		ClientSession session = m_sessions[sessionnum];

		try
		{
			String result = session.execute(cmd);
			Logger.getInstance().logDebug(this, "new query: " + cmd.toString());
			Logger.getInstance().logDebug(this, "result: " + result);

			return result;
		}
		finally
		{
			m_sessionInUse[sessionnum] = false;
		}
	}

	private void connectClients() throws IOException
	{
		m_sessions = new ClientSession[CONNECTIONS];
		m_sessionInUse = new boolean[CONNECTIONS];
		for(int i = 0; i < CONNECTIONS; i++)
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
		if(!m_isServerRunning)
		{
			Logger.getInstance().logInfo(this, "BaseX server already stopped");
			return;
		}

		try
		{
			disconnect();
			for(int i = 0; i < CONNECTIONS; i++)
			{
				m_sessions[i].close();
			}
			m_server.stop();
		}
		catch(IOException e)
		{
			Logger.getInstance().logError(this, "DocDBMS Error: " + e.toString());
		}
	}
}
