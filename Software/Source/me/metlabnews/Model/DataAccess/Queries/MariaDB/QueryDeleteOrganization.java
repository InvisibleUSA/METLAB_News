package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.DataAccess.Queries.QueryBase;
import org.basex.core.Command;

import java.sql.ResultSet;



/**
 * Created by ln on 12.04.18.
 */
public class QueryDeleteOrganization extends MariaDBQueryBase
{

	public String orgName;

	@Override
	protected String createSQLQuery()
	{
		return "DELETE FROM Klienten WHERE Name = '" + orgName + "'";
	}

	@Override
	protected void processResults(ResultSet rs)
	{

	}
}
