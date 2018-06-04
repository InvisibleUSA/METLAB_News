package me.metlabnews.Model.DataAccess.DbConnectors;


import org.basex.core.Command;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 *  simple wrapper class to store the connector classes in
 *
 * @author Lukas Niedergriese
 */
public class DatabaseConnector
{

	private MariaConnector m_mariaConnector = new MariaConnector();
	private BaseXConnector m_baseXConnector = new BaseXConnector();

	public String baseXQuery(Command query) throws IOException
	{
		return m_baseXConnector.query(query);
	}

	public Connection mariaQuery(Object[] query) throws SQLException, NamingException
	{
		return m_mariaConnector.query(query);
	}

	/**
	 * This method changes the connector used to execute request for baseX,
	 * so you can use a mocked connector.
	 * ONLY USED FOR UNIT TESTING!
	 * @param bxc the mocked instance
	 */
	public void replaceBaseXConnector(BaseXConnector bxc)
	{
		m_baseXConnector = bxc;
	}
}
