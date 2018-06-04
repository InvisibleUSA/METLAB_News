package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Presentation.UserDataRepresentation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;



/**
 * Created by ln on 10.04.18.
 */
public class QueryGetVerificationPending extends MariaDBQueryBase
{
	public String                   organization;
	public UserDataRepresentation[] users;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT * FROM Abonnent WHERE isVerified = 0 AND Firma = ?", organization};
	}

	@Override
	protected void processResults(Connection conn, Object[] q)
	{
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
			//users = new UserDataRepresentation[rs.getFetchSize()];
			ArrayList<UserDataRepresentation> tempUsers = new ArrayList<>();
			UserDataRepresentation            data;
			int                               i         = 0;
			while(rs.next())
			{
				data = new UserDataRepresentation(rs.getString("EMail"), rs.getString("VName"), rs.getString("Name"),
				                                  Objects.equals(rs.getString("isAdmin"), "1"), false,
				                                  Objects.equals(rs.getString("isVerified"), "1"));
				tempUsers.add(data);
				i++;
			}
			users = new UserDataRepresentation[tempUsers.size()];
			tempUsers.toArray(users);
		}
		catch(SQLException e)
		{
			return;
		}
	}
}
