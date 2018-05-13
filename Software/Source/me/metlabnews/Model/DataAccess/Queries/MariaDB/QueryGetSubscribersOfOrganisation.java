package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	protected void processResults(ResultSet rs)
	{
		String  email     = "";
		String  firstName = null, lastName = null, password = null, organisation = null;
		Boolean isAdmin   = null;
		try
		{
			while(rs.next())
			{
				email = rs.getString("EMail");
				firstName = rs.getString("VName");
				lastName = rs.getString("Name");
				password = rs.getString("PW");
				organisation = rs.getString("Firma");
				isAdmin = rs.getString("isAdmin") == "1";
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(!email.isEmpty() || firstName != null || lastName != null || password != null || organisation != null || isAdmin != null)
		{
			m_subscribers.add(new Subscriber(email, password, firstName, lastName, new Organisation(organisation), isAdmin));
		}
	}

	public ArrayList<Subscriber> getResult() {
		return m_subscribers;
	}
}
