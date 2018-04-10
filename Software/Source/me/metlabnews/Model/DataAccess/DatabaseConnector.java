package me.metlabnews.Model.DataAccess;


import java.sql.ResultSet;
import java.sql.SQLException;



public class DatabaseConnector
{
	private MariaConnector mariaConnector = new MariaConnector();
	private BaseXConnector baseXConnector = new BaseXConnector();

	public String baseXQuery()
	{
		//baseXConnector.query(null);
		return null;
	}

	public ResultSet mariaQuery(String query) throws SQLException
	{
		return mariaConnector.query(query);
	}
}
