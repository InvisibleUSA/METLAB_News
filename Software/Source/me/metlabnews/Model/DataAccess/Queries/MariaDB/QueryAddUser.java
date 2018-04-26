package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Entities.Subscriber;

import java.sql.ResultSet;



public class QueryAddUser extends MariaDBQueryBase
{
	public Subscriber subscriber;



	@Override
	protected String createSQLQuery()
	{
		return "INSERT INTO Abonnent (EMail, Name, VName, PW, Firma, isAdmin, isVerified) VALUES ('" + subscriber.getEmail() + "','" + subscriber.getLastName() + "','" + subscriber.getFirstName() + "','" + subscriber.getPassword() + "','" + subscriber.getOrganisationId().getName() + "', 0, " + (subscriber.isOrganisationAdministrator() ? 0 : 1) + ")";
	}

	@Override
	protected void processResults(ResultSet rs)
	{
	}
}
