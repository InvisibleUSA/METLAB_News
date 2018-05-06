package me.metlabnews.Model.DataAccess.Queries.YaCy;

import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.DataAccess.Queries.QueryBase;

import java.io.IOException;



public abstract class YaCyQueryBase extends QueryBase
{
	static
	{
		//TODO
		//Logger.getInstance().register(YaCyQueryBase.class, Logger.Channel.YaCy);
	}

	@Override
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean execute()
	{
		try
		{
			String url    = createYaCyQuery();
			String result = Helper.getHTTPResponse(url);
			processResults(result);
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Implemented by the specific subqueries. The query string is assembled and returned by this function.
	 * It is called by YaCyQueryBase when execute() is called.
	 *
	 * @return the query to be executed
	 */
	protected abstract String createYaCyQuery();

	/**
	 * Writes the results of the query back in the members of the specific subclass after the query was executed.
	 * It is called by MariaDBQueryBase when execute() is called.
	 *
	 * @param result result of YaCy query
	 */
	protected abstract void processResults(String result);
}
