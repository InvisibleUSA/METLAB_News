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



public class QueryGetEnqueuingProfileCandidates extends BaseXQueryBase
{
	private LocalDateTime                 m_startingTime;
	private LocalDateTime                 m_endingTime;
	private ArrayList<ObservationProfile> m_results = new ArrayList<>();

	public QueryGetEnqueuingProfileCandidates(LocalDateTime start, LocalDateTime end)
	{
		m_startingTime = start;
		m_endingTime = end;
		if(m_startingTime == null || m_endingTime == null)
		{
			Logger.getInstance().logError(this, "Start or End-time missing");
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

		DateTimeFormatter dtf   = DateTimeFormatter.ofPattern("YYYY-MM-DD'T'HH:mm:ss");
		final String      start = m_startingTime.format(dtf);
		final String      end   = m_endingTime.format(dtf);
		final String query = "for $profile in /profile " +
				"where xs:dateTime('" + start + "') < (xs:dateTime($profile/last-generation) + xs:dayTimeDuration($profile/period))" +
				" and xs:dateTime('" + end + " ') > (xs:dateTime($profile/last-generation) + xs:dayTimeDuration($profile/period)) " +
				"order by $profile/generationtime " +
				"return $profile";
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
				Logger.getInstance().logError(this, "Database contains invalid profiles.");
			}
		}
	}
}
