package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.SystemAdministrator;
import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Created by ln on 12.04.18.
 */
public class QueryLoginSysadmin extends QueryBase
{

	public String              email;
	public String              password;
	public boolean             adminLoginSuccessful = false;
	public boolean             userExists           = false;
	public SystemAdministrator sysadmin;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM SystemAdmin WHERE EMail = '" + email + "'";
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
			adminLoginSuccessful = false;
		}
		if(!readPassword.equals(password))
		{
			userExists = true;
			adminLoginSuccessful = false;
		}
		userExists = true;
		adminLoginSuccessful = true;
		sysadmin = new SystemAdministrator(email, password, null, null);
	}
}
