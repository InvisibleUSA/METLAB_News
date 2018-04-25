package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class QueryGetOrganisation extends QueryBase
{

	public String  organisationName;
	public boolean organisationExists = false;
	public String[] organisations;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		if(organisationName.isEmpty())
		{
			return "SELECT * FROM KLEIENTEN";
		}
		return "SELECT * FROM Klienten WHERE Name = '" + organisationName + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		ArrayList<String> temp = new ArrayList<>();
		try
		{
			while(rs.next())
			{
				temp.add(rs.getString("Name"));
				if(!organisationName.isEmpty())
				{
					break;
				}
			}
		}
		catch(SQLException e)
		{
			return;
		}
		organisations = new String[temp.size()];
		temp.toArray(organisations);
		if(temp.size() != 0)
		{
			organisationExists = true;
		}
	}
}
