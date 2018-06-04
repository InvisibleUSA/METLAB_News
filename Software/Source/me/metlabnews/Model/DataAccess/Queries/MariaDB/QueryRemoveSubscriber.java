package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * Created by ln on 23.04.18.
 */
public class QueryRemoveSubscriber extends MariaDBQueryBase
{
	public String  email;
	public Date    date;
	public boolean isFinal;

	@Override
	protected Object[] createSQLQuery()
	{
		if(isFinal)
		{
			return new String[] {"DELETE FROM Abonnent WHERE EMail = ?", email};
		}
		return new Object[] {"UPDATE Abonnent SET deactivatedSince = ? WHERE EMail = ?", date, email};

	}

	@Override
	protected void processResults(Connection conn, Object[] q) {

	}

}
