package me.metlabnews.Model.DataAccess.Queries.BaseX;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 *  retrieves profiles from basex that have the e-mail address subscriberEmail
 *  returns: list of all profiles matching the e-mail
 *
 * @author Marco Rempfer
 */
public class QueryGetProfilesByEmail extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetProfilesByEmail.class, Logger.Channel.DocDBMS);
	}

	public String subscriberEmail = "";

	private ArrayList<ObservationProfile> m_results = new ArrayList<>();

	public List<ObservationProfile> getResults()
	{
		return Collections.unmodifiableList(m_results);
	}


	@Override
	protected Command createBaseXQuery()
	{
		final String query = "for $profile in /profile\n" +
				" where $profile/owner = '" + subscriberEmail + "'\n" +
				" return $profile";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag result = new XMLTag("<profiles>" + str + "</profiles>");
		for(XMLTag tag : result.children("profile"))
		{
			try
			{
				ObservationProfile profile = new ObservationProfile(tag);
				m_results.add(profile);
			}
			catch(IllegalArgumentException e)
			{
				m_logger.logError(this, "Database contains invalid res.");
			}
		}
	}
}
