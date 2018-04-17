package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryGetUser extends QueryBase
{
	public String  email;
	public boolean userExists;

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
		String email = "";
		try
		{
			while(rs.next())
			{
				email = rs.getString("EMail");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(!email.isEmpty())
		{
			userExists = true;
		}
	}
}
