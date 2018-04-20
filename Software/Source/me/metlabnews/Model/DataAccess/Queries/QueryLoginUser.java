package me.metlabnews.Model.DataAccess.Queries;


import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryLoginUser extends QueryBase
{

	public String email;
	public String password;
	public boolean userLoginSuccessful = false;
	public boolean userExists          = false;
	public boolean userVerified        = false;
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
		String email        = "";
		String readPassword = "";
		try
		{
			while(rs.next())
			{
				email = rs.getString("EMail");
				readPassword = rs.getString("PW");
				m_name = rs.getString("Name");
				m_vName = rs.getString("VName");
				m_organisation = rs.getString("Firma");
				isAdmin = rs.getString("isAdmin");
				userVerified = rs.getString("isVerified").equals("1");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(email.isEmpty())
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
		if(!userVerified)
		{
			userExists = true;
			userLoginSuccessful = false;
			return;
		}
		subscriber = new Subscriber(email, password, m_vName, m_name, new Organisation(m_organisation), isAdmin == "1");
		userExists = true;
		userLoginSuccessful = true;
	}

	private String m_name;
	private String m_vName;
	private String m_organisation;
	private String isAdmin;
}