package com.metlab.crawler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Profile
{
	private String profileName;
	private String userMail;
	private List<String> keywords = new ArrayList<>();
	private List<String> sources  = new ArrayList<>();
	private LocalTime m_generationTime;

	public Profile(String name, String userMail, ArrayList<String> keywords, ArrayList<String> sources, LocalTime generationTime)
	{
		profileName = name;
		this.userMail = userMail;
		this.sources = sources;
		this.keywords = keywords;
		m_generationTime = generationTime;
	}

	public Profile(String name)
	{
		profileName = name;
	}

	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}

	public void addSource(String sourceLink)
	{
		sources.add(sourceLink);
	}

	public void setGenerationTime(LocalTime gt)
	{
		m_generationTime = gt;
	}

	public String getName()
	{
		return profileName;
	}

	public String getUserMail()
	{
		return userMail;
	}

	public List<String> getKeywords()
	{
		return Collections.unmodifiableList(keywords);
	}

	public List<String> getSources()
	{
		return Collections.unmodifiableList(sources);
	}

	public LocalTime getGenerationTime()
	{
		return m_generationTime;
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("---------------------" + profileName + "---------------------\n");
		s.append("Generation time: ").append(m_generationTime).append("\n");
		s.append("Keywords:\n");
		for(String key : keywords)
		{
			s.append("    ").append(key).append("\n");
		}
		s.append("Sources:\n");
		for(String src : sources)
		{
			s.append("    ").append(src).append("\n");
		}
		for(char c : profileName.toCharArray())
		{
			s.append("-");
		}
		s.append("------------------------------------------\n");
		return s.toString();
	}
}
