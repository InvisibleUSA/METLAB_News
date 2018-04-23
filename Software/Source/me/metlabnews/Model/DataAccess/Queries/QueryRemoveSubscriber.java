package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;

import java.sql.ResultSet;



/**
 * Created by ln on 23.04.18.
 */
public class QueryRemoveSubscriber extends QueryBase
{
	public String email;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "DELETE FROM Abonnent WHERE EMail = '" + email + "'";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
