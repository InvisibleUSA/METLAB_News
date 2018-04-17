package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.Subscriber;
import org.basex.core.Command;

import java.sql.ResultSet;



/**
 * Created by ln on 10.04.18.
 */
public class QueryVerifyUser extends QueryBase
{

	public String     email;
	public int        status = 0;
	public Subscriber subscriber;
	public boolean    userExists;

	@Override
	protected Command createBaseXQuery()
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
