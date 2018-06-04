package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.*;



public class QueryRemoveSource extends MariaDBQueryBase
{
	public String uniqueName;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] { "DELETE FROM Quellen WHERE Name = ?", uniqueName };
	}

	@Override
	protected void processResults(Connection conn, Object[] q) {
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
			ps.executeQuery();
		}
		catch (SQLException e)
		{
			return;
		}
	}


}
