package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.NewsSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class QueryAddSource extends MariaDBQueryBase
{
	public NewsSource source;


	@Override
	protected Object[] createSQLQuery()
	{
		return new Object[] {"INSERT INTO Quellen (Name, Link, RSS_FEED) VALUES (?,?,?)",
				source.getName(), source.getLink(), source.getRss_link()};
	}

	@Override
	protected void processResults(Connection conn, Object[] q) {

	}


}
