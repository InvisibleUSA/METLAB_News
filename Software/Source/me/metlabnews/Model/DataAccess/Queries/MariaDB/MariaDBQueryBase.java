package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.QueryBase;

import java.sql.ResultSet;
import java.sql.SQLException;



abstract class MariaDBQueryBase extends QueryBase
{
	/**
	 * @InheritDoc
	 */
	@Override
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean execute()
	{
		try
		{
			String    sql = createSQLQuery();
			ResultSet rs  = m_dbConnector.mariaQuery(sql);
			processResults(rs);
			return true;
		}
		catch(SQLException e)
		{
			Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR, "SQL DB Error: " + e.toString());
			return false;
		}
	}

	/**
	 * Implemented by the specific subqueries. The query string is assembled and returned by this function.
	 * It is called by BaseXQueryBase when execute() is called.
	 *
	 * @return the query to be executed
	 */
	protected abstract String createSQLQuery();

	/**
	 * Writes the results of the query back in the members of the specific subclass after the query was executed.
	 * It is called by MariaDBQueryBase when execute() is called.
	 *
	 * @param rs result of sql query
	 */
	protected abstract void processResults(ResultSet rs);
}
