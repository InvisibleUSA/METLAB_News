package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;



/**
 * Created by ln on 10.04.18.
 */
public class QueryGetVerificationstatus extends MariaDBQueryBase
{

	public String email;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT isVeryfied FROM Abonnent WHERE EMail = ?", email};
	}

	@Override
	protected void processResults(ResultSet rs)
	{


	}
}
