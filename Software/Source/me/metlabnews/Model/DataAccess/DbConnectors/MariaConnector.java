package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;

import java.sql.*;



class MariaConnector
{
	static
	{
		Logger.getInstance().register(MariaConnector.class, Logger.Channel.RDBMS);
		try
		{
			Class.forName(ConfigurationManager.getInstance().getRdbmsDriver());
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Cannot find the driver in the classpath!" + e.toString());
		}
	}

	private ConfigurationManager configurationManager = ConfigurationManager.getInstance();

	private String     conString = (configurationManager.getRdbmsUseLocalDB()) ? configurationManager.getRdbmsLocalUrl() : configurationManager.getRdbmsRemoteUrl();
	private Connection conn;

	ResultSet query(String[] q) throws SQLException
	{
		conn = connect();
		PreparedStatement ps = conn.prepareStatement(q[0]);
		for(int i = 1; i < q.length; i++)
		{
			ps.setString(i, q[i]);
		}
		ResultSet rs = ps.executeQuery();
		ps.close();
		disconnect();
		return rs;
	}


	MariaConnector()
	{
	}


	private Connection connect() throws SQLException
	{
		return DriverManager.getConnection(conString, configurationManager.getRdbmsUsername(),
		                                   configurationManager.getRdbmsPassword());
	}

	private void disconnect() throws SQLException
	{
		conn.close();
	}
}
