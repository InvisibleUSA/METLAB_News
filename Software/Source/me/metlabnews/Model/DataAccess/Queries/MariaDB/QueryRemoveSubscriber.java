package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import java.sql.ResultSet;



/**
 * Created by ln on 23.04.18.
 */
public class QueryRemoveSubscriber extends MariaDBQueryBase
{
	public String email;

	@Override
	protected String createSQLQuery()
	{
		return "DELETE FROM Abonnent WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
