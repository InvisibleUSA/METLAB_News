package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;

import java.sql.*;
import java.util.ArrayList;



public class QueryGetSubscribersOfOrganisation extends MariaDBQueryBase
{
	public String organisationId;
	private ArrayList<Subscriber> m_subscribers = new ArrayList<>();

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT * FROM Abonnent WHERE Firma = ?", organisationId };
	}

	@Override
	protected void processResults(Connection conn, Object[] q)
	{
		String  email     = "";
		String  firstName = null, lastName = null, password = null, organisation = null;
		Boolean isAdmin   = null;
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
				email = rs.getString("EMail");
				firstName = rs.getString("VName");
				lastName = rs.getString("Name");
				password = rs.getString("PW");
				organisation = rs.getString("Firma");
				isAdmin = rs.getString("isAdmin") == "1";

				if(!email.isEmpty() || firstName != null || lastName != null || password != null || organisation != null || isAdmin != null)
				{
					m_subscribers.add(new Subscriber(email, password, firstName, lastName, new Organisation(organisation), isAdmin));
				}
			}
		}
		catch(SQLException e)
		{
			return;
		}

	}

	public ArrayList<Subscriber> getResult() {
		return m_subscribers;
	}
}
