package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.NewsSource;

import java.sql.*;



public class QueryAddSource extends MariaDBQueryBase
{
	public NewsSource source;


	@Override
	protected Object[] createSQLQuery()
	{
		return new Object[] {"INSERT INTO Quellen (Name, Link, RSS_FEED) VALUES (?,?,?)",
				source.getName(), source.getLink(), source.getRss_link()};
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
