package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;

import java.sql.ResultSet;



/**
 * Created by ln on 12.04.18.
 */
public class QueryDeleteOrganization extends QueryBase
{

	public String orgName;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "DELETE FROM Klienten WHERE Name = '" + orgName + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
