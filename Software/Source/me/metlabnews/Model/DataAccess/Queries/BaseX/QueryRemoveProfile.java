package me.metlabnews.Model.DataAccess.Queries.BaseX;

import org.basex.core.Command;
import org.basex.core.cmd.Delete;



public class QueryRemoveProfile extends BaseXQueryBase
{
	public String profileID = "";
	public String result = "";

	@Override
	protected Command createBaseXQuery()
	{
		return new Delete("/profiles/" + profileID);
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
