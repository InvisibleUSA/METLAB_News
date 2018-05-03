package me.metlabnews.Model.DataAccess.DbConnectors;


import org.basex.core.Command;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DatabaseConnector
{

	private MariaConnector mariaConnector = new MariaConnector();
	private BaseXConnector baseXConnector = new BaseXConnector();

	public String baseXQuery(Command query) throws IOException
	{
		return baseXConnector.query(query);
	}

	public ResultSet mariaQuery(Object[] query) throws SQLException
	{
		return mariaConnector.query(query);
	}
}
