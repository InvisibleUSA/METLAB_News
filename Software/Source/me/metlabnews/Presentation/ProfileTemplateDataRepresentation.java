package me.metlabnews.Presentation;

import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



public class ProfileTemplateDataRepresentation
{
	public ProfileTemplateDataRepresentation(String ownerMail, String profileName,
	                                         List<String> keywords, List<String> sources)
	{
		m_ownerEmail = ownerMail;
		m_profileName = profileName;
		m_keywords = keywords;
		m_sources = sources;
	}

	public ProfileTemplateDataRepresentation(ObservationProfileTemplate profile)
	{
		m_id = profile.getID();
		m_profileName = profile.getName();
		m_keywords = profile.getKeywords();
		m_sources = profile.getSources();
	}

	/*
	@Override
	public String toString()
	{

	}
	*/

	public String getID()
	{
		return m_id;
	}

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



	private String        m_id = null;
	private String        m_profileName;
	private String        m_ownerEmail;
	private List<String>  m_keywords;
	private List<String>  m_sources;
}
