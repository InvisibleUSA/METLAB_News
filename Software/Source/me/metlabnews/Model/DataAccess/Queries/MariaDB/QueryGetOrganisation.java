package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.*;
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
	protected void processResults(Connection conn, Object[] q)
	{
		ArrayList<String> temp = new ArrayList<>();
		try
		{
            PreparedStatement ps = conn.prepareStatement((String)q[0]);
            for(int i = 1; i < q.length; i++)
            {
                if(q[i] instanceof String)
                {
                    ps.setString(i, (String)q[i]);
                }
                else if(q[i] instanceof Integer)
                {
                    ps.setInt(i, (int)q[i]);
                }
                else if(q[i] instanceof Date)
                {
                    ps.setDate(i, (Date)q[i]);
                }
            }
			ResultSet rs = ps.executeQuery();
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
