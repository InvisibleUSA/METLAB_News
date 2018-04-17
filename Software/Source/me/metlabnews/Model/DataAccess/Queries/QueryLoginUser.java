package me.metlabnews.Model.DataAccess.Queries;


import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryLoginUser extends QueryBase
{
	public String email;
	public String password;
	public boolean userLoginSuccessful = false;
	public boolean userExists          = false;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT EMail, PW FROM Abonennten WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		String email        = "";
		String readPassword = "";
		try
		{
			while(rs.next())
			{
				email = rs.getString("EMail");
				password = rs.getString("PW");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(!email.isEmpty())
		{
			userExists = false;
			userLoginSuccessful = false;
			return;
		}
		if(!readPassword.equals(password))
		{
			userExists = true;
			userLoginSuccessful = false;
			return;
		}
		userLoginSuccessful = true;
		userLoginSuccessful = true;
	}
}
