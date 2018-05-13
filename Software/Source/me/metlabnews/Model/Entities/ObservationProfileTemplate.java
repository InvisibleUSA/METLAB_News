package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;



public class ObservationProfileTemplate
{
	public ObservationProfileTemplate(String name, String organisationID, @NotNull List<String> keywords,
	                                  @NotNull List<String> sources)
	{
		m_id = UUID.randomUUID().toString();
		m_profileName = name;
		m_organisationID = organisationID;
		m_sources.addAll(sources);
		m_keywords.addAll(keywords);
	}

	// 'protected' is less restrictive than 'package private', because the Java language definition
	// is complete bullshit and Java is just a useless stupid ugly Version of C#
	@SuppressWarnings("WeakerAccess")
	protected ObservationProfileTemplate() { }

	public ObservationProfileTemplate(XMLTag tag)
			throws IllegalArgumentException
	{
		try
		{
			m_id = tag.child("id").value();
			m_profileName = tag.child("name").value();
			m_organisationID = tag.child("organisation").value();
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
			Logger.getInstance().logError(this, "Not a valid profile template.");
			throw new IllegalArgumentException("Parameter tag does not accurately represent a profile template.");
		}
	}



	public String getID()
	{
		return m_id;
	}

	public String getName()
	{
		return m_profileName;
	}

	public void changeName(String newName)
	{
		m_profileName = newName;
	}

	public String getOrganisationID()
	{
		return m_organisationID;
	}


	@NotNull
	public List<String> getKeywords()
	{
		return Collections.unmodifiableList(m_keywords);
	}

	@NotNull
	public List<String> getSources()
	{
		return Collections.unmodifiableList(m_sources);
	}


	public void addKeywords(@NotNull List<String> newKeywords)
	{
		m_keywords.addAll(newKeywords);
	}

	public void addSources(@NotNull List<String> newSourceLinks)
	{
		m_sources.addAll(newSourceLinks);
	}

	public void removeKeywords(@NotNull List<String> keywordsToRemove)
	{
		m_keywords.removeAll(keywordsToRemove);
	}

	public void removeSources(@NotNull List<String> sourceLinksToRemove)
	{
		m_sources.removeAll(sourceLinksToRemove);
	}

	public void replaceKeywords(@NotNull List<String> newKeywords)
	{
		m_keywords = newKeywords;
	}

	public void replaceSources(@NotNull List<String> newSources)
	{
		m_sources = newSources;
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
			ObservationProfileTemplate otherTemplate = (ObservationProfileTemplate)otherObject;
			result = m_profileName.equals(otherTemplate.m_profileName)
					&& m_organisationID.equals(otherTemplate.m_organisationID)
					&& m_keywords.equals(otherTemplate.m_keywords)
					&& m_sources.equals(otherTemplate.m_sources);
		}
		return result;
	}


	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder("---------------------"
				                                        + m_profileName + "---------------------\n");
		stringBuilder.append("Keywords:\n");
		for(String key : m_keywords)
		{
			stringBuilder.append("    ").append(key).append("\n");
		}
		stringBuilder.append("Sources:\n");
		for(String src : m_sources)
		{
			stringBuilder.append("    ").append(src).append("\n");
		}
		for(char ignored : m_profileName.toCharArray())
		{
			stringBuilder.append("-");
		}
		stringBuilder.append("------------------------------------------\n");
		return stringBuilder.toString();
	}


	public String toXML()
	{
		StringBuilder stringBuilder = new StringBuilder(
				"<template>\n" +
						"    <id>" + m_id + "</id>\n" +
						"    <name>" + m_profileName + "</name>\n" +
						"    <organisation>" + m_organisationID + "</organisation>\n" +
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
		stringBuilder.append("    </sources>\n" + "</template>\n");
		return stringBuilder.toString();
	}



	protected String m_id;
	protected String m_profileName;
	protected String m_organisationID;
	protected List<String> m_keywords = new ArrayList<>();
	protected List<String> m_sources  = new ArrayList<>();
}
