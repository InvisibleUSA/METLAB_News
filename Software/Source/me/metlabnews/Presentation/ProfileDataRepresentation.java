package me.metlabnews.Presentation;

import me.metlabnews.Model.Entities.ObservationProfile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



public class ProfileDataRepresentation
{
	public ProfileDataRepresentation(String ownerMail, String profileName,
	                                 List<String> keywords, List<String> sources, Boolean isActive,
	                                 LocalDateTime lastGenerationTime, Duration interval)
	{
		m_ownerEmail = ownerMail;
		m_profileName = profileName;
		m_keywords = keywords;
		m_sources = sources;
		m_isActive = isActive;
		m_lastGenerationTime = lastGenerationTime;
		m_interval = interval;
	}

	public ProfileDataRepresentation(ObservationProfile profile)
	{
		m_id = profile.getID();
		m_ownerEmail = profile.getUserMail();
		m_profileName = profile.getName();
		m_keywords = profile.getKeywords();
		m_sources = profile.getSources();
		m_lastGenerationTime = profile.getLastGenerationTime();
		m_interval = profile.getGenerationPeriod();
	}

	/*
	@Override
	public String toString()
	{

	}
	*/

	public String getEmail()
	{
		return m_ownerEmail;
	}

	public String getName()
	{
		return m_profileName;
	}

	public List<String> getKeywords()
	{
		return m_keywords;
	}

	public List<String> getSources()
	{
		return m_sources;
	}

	public Boolean getIsActive()
	{
		return m_isActive;
	}

	public LocalDateTime getLastGenerationTime()
	{
		return m_lastGenerationTime;
	}

	public Duration getInterval()
	{
		return m_interval;
	}



	private String        m_id = null;
	private String        m_profileName;
	private String        m_ownerEmail;
	private List<String>  m_keywords;
	private List<String>  m_sources;
	private Boolean       m_isActive;
	private LocalDateTime m_lastGenerationTime;
	private Duration      m_interval;
}
