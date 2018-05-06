package me.metlabnews.Model.DataAccess.Queries.BaseX;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.QueryBase;
import org.basex.core.Command;

import java.io.IOException;



abstract class BaseXQueryBase extends QueryBase
{
	static
	{
		Logger.getInstance().register(BaseXQueryBase.class, Logger.Channel.DocDBMS);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	/**
	 *  This method is called to execute the concrete subclass of BaseXQueryBase.
	 *  The return value says nothing about the result of the query. It informs the
	 *  caller the successful execution of the query. Not successful means the database could not be reached.
	 *  Successful merely means the database received the query and responded.
	 *  Information about the specific query (e.g. invalid login data) is NOT returned here.
	 * @return Query could / could not be executed
	 */
	public boolean execute()
	{
		try
		{
			Command basex = createBaseXQuery();
			String  str   = m_dbConnector.baseXQuery(basex);
			processResults(str);
			return true;
		}

		catch(IOException e)
		{
			Logger.getInstance().logError(this, "DocDBMS DB Error: " + e.toString());
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
	 * Writes the results of the query back in the members of the specific subclass after the query was executed.
	 * It is called by BaseXQueryBase when execute() is called.
	 *
	 * @param str result of basex query
	 */
	protected abstract void processResults(String str);
}
