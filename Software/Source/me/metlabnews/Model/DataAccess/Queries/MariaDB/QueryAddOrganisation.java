package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;



/**
 * Created by ln on 12.04.18.
 */
public class QueryAddOrganisation extends MariaDBQueryBase
{


	public String orgName;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"INSERT INTO Klienten (Name) VALUES (?)", orgName};
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
