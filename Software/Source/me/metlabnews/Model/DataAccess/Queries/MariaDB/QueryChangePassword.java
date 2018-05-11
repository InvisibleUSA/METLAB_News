package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;



/**
 * Created by ln on 11.05.18.
 */
public class QueryChangePassword extends MariaDBQueryBase
{
	public String password;
	public String email;

	@Override
	protected Object[] createSQLQuery()
	{
		return new Object[] {"UPDATE Abonnent SET PW = ? WHERE EMail = ?", password, email};
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
