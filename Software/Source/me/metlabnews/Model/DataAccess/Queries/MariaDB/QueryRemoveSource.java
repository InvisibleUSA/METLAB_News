package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.ResultSet;



public class QueryRemoveSource extends MariaDBQueryBase
{
	public String uniqueName;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] { "DELETE FROM Quellen WHERE Name = ?", uniqueName };
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
