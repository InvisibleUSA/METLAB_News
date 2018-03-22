package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;
import org.basex.server.ClientSession;

import java.io.IOException;

public class BaseXServerController
{

	private BaseXServer server;

	private String username    = "admin";
	private String pw          = "admin";
	private String hostaddress = "localhost";
	private int    port        = 1984;
	private String dbName = "ClippingDB";

	public BaseXServerController()
	{
		try
		{
			server = new BaseXServer();
			System.out.println(execute(new CreateDB(dbName)));
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

	public synchronized String execute(String xQuery)
	{
		try
		{
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			session.execute(new Open(dbName));
			String        result  = session.query(xQuery).execute();
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