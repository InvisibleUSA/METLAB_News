package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ObservationProfile
{
	private String       m_profileName;
	private String       m_userMail;
	private List<String> m_keywords = new ArrayList<>();
	private List<String> m_sources  = new ArrayList<>();
	private LocalTime    m_generationTime;

	public ObservationProfile(String name, String userMail, ArrayList<String> keywords, ArrayList<String> sources, LocalTime generationTime)
	{
		m_profileName = name;
		this.m_userMail = userMail;
		this.m_sources = sources;
		this.m_keywords = keywords;
		m_generationTime = generationTime;
	}

	public ObservationProfile(XMLTag tag)
	{
		try
		{
			m_profileName = tag.child("name").value();
			m_userMail = tag.child("owner").value();
			m_generationTime = LocalTime.parse(tag.child("generationtime").value(),
			                                   DateTimeFormatter.ofPattern("HH:mm:ss"));
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
			throw new IllegalArgumentException("tag does not accurately represent a profile.");
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

	public void setGenerationTime(LocalTime gt)
	{
		m_generationTime = gt;
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

	public LocalTime getGenerationTime()
	{
		return m_generationTime;
	}

	public boolean equals(Object o)
	{
		if(o.getClass() != this.getClass())
		{
			return false;
		}
		ObservationProfile p = (ObservationProfile)o;
		return m_profileName.equals(p.m_profileName);
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("---------------------" + m_profileName + "---------------------\n");
		s.append("Generation time: ").append(m_generationTime).append("\n");
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
		StringBuilder res = new StringBuilder("<profile>\n" +
				"    <name>" + m_profileName + "</name>\n" +
				"    <owner>" + m_userMail + "</owner>\n" +
				"    <generationtime>" + m_generationTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "</generationtime>\n" +
				"    <keywords>\n");
		for (String k : m_keywords)
				res.append("        <keyword>").append(k).append("</keyword>\n");
		res.append("    </keywords>\n" + "<sources>\n");
		for (String src: m_sources)
				res.append("        <source>").append(src).append("</source>\n");
		res.append("</sources>\n" + "</profile>\n");
		return res.toString();
	}
}
