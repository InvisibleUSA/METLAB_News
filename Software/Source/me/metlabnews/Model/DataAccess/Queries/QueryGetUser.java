package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;



public class QueryGetUser extends QueryBase
{
	public String email;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Abonnenten WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
