package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Entities.Subscriber;

import java.sql.*;



public class QueryAddUser extends MariaDBQueryBase
{
	public Subscriber subscriber;



	@Override
	protected Object[] createSQLQuery()
	{
		return new Object[] {
				"INSERT INTO Abonnent (EMail, Name, VName, PW, Firma, isAdmin, isVerified) VALUES (?,?,?,?,?,?,0)",
				subscriber.getEmail(), subscriber.getLastName(), subscriber.getFirstName(), subscriber.getPassword(),
				subscriber.getOrganisationId().getName(), (subscriber.isOrganisationAdministrator() ? 1 : 0)};
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
