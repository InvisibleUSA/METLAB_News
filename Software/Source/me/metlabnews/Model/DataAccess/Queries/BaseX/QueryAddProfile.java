package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.Add;



public class QueryAddProfile extends BaseXQueryBase
{

	public ObservationProfile profile;
	public String             name;

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("/Profiles/" + name, profile.toString());
	}

	@Override
	protected void processResults(String str)
	{

	}
}
