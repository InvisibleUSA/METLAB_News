package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.server.ClientSession;

import java.io.IOException;

public class BaseXServerController
{

	private BaseXServer server;

	private String username    = "admin";
	private String pw          = "admin";
	private String hostaddress = "localhost";
	private int    port        = 1984;

	public BaseXServerController()
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

	public void stop() throws IOException
	{
		server.stop();
	}
}