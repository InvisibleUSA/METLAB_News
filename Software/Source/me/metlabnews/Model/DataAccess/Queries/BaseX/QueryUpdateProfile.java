package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.Replace;



/**
 *  changes a profile that is already in basex
 *  returns: nothing
 * @author Marco Rempfer
 */
public class QueryUpdateProfile extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryUpdateProfile.class, Logger.Channel.DocDBMS);
	}

	public ObservationProfile profile;
	public String   result;

	@Override
	protected Command createBaseXQuery()
	{
		return new Replace("/profiles/" + profile.getID(), profile.toXML());
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
