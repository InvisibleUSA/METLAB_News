package me.metlabnews.Presentation;

import me.metlabnews.Model.Entities.NewsSource;



public class SourceDataRepresentation
{
	public SourceDataRepresentation(String name, String link, String rssLink)
	{
		m_name = name;
		m_link = link;
		m_rssLink = rssLink;
	}

	public SourceDataRepresentation(NewsSource source)
	{
		m_name = source.getName();
		m_link = source.getLink();
		m_rssLink = source.getRss_link();
	}

	/*
	@Override
	public String toString()
	{
		//TODO
	}
	*/
	public String getName()
	{
		return m_name;
	}

	public String getLink()
	{
		return m_link;
	}

	public String getRssLink()
	{
		return m_rssLink;
	}

	private String m_name;
	private String m_link;
	private String m_rssLink;
}
