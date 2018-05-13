package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import org.basex.core.Command;
import org.basex.core.cmd.Delete;



/**
 *   deletes profile based on its ID
 *
 *   returns: nothing
 *
 * @author Marco Rempfer
 */
public class QueryRemoveProfile extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryRemoveProfile.class, Logger.Channel.DocDBMS);
	}
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
