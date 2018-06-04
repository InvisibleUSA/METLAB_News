package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.QueryBase;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;



abstract class MariaDBQueryBase extends QueryBase
{
	static
	{
		Logger.getInstance().register(MariaDBQueryBase.class, Logger.Channel.RDBMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean execute()
	{
		try
		{
			Object[]  sql = createSQLQuery();
			Connection conn  = m_dbConnector.mariaQuery(sql);

			processResults(conn, sql);
			conn.close();
			return true;
		}
		catch(SQLException e)
		{
			Logger.getInstance().logError(this, "SQL DB Error: " + e.toString());
			return false;
		}
		catch(NamingException e)
		{
			Logger.getInstance().logError(this, "Naming Error: " + e.toString());
			return false;
		}
	}

	/**
	 * Implemented by the specific subqueries. The query string is assembled and returned by this function.
	 * It is called by BaseXQueryBase when execute() is called.
	 *
	 * @return the query to be executed
	 */
	protected abstract Object[] createSQLQuery();

	/**
	 * Writes the results of the query back in the members of the specific subclass after the query was executed.
	 * It is called by MariaDBQueryBase when execute() is called.
	 *
	 * @param conn result of sql query
	 * @param q
	 */
	protected abstract void processResults(Connection conn, Object[] q);
}
