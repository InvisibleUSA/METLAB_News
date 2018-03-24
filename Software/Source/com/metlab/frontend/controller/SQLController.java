package com.metlab.frontend.controller;

import java.sql.*;



public class SQLController
{

	/**
	 * Registers the User and writes the Data to SQL in Table 'Abonnent'
	 *
	 * @param Email
	 * @param Company
	 * @param Sex
	 * @param Name
	 * @param Password
	 * @param PreName
	 */
	public void registerUser(String Email, String Company, String Sex, String Name, String Password, String PreName)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  statement = conn.createStatement();
			statement.executeQuery("INSERT INTO Abonnent (EMail, Firma, Geschlecht, isAdmin, Name, PW, VName) " +
					                       "VALUES ('" + Email + "', '" + Company + "', '" + Sex + "' , 1 , '" + Name + "', '" + Password + "', '" + PreName + "')");

			conn.close();
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
		}
	}


	/**
	 * Test method, to return ALL E-Mails from SQL
	 */
	public void getEmails()
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  stmt      = conn.createStatement();
			ResultSet  rs        = stmt.executeQuery("SELECT Email FROM Abonnent");
			while(rs.next())
			{
				String Email = rs.getString("EMail");
				System.out.println(Email + "\n");
			}
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
		}
	}
}
