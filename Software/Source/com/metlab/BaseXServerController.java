package com.metlab;

import org.basex.BaseXServer;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;
import org.basex.server.ClientSession;

import java.io.IOException;



public class BaseXServerController
{

	BaseXServer server;

	public BaseXServerController()
	{
		try
		{
			server = new BaseXServer();
			Context       context = new Context();
			ClientSession session = new ClientSession("localhost", 1984, "admin", "admin");
			System.out.println("Create a local database.");
			String result = "";
			new CreateDB("LocalDB", result).execute(context);
			System.out.println("result: " + result);
			server.stop();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
