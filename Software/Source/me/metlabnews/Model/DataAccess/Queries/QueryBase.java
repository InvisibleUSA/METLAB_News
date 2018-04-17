package me.metlabnews.Model.DataAccess.Queries;


import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.DbConnectors.DatabaseConnector;
import org.basex.core.Command;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



public abstract class QueryBase
{

	public static DatabaseConnector m_dbConnector = new DatabaseConnector();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
			//Logger.getInstance().log(Logger.Channel.RDBMS, Logger.LogPriority.ERROR, Logger.LogType.ToFile,"SQL DB Error: " + e.toString());
			//FIXME: Fix Logger
			return false;
		}
		catch(IOException e)
		{
			//Logger.getInstance().log(Logger.Channel.BaseX, Logger.LogPriority.ERROR, Logger.LogType.ToFile, "BaseX DB Error: " + e.toString());
			//FIXME: Fix logger
			return false;
		}
	}

	protected abstract Command createBaseXQuery();

	protected abstract String createSQLQuery();

	protected abstract void processResults(ResultSet rs, String str);
}
