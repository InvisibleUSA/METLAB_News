package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;
import org.basex.core.cmd.Add;
import sun.java2d.cmm.Profile;

import java.sql.ResultSet;



public class QueryAddProfile extends QueryBase
{

	public Profile profile;
	public String  name;

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("/Profiles/" + name, profile.toString());
	}

	@Override
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
