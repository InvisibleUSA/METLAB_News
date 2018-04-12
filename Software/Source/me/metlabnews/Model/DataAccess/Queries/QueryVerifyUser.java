package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;



/**
 * Created by ln on 10.04.18.
 */
public class QueryVerifyUser extends QueryBase
{
	public String email;
	public int status = 0;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "UPDATE Abonnent SET isVerified = " + status + " WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
