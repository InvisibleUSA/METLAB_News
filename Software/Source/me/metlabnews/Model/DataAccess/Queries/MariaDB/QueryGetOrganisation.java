package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class QueryGetOrganisation extends MariaDBQueryBase
{

	public String  organisationName   = "";
	public boolean organisationExists = false;
	public String[] organisations;

	@Override
	protected Object[] createSQLQuery()
	{
		if(organisationName.isEmpty())
		{
			return new String[] {"SELECT * FROM Klienten"};
		}
		return new String[] {"SELECT * FROM Klienten WHERE Name = ?", organisationName};
	}

	@Override
	protected void processResults(ResultSet rs)
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
