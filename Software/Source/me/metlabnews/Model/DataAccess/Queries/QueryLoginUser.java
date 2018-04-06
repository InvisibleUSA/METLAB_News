package me.metlabnews.Model.DataAccess.Queries;


import java.sql.ResultSet;



public class QueryLoginUser extends QueryBase
{
	public String username;
	public String password;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
