package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.server.ClientSession;

import java.io.IOException;



public class BaseXController
{

	private BaseXServer server;

	private String username    = "admin";
	private String pw          = "admin";
	private String hostaddress = "localhost";
	private int    port        = 1984;
	private static BaseXController m_bxc;

	protected BaseXController()
	{
		try
		{
			server = new BaseXServer();
			server.stop();
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

	public synchronized String execute(Command cmd)
	{
		try
		{
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			String        result  = session.execute(cmd);
			session.close();
			return result;
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
			String[]      result  = new String[cmd.length];
			int           i       = 0;
			for(Command c : cmd)
			{
				result[i++] = session.execute(c);
			}
			session.close();
			return result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void stop() throws IOException
	{
		server.stop();
	}
}