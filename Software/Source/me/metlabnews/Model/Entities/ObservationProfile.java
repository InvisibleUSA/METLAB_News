package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class ObservationProfile extends ObservationProfileTemplate
{
	static
	{
		Logger.getInstance().register(ObservationProfile.class, Logger.Channel.Entities);
	}

	public ObservationProfile(String name, String userMail, String organisationID,
	                          @NotNull List<String> keywords, @NotNull List<String> sources, @NotNull Duration period)
	{
		super(name, organisationID, keywords, sources);
		m_userMail = userMail;
		m_period = period;
	}

	@SuppressWarnings("unused")
	private ObservationProfile()
	{
		super();
	}

	public ObservationProfile(String userMail, ObservationProfile otherProfile)
	{
		super(otherProfile.m_profileName, otherProfile.m_organisationID,
		      otherProfile.m_keywords, otherProfile.m_sources);
		this.m_userMail = userMail;
		this.m_period = otherProfile.m_period;
		this.m_isActive = false;
	}

	public ObservationProfile(XMLTag tag)
			throws IllegalArgumentException
	{
		try
		{
			m_id = tag.child("id").value();
			m_profileName = tag.child("name").value();
			m_userMail = tag.child("owner").value();
			m_organisationID = tag.child("organisation").value();
			m_isActive = Boolean.parseBoolean(tag.child("active").value());
			m_lastGeneration = LocalDateTime.parse(tag.child("last-generation").value());
			m_period = Duration.parse(tag.child("period").value());
			for(XMLTag keyword : tag.child("keywords").children("keyword"))
			{
				m_keywords.add(keyword.value());
			}

			for(XMLTag src : tag.child("sources").children("source"))
			{
				m_sources.add(src.value());
			}
		}
		catch(NullPointerException e)
		{
			Logger.getInstance().logError(this, "Not a valid profile.");
			throw new IllegalArgumentException("Parameter tag does not accurately represent a profile.");
		}
	}


	public void setGenerationPeriod(@NotNull Duration period)
	{
		m_period = period;
	}

	public void setLastGenerationTime(@NotNull LocalDateTime lastGenerationTime)
	{
		m_lastGeneration = lastGenerationTime;
	}


	public String getUserMail()
	{
		return m_userMail;
	}


	@NotNull
	public Duration getGenerationPeriod()
	{
		return m_period;
	}

	@NotNull
	public LocalDateTime getLastGenerationTime()
	{
		return m_lastGeneration;
	}


	public boolean isActive()
	{
		return m_isActive;
	}

	public boolean activate()
	{
		m_isActive = !m_period.isZero() && !m_keywords.isEmpty() && !m_sources.isEmpty();
		return m_isActive;
	}

	public void deactivate()
	{
		m_isActive = false;
	}


	@Override
	public boolean equals(Object otherObject)
	{
		boolean result;
		if(otherObject.getClass() != this.getClass())
		{
			result = false;
		}
		else
		{
			ObservationProfile otherProfile = (ObservationProfile)otherObject;
			result = m_profileName.equals(otherProfile.m_profileName)
					&& m_userMail.equals(otherProfile.m_userMail)
					&& m_keywords.equals(otherProfile.m_keywords)
					&& m_sources.equals(otherProfile.m_sources);
		}
		return result;
	}


	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("Profilname: " + m_profileName + "<br>");
		//s.append("Last generation time: ").append(m_lastGeneration).append("<br>");
		//s.append("Period: ").append(m_period).append("<br>");
		s.append("Schlüsselwörter: ");
		for(String key : m_keywords)
		{
			s.append("    ").append(key).append("\n");
		}
		s.append("<br>");
		s.append("Quellen: ");
		for(String src : m_sources)
		{
			s.append(src).append(", ");
		}
		return s.toString();
	}


	@Override
	public String toXML()
	{
		StringBuilder stringBuilder = new StringBuilder(
				"<profile>\n" +
						"    <id>" + m_id + "</id>\n" +
						"    <name>" + m_profileName + "</name>\n" +
						"    <owner>" + m_userMail + "</owner>\n" +
						"    <organisation>" + m_organisationID + "</organisation>\n" +
						"    <last-generation>" + m_lastGeneration.format(m_dateTimePattern) + "</last-generation>\n" +
						"    <period>" + m_period + "</period>\n" +
						"    <active>" + String.valueOf(m_isActive) + "</active>\n" +
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



	private String            m_userMail;
	private LocalDateTime     m_lastGeneration  = LocalDateTime.now();
	private Duration          m_period          = Duration.ofDays(1);
	private boolean           m_isActive        = false;
	private DateTimeFormatter m_dateTimePattern = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");
}
