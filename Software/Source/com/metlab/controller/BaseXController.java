package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.core.cmd.Check;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.Open;
import org.basex.server.ClientSession;

import java.io.IOException;



public class BaseXController
{

	private BaseXServer server;

	final private String username    = "admin";
	final private String pw          = "admin";
	final private String hostaddress = "localhost";
	final private String dbName      = "ClippingDB";
	final private int    port        = 1984;
	private static BaseXController m_bxc;
	private        ClientSession   m_session;

	private BaseXController()
	{
		try
		{
			server = new BaseXServer();
			System.out.println(new Check(dbName).execute(server.context));
			m_session = new ClientSession(hostaddress, port, username, pw);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized BaseXController getInstance()
	{
		if(m_bxc == null)
		{
			m_bxc = new BaseXController();
		}
		return m_bxc;
	}

	public synchronized String execute(Command c)
	{
		try
		{
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			session.execute(new Open(dbName));
			String result = session.execute(c);
			session.execute(new Close());
			session.close();
			return "result: " + result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return "ERROR " + e.getMessage();
		}
	}

	public synchronized String[] execute(Command[] cmd)
	{
		try
		{
			m_session.execute(new Open(dbName));
			String[]      result  = new String[cmd.length];
			int           i       = 0;
			for(Command c : cmd)
			{
				result[i++] = m_session.execute(c);
			}
			m_session.execute(new Close());
			return result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public synchronized String execute(String command)
	{
		try
		{
			m_session.execute(new Open(dbName));
			String result = m_session.execute(command);
			m_session.execute(new Close());
			return result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public synchronized String query(String xQuery)
	{
		try
		{
			m_session.execute(new Open(dbName));
			String result = m_session.query(xQuery).execute();
			m_session.execute(new Close());
			return result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void stop()
	{
		try
		{
			m_session.close();
			server.stop();
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
}