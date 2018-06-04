package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import java.sql.*;



/**
 * Created by ln on 23.04.18.
 */
public class QueryRemoveSubscriber extends MariaDBQueryBase
{
	public String  email;
	public Date    date;
	public boolean isFinal;

	@Override
	protected Object[] createSQLQuery()
	{
		if(isFinal)
		{
			return new String[] {"DELETE FROM Abonnent WHERE EMail = ?", email};
		}
		return new Object[] {"UPDATE Abonnent SET deactivatedSince = ? WHERE EMail = ?", date, email};

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
