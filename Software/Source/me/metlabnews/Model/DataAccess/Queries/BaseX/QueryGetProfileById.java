package me.metlabnews.Model.DataAccess.Queries.BaseX;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



/**
 * get profile from basex using the id of the profile
 * returns: instance of the m_profile
 */
public class QueryGetProfileById extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetProfileById.class, Logger.Channel.DocDBMS);
	}

	public String profileID = "";
	private ObservationProfile m_profile;

	public ObservationProfile getProfile()
	{
		return m_profile;
	}


	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $profile in /profile\n" +
				" where $profile/id = '" + profileID + "'\n" +
				" return $profile";
		return new XQuery(query);
	}


	@Override
	protected void processResults(String str)
	{
		try
		{
			XMLTag res = new XMLTag(str);
			m_profile = new ObservationProfile(res);
		}
		catch(IllegalArgumentException e)
		{
			m_logger.logError(this, "Database contains invalid article. " + e.toString());
		}
	}
}
