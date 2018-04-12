package me.metlabnews.Model.DataAccess.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Created by ln on 10.04.18.
 */
public class QueryGetVerificationpending extends QueryBase
{

	public String[] mails;

	@Override
	protected String createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT EMail FROM Abonnent WHERE isVeryfied = 0";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		try
		{
			mails = new String[rs.getFetchSize()];
			int i = 0;
			while(rs.next())
			{
				mails[i] = rs.getString("EMail");
				i++;
			}
		}
		catch(SQLException e)
		{
			return;
		}
	}
}
