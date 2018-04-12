package me.metlabnews.Model.DataAccess.Queries;


import me.metlabnews.Model.DataAccess.DbConnectors.DatabaseConnector;

import java.sql.ResultSet;
import java.sql.SQLException;



public abstract class QueryBase
{

	public static DatabaseConnector m_dbConnector = new DatabaseConnector();

	public boolean execute()
	{
		//TODO: Implement
		try
		{
			String    str   = null; //initialize result string for BaseXQuery
			ResultSet rs    = null; //initialize resultset for SQL Query
			String    sql   = createSQLQuery(); //
			String    basex = createBaseXQuery();
			if(basex != null)
			{
				str = m_dbConnector.baseXQuery(/*basex*/);
			}
			if(sql != null)
			{
				rs = m_dbConnector.mariaQuery(sql);
			}
			processResults(rs, str);
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected abstract String createBaseXQuery();
	protected abstract String createSQLQuery();
	protected abstract void processResults(ResultSet rs, String str);

}
