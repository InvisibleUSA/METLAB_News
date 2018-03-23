package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.cmd.Check;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;
import org.basex.server.ClientSession;

import java.io.IOException;



public class BaseXController
{

	private BaseXServer server;

	private String username    = "admin";
	private String pw          = "admin";
	private String hostaddress = "localhost";
	private String dbName      = "ClippingDB";
	private int    port        = 1984;
	private static BaseXController m_bxc;

	private BaseXController()
	{
		try
		{
			server = new BaseXServer();
			System.out.println(new Check(dbName).execute(server.context));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static BaseXController getInstance()
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
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			session.execute(new Open(dbName));
			String[]      result  = new String[cmd.length];
			int           i       = 0;
			for(Command c : cmd)
			{
				result[i++] = session.execute(c);
			}
			session.execute(new Close());
			session.close();
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
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			session.execute(new Open(dbName));
			String result = session.execute(command);
			session.execute(new Close());
			session.close();
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
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			session.execute(new Open(dbName));
			String result = session.query(xQuery).execute();
			session.execute(new Close());
			session.close();
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
			server.stop();
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
}