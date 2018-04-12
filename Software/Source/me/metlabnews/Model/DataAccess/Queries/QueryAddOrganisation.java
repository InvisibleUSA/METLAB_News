package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;



/**
 * Created by ln on 12.04.18.
 */
public class QueryAddOrganisation extends QueryBase
{


	public String orgName;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "INSERT INTO Klienten (Name) VALUES ('" + orgName + "')";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
