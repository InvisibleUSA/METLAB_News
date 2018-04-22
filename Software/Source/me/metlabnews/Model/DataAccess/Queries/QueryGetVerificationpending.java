package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Presentation.UserDataRepresentation;
import org.basex.core.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



/**
 * Created by ln on 10.04.18.
 */
public class QueryGetVerificationpending extends QueryBase
{

	public UserDataRepresentation[] users;

	@Override
	protected Command createBaseXQuery()
	{
		return null;
	}

	@Override
	protected String createSQLQuery()
	{
		return "SELECT * FROM Abonnent WHERE isVerified = 0";
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		try
		{
			//users = new UserDataRepresentation[rs.getFetchSize()];
			ArrayList<UserDataRepresentation> tempUsers = new ArrayList<>();
			UserDataRepresentation data;
			int i = 0;
			while(rs.next())
			{
				data = new UserDataRepresentation(rs.getString("EMail"), rs.getString("VName"), rs.getString("Name"),
				                                  rs.getString("isAdmin") == "1", false,
				                                  rs.getString("isVerified") == "1");
				tempUsers.add(data);
				i++;
			}
			users = new UserDataRepresentation[tempUsers.size()];
			tempUsers.toArray(users);
		}
		catch(SQLException e)
		{
			return;
		}
	}
}
