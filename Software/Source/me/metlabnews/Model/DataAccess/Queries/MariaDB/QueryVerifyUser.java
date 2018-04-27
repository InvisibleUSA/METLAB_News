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
	protected Object[] createSQLQuery()
	{
		return new Object[] {"UPDATE Abonnent SET isVerified = ? WHERE EMail = ?", status, email};
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
