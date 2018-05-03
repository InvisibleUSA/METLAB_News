package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import java.sql.ResultSet;



/**
 * Created by ln on 23.04.18.
 */
public class QueryRemoveSubscriber extends MariaDBQueryBase
{
	public String email;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"DELETE FROM Abonnent WHERE EMail = ?", email};
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
