package com.metlab.frontend.controller;

import java.sql.*;



public class SQLController
{

	public void test(String Email, String Company, String Sex, String Name, String Password, String PreName)
	{
		try
		{
			String     url       = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(url);
			Statement  statement = conn.createStatement();
			statement.executeQuery("INSERT INTO Abonnent (EMail, Firma, Geschlecht, isAdmin, Name, PW, VName) " +
					                       "VALUES ('" + Email + "', '" + Company + "', '" + Sex + "' , 1 , '" + Name + "', '" + Password + "', '" + PreName + "')");

			conn.close();
		}
		catch(Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
