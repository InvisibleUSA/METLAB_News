package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Entities.Subscriber;

import java.sql.ResultSet;



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
	protected void processResults(ResultSet rs)
	{
	}
}
