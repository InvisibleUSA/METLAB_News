package com.metlab.crawler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



public class Profile
{
	private String profileName;
	private List<String> keywords    = new ArrayList<String>();
	private List<String> sourceLinks = new ArrayList<String>();
	private LocalTime m_generationTime;

	public Profile(String name, ArrayList<String> keywords, ArrayList<String> sourceLinks, LocalTime generationTime)
	{
		profileName = name;
		this.sourceLinks = sourceLinks;
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
		sourceLinks.add(sourceLink);
	}

	public void setGenerationTime(LocalTime gt)
	{
		m_generationTime = gt;
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
		for(String src : sourceLinks)
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
