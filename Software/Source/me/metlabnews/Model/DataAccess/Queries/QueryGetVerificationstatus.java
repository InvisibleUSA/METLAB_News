package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;



/**
 * Created by ln on 10.04.18.
 */
public class QueryGetVerificationstatus extends QueryBase
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
		return "SELECT isVeryfied FROM Abonennten WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{


	}
}
