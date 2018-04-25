package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.Subscriber;

import java.sql.ResultSet;



public class QueryVerifyUser extends MariaDBQueryBase
{

	public String email;
	public int status = 0;
	public Subscriber subscriber;
	public boolean    userExists;

	@Override
	protected String createSQLQuery()
	{
		return "UPDATE Abonnent SET isVerified = " + status + " WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs)
	{
		QueryGetUser qgu = new QueryGetUser();
		qgu.email = email;
		if(!qgu.execute())
		{
			return;
		}
		userExists = qgu.userExists;
		subscriber = qgu.subscriber;
	}
}
