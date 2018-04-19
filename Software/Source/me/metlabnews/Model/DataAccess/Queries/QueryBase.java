package me.metlabnews.Model.DataAccess.Queries;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.DatabaseConnector;
import org.basex.core.Command;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



public abstract class QueryBase
{

	private static DatabaseConnector m_dbConnector = new DatabaseConnector();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	/**
	 *  This method is called to execute the concrete subclass of QueryBase.
	 *  The return value says nothing about the result of the query. It informs the
	 *  caller the successful execution of the query. Not successful means the database could not be reached.
	 *  Successful merely means the database received the query and responded.
	 *  Information about the specific query (e.g. invalid login data) is NOT returned here.
	 * @return Query could / could not be executed
	 */
	public boolean execute()
	{
		//TODO: Implement
		try
		{
			String    str   = null; //initialize result string for BaseXQuery
			ResultSet rs    = null; //initialize resultset for SQL Query
			String    sql   = createSQLQuery(); //
			Command   basex = createBaseXQuery();
			if(basex != null)
			{
				str = m_dbConnector.baseXQuery(basex);
			}
			if(!sql.equals(""))
			{
				rs = m_dbConnector.mariaQuery(sql);
			}
			processResults(rs, str);
			return true;
		}
		catch(SQLException e)
		{
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR, "SQL DB Error: " + e.toString());
			return false;
		}
		catch(IOException e)
		{
			Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.ERROR, "BaseX DB Error: " + e.toString());
			return false;
		}
	}

	/**
	 * Implemented by the specific subqueries. The query command is built and returned by this function.
	 * It is called by QueryBase when execute() is called.
	 *
	 * @return the query to be executed
	 */
	protected abstract Command createBaseXQuery();

	/**
	 * Implemented by the specific subqueries. The query string is assembled and returned by this function.
	 * It is called by QueryBase when execute() is called.
	 * @return the query to be executed
	 */
	protected abstract String createSQLQuery();

	/**
	 * Writes the results of the query back in the members of the specific subclass after the query was executed.
	 * It is called by QueryBase when execute() is called.
	 * @param rs result of sql query
	 * @param str result of basex query
	 */
	protected abstract void processResults(ResultSet rs, String str);
}
