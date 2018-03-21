package com.metlab.controller;

import org.basex.BaseXServer;
import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.server.ClientSession;

import java.io.IOException;



public class BaseXServerController
{

	private BaseXServer server;
	private Context     context;

	private String username    = "admin";
	private String pw          = "admin";
	private String hostaddress = "localhost";
	private int    port        = 1984;

	protected BaseXServerController()
	{
		try
		{
			server = new BaseXServer();
			context = new Context();
			System.out.println("Create a local database.");
			System.out.println("result: " + new CreateDB("LocalDB").execute(context));
			server.stop();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public String execute(Command cmd)
	{
		try
		{
			ClientSession session = new ClientSession(hostaddress, port, username, pw);
			return session.execute(cmd);
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
