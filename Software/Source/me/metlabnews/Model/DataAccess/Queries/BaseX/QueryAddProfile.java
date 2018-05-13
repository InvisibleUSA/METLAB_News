package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.Add;



/**
 * Adding a new profile with the same ID an an already existing one will overwrite that profile.
 * This query can therefore be used to update existing profiles.
 */
public class QueryAddProfile extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryAddProfile.class, Logger.Channel.DocDBMS);
	}

	public ObservationProfile profile;
	public String   result;

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("/profiles/" + profile.getID(), profile.toXML());
	}


	@Override
	protected void processResults(String str)
	{
		result = str;
	}
}
