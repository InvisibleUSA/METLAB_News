package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.Add;



public class QueryAddProfile extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryAddProfile.class, Logger.Channel.DocDBMS);
	}

	public ObservationProfile profile;
	public String             name;

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("/Profiles/" + name, profile.toXML());
	}

	@Override
	protected void processResults(String str)
	{

	}
}
