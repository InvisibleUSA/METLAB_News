package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * Created by ln on 12.04.18.
 */
public class QueryDeleteOrganization extends MariaDBQueryBase
{

	public String orgName;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"DELETE FROM Klienten WHERE Name = ?", orgName};
	}

    @Override
    protected void processResults(Connection conn, Object[] q) {

    }

}
