package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.*;



/**
 * Created by ln on 11.05.18.
 */
public class QueryChangePassword extends MariaDBQueryBase
{
	public String password;
	public String email;

	@Override
	protected Object[] createSQLQuery()
	{
		return new Object[] {"UPDATE Abonnent SET PW = ? WHERE EMail = ?", password, email};
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
