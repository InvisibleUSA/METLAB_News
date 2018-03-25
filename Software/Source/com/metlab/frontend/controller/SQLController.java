package com.metlab.frontend.controller;

import com.metlab.crawler.Source;

import java.sql.*;
import java.util.ArrayList;



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

	public static SQLController instance;

	public static SQLController getInstance()
	{
		if(instance == null)
		{
			instance = new SQLController();
		}
		return instance;
	}

	public SQLController()
	{
	}

	//TODO: Documentation
	public boolean registerUser(String Email, String Company, String Sex, String Name, String Password, String PreName)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  statement = conn.createStatement();
			statement.executeQuery("INSERT INTO Abonnent (EMail, Firma, Geschlecht, isAdmin, Name, PW, VName) " +
					                       "VALUES ('" + Email + "', '" + Company + "', '" + Sex + "' , 1 , '" + Name + "', '" + Password + "', '" + PreName + "')");

			conn.close();
			System.out.println("User registration successful");
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
			return false;
		}
	}

	//TODO:Documentation
	public int loginUser(String email, String password)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  stmt      = conn.createStatement();
			ResultSet  rs        = stmt.executeQuery("SELECT PW, Email FROM Abonnent WHERE Email = '" + email + "'");
			String     pw        = "";
			while(rs.next())
			{
				pw = rs.getString("PW");
			}
			if(pw.isEmpty())
			{
				System.out.println("User not registered");
				return 0; //User not registered
			}
			else if(!pw.equals(password))
			{
				System.out.println("Wrong password");
				return 1; //Wrong password
			}
			else if(pw.equals(password))
			{
				System.out.println("Login successful");
				return 2; //Login correct
			}
			else
			{
				System.out.println("An error occured");
				return -1; //Error
			}
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
			return -1; //Error
		}
	}

	//TODO: Documentation
	public boolean isAdmin(String email)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  stmt      = conn.createStatement();
			ResultSet  rs        = stmt.executeQuery("SELECT Email FROM SystemAdmins");
			String     Email     = "";
			while(rs.next())
			{
				Email = rs.getString("EMail");
			}

			if(Email.isEmpty() || !email.equals(Email))
			{
				return false;
			}
			else if(email.equals(Email))
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
			return false;
		}
		return false;
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

	public ArrayList<Source> getSources()
	{
		try
		{
			String            conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection        conn      = DriverManager.getConnection(conString);
			Statement         stmt      = conn.createStatement();
			ResultSet         rs        = stmt.executeQuery("SELECT * FROM Quellen");
			ArrayList<Source> sources   = new ArrayList<>();
			while(rs.next())
			{
				String name     = rs.getString("Name");
				String rss_link = rs.getString("RSS_Feed");
				String link     = rs.getString("Link");
				Source curr     = new Source(name, link, rss_link);
				sources.add(curr);
			}
			return sources;
		}
		catch(Exception e)
		{
			return new ArrayList<>();
		}
	}

	public Source getSource(String name)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  stmt      = conn.createStatement();
			ResultSet  rs        = stmt.executeQuery("SELECT * FROM Quellen where Name='" + name + "'");
			if(rs.next())
			{
				String rss_link = rs.getString("RSS_Feed");
				String link     = rs.getString("Link");
				Source curr     = new Source(name, link, rss_link);
				return curr;
			}
		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}

	public boolean addSource(Source src)
	{
		try
		{
			String     conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";
			Connection conn      = DriverManager.getConnection(conString);
			Statement  statement = conn.createStatement();
			statement.executeQuery("INSERT INTO Quellen (Name, Link, RSS_Feed) " +
					                       "VALUES ('" + src.getName() + "', '" + src.getLink() + "', '" + src.getRss_link() + "')");
			conn.close();
			System.out.println("Source was added successfully");
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Exception in SQL Query: ");
			System.err.println(e.getMessage());
			return false;
		}
	}
}
