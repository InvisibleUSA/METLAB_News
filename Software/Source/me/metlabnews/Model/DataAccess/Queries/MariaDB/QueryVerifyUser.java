package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.Subscriber;

import java.sql.*;



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
	protected void processResults(Connection conn, Object[] q)
	{
		try
		{
			PreparedStatement ps = conn.prepareStatement((String)q[0]);
			for(int i = 1; i < q.length; i++)
			{
				if(q[i] instanceof String)
				{
					ps.setString(i, (String)q[i]);
				}
				else if(q[i] instanceof Integer)
				{
					ps.setInt(i, (int)q[i]);
				}
				else if(q[i] instanceof Date)
				{
					ps.setDate(i, (Date)q[i]);
				}
			}
			ps.executeQuery();
		}
		catch (SQLException e)
		{
			return;
		}
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
