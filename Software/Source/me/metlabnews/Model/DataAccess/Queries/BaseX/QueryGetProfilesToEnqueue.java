package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * query used by clipping daemon to find profiles that need to be generated
 * input: start and ending time
 * returns: profile that should be generated between start and end time
 *
 * @author Erik Hennig
 */
public class QueryGetProfilesToEnqueue extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetProfilesToEnqueue.class, Logger.Channel.DocDBMS);
	}

	private LocalDateTime                 m_startingTime;
	private LocalDateTime                 m_endingTime;
	private ArrayList<ObservationProfile> m_results = new ArrayList<>();

	public QueryGetProfilesToEnqueue(LocalDateTime start, LocalDateTime end)
	{
		m_startingTime = start;
		m_endingTime = end;
		if(m_startingTime == null || m_endingTime == null)
		{
			m_logger.logError(this, "Start or End-time missing");
		}
	}

	public List<ObservationProfile> getResults()
	{
		return Collections.unmodifiableList(m_results);
	}

	@Override
	protected Command createBaseXQuery()
	{
		if(m_startingTime.isAfter(m_endingTime))
		{
			return null;
		}

		DateTimeFormatter dtf   = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");
		final String      start = m_startingTime.format(dtf);
		final String      end   = m_endingTime.format(dtf);
		final String query = "for $profile in /profile \n" +
				" where xs:dateTime('" + end + "') > (xs:dateTime($profile/last-generation) + xs:dayTimeDuration($profile/period))\n" +
				//" and xs:dateTime('" + start + "') <= (xs:dateTime($profile/last-generation) + xs:dayTimeDuration($profile/period))\n" +
				" and $profile/active = 'true'\n" +
				" order by (xs:dateTime($profile/last-generation) + xs:dayTimeDuration($profile/period))\n" +
				" return $profile";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag profiles = new XMLTag("<profiles>" + str + "</profiles>");
		for(XMLTag singleProfile : profiles.children("profile"))
		{
			try
			{
				ObservationProfile op = new ObservationProfile(singleProfile);
				m_results.add(op);
			}
			catch(IllegalArgumentException e)
			{
				m_logger.logError(this, "Database contains invalid profiles. " + e.toString());
			}
		}
	}
}
