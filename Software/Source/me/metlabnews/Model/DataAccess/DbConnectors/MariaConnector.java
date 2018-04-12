package me.metlabnews.Model.DataAccess.DbConnectors;

import java.sql.*;



public class MariaConnector
{
	private String conString = "jdbc:mariadb://46.101.223.95:3306/METLAB_DB?user=test&password=test";

	protected ResultSet query(String q) throws SQLException
	{
		return connect().executeQuery(q);
	}


	public MariaConnector()
	{
	}


	private Statement connect() throws SQLException
	{
		Connection conn      = DriverManager.getConnection(conString);
		Statement  statement = conn.createStatement();
		return statement;
	}

	private void disconnect()
	{
	}
}
