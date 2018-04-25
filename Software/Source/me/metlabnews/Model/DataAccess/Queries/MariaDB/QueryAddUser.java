package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.DataAccess.Queries.QueryBase;
import org.basex.core.Command;

import java.sql.ResultSet;



public class QueryAddUser extends MariaDBQueryBase
{

	public String  email;
	public String  password;
	public String  firstName;
	public String  lastName;
	public String  organisationName;
	public boolean clientAdmin;

	@Override
	protected String createSQLQuery()
	{
		return "INSERT INTO Abonnent (EMail, Name, VName, PW, Firma, isAdmin, isVerified) VALUES ('" + email + "','" + lastName + "','" + firstName + "','" + password + "','" + organisationName + "', 0, 0)";
	}

	@Override
	protected void processResults(ResultSet rs)
	{
	}
}
