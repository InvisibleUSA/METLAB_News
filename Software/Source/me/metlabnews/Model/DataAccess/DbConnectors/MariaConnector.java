package me.metlabnews.Model.DataAccess.DbConnectors;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;

import java.beans.PropertyVetoException;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import javax.xml.bind.PropertyException;
import java.util.Properties;


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
			Logger.getInstance().logError(MariaConnector.class,
			                              "MARIA CONNECTOR: Cannot find the driver in the classpath!" + e.toString());
		}

		//dataSource = setupDataSource();
	}


	private static ConfigurationManager configurationManager = ConfigurationManager.getInstance();
	private static DataSource dataSource = setupDataSource();
	private Connection connection = null;
	private Statement statement = null;
	//private ResultSet resultSet = null;

	private String     conString = (configurationManager.getRdbmsUseLocalDB()) ? configurationManager.getRdbmsLocalUrl() : configurationManager.getRdbmsRemoteUrl();
	//private Connection conn;

	ResultSet query(Object[] q) throws SQLException
	{
		Connection conn = connect();
		PreparedStatement ps = conn.prepareStatement((String)q[0]);
		for(int i = 1; i < q.length; i++)
		{
			if(q[i] instanceof String)
			{
				ps.setString(i, (String)q[i]);
			}
			else if(q[i] instanceof Integer)
			{
				ps.setInt(i, (int)q[i]);
			}
			else if(q[i] instanceof Date)
			{
				ps.setDate(i, (Date)q[i]);
			}
		}
		ResultSet rs = ps.executeQuery();
		//ps.close();
		conn.close();
		return rs;
	}


	MariaConnector()
	{
	}


	private Connection connect() throws SQLException
	{
		return dataSource.getConnection();
		//return DriverManager.getConnection(conString, configurationManager.getRdbmsUsername(),configurationManager.getRdbmsPassword());
	}

	private static DataSource setupDataSource()
	{
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(configurationManager.getRdbmsDriver());
		} catch (PropertyVetoException e) {
			Logger.getInstance().logError(MariaConnector.class, e.toString());
		}
		if (configurationManager.getRdbmsUseLocalDB())
		{
			cpds.setJdbcUrl(configurationManager.getRdbmsLocalUrl());
		}
		else
		{
			cpds.setJdbcUrl(configurationManager.getRdbmsRemoteUrl());
		}
		cpds.setUser(configurationManager.getRdbmsUsername());
		cpds.setPassword(configurationManager.getRdbmsPassword());
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		return cpds;
	}

}
