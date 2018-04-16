package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryGetOrganisation extends QueryBase
{

	public String  organisationName;
	public boolean organisationExists = false;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Klienten WHERE Name = '" + organisationName + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		String orgname = "";
		try
		{
			while(rs.next())
			{
				orgname = rs.getString("Name");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(!orgname.isEmpty())
		{
			organisationExists = true;
		}
	}
}
