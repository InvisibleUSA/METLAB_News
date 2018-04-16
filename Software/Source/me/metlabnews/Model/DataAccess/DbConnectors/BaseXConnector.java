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



public class BaseXConnector
{
	private       String m_username = "admin";
	private       String m_password = "admin";
	final private String m_host     = "localhost";
	private String m_path;
	private String m_dbName = "ClippingDB";
	private int    m_port   = 1984;
	private ClientSession[] m_sessions;
	private int m_currSessionNum = 0;

	public BaseXConnector()
	{
		loadConfig();
		try
		{
			startServer();

			connectClients();
			Logger.getInstance().log(Logger.Channel.DataBase, Logger.LogPriority.DEBUG, Logger.LogType.ToFile,
			                         query(new Check(m_dbName)));
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.Channel.DataBase, Logger.LogPriority.ERROR, Logger.LogType.ToFile,
			                         "BaseX Error: " + e.toString());
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

	private ClientSession getSession()
	{
		if(m_currSessionNum == 9)
		{
			m_currSessionNum = 0;
		}
		else
		{
			m_currSessionNum++;
		}
		return m_sessions[m_currSessionNum];
	}

	String query(String q) throws IOException
	{
		ClientSession session = getSession();
		String        result  = session.query(q).execute();

		return result;
	}

	String query(Command cmd) throws IOException
	{
		ClientSession session = getSession();
		String        result  = session.execute(cmd);

		return result;
	}

	private void connectClients() throws IOException
	{
		m_sessions = new ClientSession[10];
		for(int i = 0; i < 10; i++)
		{
			m_sessions[i] = new ClientSession(m_host, m_port, m_username, m_password);
			m_sessions[i].execute(new Open(m_dbName));
		}
	}

	private void disconnect() throws IOException
	{
		for(ClientSession cs : m_sessions)
		{
			cs.execute(new Close());
		}
	}

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
			Logger.getInstance().log(Logger.Channel.DataBase, Logger.LogPriority.ERROR, Logger.LogType.ToFile,
			                         "BaseX Error: " + e.toString());
		}
	}
}
