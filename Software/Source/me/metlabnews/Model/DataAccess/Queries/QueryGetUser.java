package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryGetUser extends QueryBase
{

	public String     email;
	public boolean    userExists;
	public Subscriber subscriber;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Abonnent WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		String  email     = "";
		String  firstName = null, lastName = null, password = null, organisation = null;
		Boolean isAdmin   = null;
		try
		{
			while(rs.next())
			{
				email = rs.getString("EMail");
				firstName = rs.getString("VName");
				lastName = rs.getString("Name");
				password = rs.getString("PW");
				organisation = rs.getString("Firma");
				isAdmin = rs.getString("isAdmin") == "1";
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(!email.isEmpty() || firstName != null || lastName != null || password != null || organisation != null || isAdmin != null)
		{
			subscriber = new Subscriber(email, password, firstName, lastName, new Organisation(organisation), isAdmin);
			userExists = true;
		}
	}
}
