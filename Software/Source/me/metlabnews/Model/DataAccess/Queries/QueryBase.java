package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.DatabaseConnector;



public abstract class QueryBase
{

	protected static Logger            m_logger      = Logger.getInstance();
	protected static DatabaseConnector m_dbConnector = new DatabaseConnector();

	/**
	 *  This method is called to execute the concrete subclass of QueryBase.
	 *  The return value says nothing about the result of the query. It informs the
	 *  caller the successful execution of the query. Not successful means the database could not be reached.
	 *  Successful merely means the database received the query and responded.
	 *  Information about the specific query (e.g. invalid login data) is NOT returned here.
	 * @return Query could / could not be executed
	 */
	public abstract boolean execute();

	public static DatabaseConnector getDBConnector()
	{
		return m_dbConnector;
	}
}
