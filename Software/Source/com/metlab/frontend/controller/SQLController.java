package com.metlab.frontend.controller;

import java.sql.*;



public class SQLController
{

	public void test()
	{
		try
		{
			String     url       = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(url);
			Statement  statement = conn.createStatement();
			statement.executeQuery("INSERT INTO Abonnent (EMail, Firma, Geschlecht, isAdmin, Name, PW, VName) " +
					                       "VALUES ('email', 'Beispielfirma', 'm' , 1, 'name', 'pw', 'pName')");

			conn.close();
		}
		catch(Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
