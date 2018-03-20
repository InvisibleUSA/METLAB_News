package com.metlab.frontend.controller;

import java.sql.*;



public class SQLController
{
	public void test(String email, String name, String pName, String pw, String company, String sex)
	{
		try
		{
			String     url  = "jdbc:mariadb://46.101.223.95:3306/db";
			Connection conn = DriverManager.getConnection(url, "root", "tinf16in-MATLAB%");
			Statement statement = conn.createStatement();
			statement.executeQuery("INSERT INTO `METLAB_DB`.`Abonnent` (`EMail`, `Name`, `VName`, `PW`, `Firma`, `isAdmin`, `Geschlecht`) VALUES ('" + email + "', '" + name + "', '" + pName + "', '" + pw + "', '" + company + "', '1', " + sex + ")");
			conn.close();

		}
		catch(Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
