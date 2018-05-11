package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ObservationProfile extends ObservationProfileTemplate
{
	public ObservationProfile(String name, String userMail, ArrayList<String> keywords,
	                          ArrayList<String> sources, LocalDateTime lastGenerationTime, Duration period)
	{
		m_profileName = name;
		m_userMail = userMail;
		m_sources = sources;
		m_keywords = keywords;
		m_lastGeneration = lastGenerationTime;
		m_period = period;
	}

	public ObservationProfile(XMLTag tag)
			throws IllegalArgumentException
	{
		try
		{
			m_profileName = tag.child("name").value();
			m_userMail = tag.child("owner").value();
			m_lastGeneration = LocalDateTime.parse(tag.child("last-generation").value());
			m_period = Duration.parse(tag.child("period").value());
			for(XMLTag keyword : tag.children("keywords"))
			{
				m_keywords.add(keyword.child("keyword").value());
			}

			for(XMLTag src : tag.children("sources"))
			{
				m_sources.add(src.child("source").value());
			}
		}
		catch(NullPointerException e)
		{
			Logger.getInstance().logError(this, "Not a valid profile.");
			throw new IllegalArgumentException("Parameter tag does not accurately represent a profile.");
		}
	}

	public ObservationProfile(String name)
	{
		m_profileName = name;
	}

	public void addKeyword(String keyword)
	{
		m_keywords.add(keyword);
	}

	public void addSource(String sourceLink)
	{
		m_sources.add(sourceLink);
	}

	public void setLastGenerationTime(LocalDateTime gt)
	{
		m_lastGeneration = gt;
	}

	public String getName()
	{
		return m_profileName;
	}

	public String getUserMail()
	{
		return m_userMail;
	}

	public List<String> getKeywords()
	{
		return Collections.unmodifiableList(m_keywords);
	}

	public List<String> getSources()
	{
		return Collections.unmodifiableList(m_sources);
	}

	public LocalDateTime getLastGenerationTime()
	{
		return m_lastGeneration;
	}


	public boolean isActive()
	{
		return m_isActive;
	}

	public void setActive(boolean state)
	{
		m_isActive = state;
	}


	public boolean equals(Object otherObject)
	{
		if(otherObject.getClass() != this.getClass())
		{
			return false;
		}
		ObservationProfile otherProfile = (ObservationProfile)otherObject;
		boolean result = m_profileName.equals(otherProfile.m_profileName)
				&& m_userMail.equals(otherProfile.m_userMail)
				&& m_keywords.equals(otherProfile.m_keywords)
				&& m_sources.equals(otherProfile.m_sources);
		return result;
	}


	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("---------------------" + m_profileName + "---------------------\n");
		s.append("Last generation time: ").append(m_lastGeneration).append("\n");
		s.append("Period: ").append(m_period).append("\n");
		s.append("Keywords:\n");
		for(String key : m_keywords)
		{
			s.append("    ").append(key).append("\n");
		}
		s.append("Sources:\n");
		for(String src : m_sources)
		{
			s.append("    ").append(src).append("\n");
		}
		for(char c : m_profileName.toCharArray())
		{
			s.append("-");
		}
		s.append("------------------------------------------\n");
		return s.toString();
	}


	public String toXML()
	{
		StringBuilder stringBuilder = new StringBuilder(
				"<profile>\n" +
						"    <name>" + m_profileName + "</name>\n" +
						"    <owner>" + m_userMail + "</owner>\n" +
						"    <last-generation>" + m_lastGeneration.format(m_dateTimePattern) + "</last-generation>\n" +
						"    <period>" + m_period + "</period>\n" +
						"    <keywords>\n");
		for(String keyword : m_keywords)
		{
			stringBuilder.append("        <keyword>").append(keyword).append("</keyword>\n");
		}
		stringBuilder.append("    </keywords>\n" + "    <sources>\n");
		for(String src : m_sources)
		{
			stringBuilder.append("        <source>").append(src).append("</source>\n");
		}
		stringBuilder.append("    </sources>\n" + "</profile>\n");
		return stringBuilder.toString();
	}



	private       LocalDateTime     m_lastGeneration;
	private       Duration          m_period          = Duration.ofDays(1);
	private       boolean           m_isActive        = false;

	private       DateTimeFormatter m_dateTimePattern = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");
}
