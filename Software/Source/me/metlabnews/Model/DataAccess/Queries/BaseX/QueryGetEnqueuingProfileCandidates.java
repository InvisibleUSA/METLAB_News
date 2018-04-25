package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class QueryGetEnqueuingProfileCandidates extends BaseXQueryBase
{
	private LocalTime m_startingTime;
	private LocalTime m_endingTime;
	private ArrayList<ObservationProfile> m_results = new ArrayList<>();

	public QueryGetEnqueuingProfileCandidates(LocalTime start, LocalTime end)
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
			m_endingTime = LocalTime.MIDNIGHT;
			//TODO fix day to next day query
			//if profiles are shortly after midnight, they might not get generated
		}

		DateTimeFormatter dtf   = DateTimeFormatter.ofPattern("HH:mm:ss");
		final String      start = m_startingTime.format(dtf);
		final String      end   = m_endingTime.format(dtf);
		final String query = "for $profile in /profile " +
				"where xs:time('" + start + "') < xs:time($profile/generationtime) and xs:time('" + end + " ') > xs:time($profile/generationtime) " +
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
