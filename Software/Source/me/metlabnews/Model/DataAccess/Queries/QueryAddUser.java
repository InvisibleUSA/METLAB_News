package me.metlabnews.Model.DataAccess.Queries;


import java.sql.ResultSet;



public class QueryAddUser extends QueryBase
{

	public String  email;
	public String  password;
	public String  firstName;
	public String  lastName;
	public String  organisationName;
	public boolean clientAdmin;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{

		return "INSERT INTO Abonennt (EMail, Firma";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
