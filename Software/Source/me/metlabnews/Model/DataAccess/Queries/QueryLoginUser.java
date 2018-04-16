package me.metlabnews.Model.DataAccess.Queries;


import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryLoginUser extends QueryBase
{

	public String email;
	public String password;
	public boolean userLoginSuccessful = false;
	public boolean userExists          = false;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT EMail, PW FROM Abonnent WHERE EMail = '" + email + "'";
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
				readPassword = rs.getString("PW");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(email == "")
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
		userExists = true;
		userLoginSuccessful = true;
	}
}
