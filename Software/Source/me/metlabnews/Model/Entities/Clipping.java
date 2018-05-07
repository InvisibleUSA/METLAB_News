package me.metlabnews.Model.Entities;



import me.metlabnews.Model.Common.Mail.ResponsiveHTMLMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;



public class Clipping
{
	private       ArrayList<Article> m_articles       = new ArrayList<>();
	private       ObservationProfile m_profile        = null;
	final private LocalDateTime      m_generationTime = LocalDateTime.now();

	public Clipping(ObservationProfile profile)
	{
		m_profile = profile;
	}

	public ObservationProfile getProfile()
	{
		return m_profile;
	}

	public LocalDateTime getGenerationTime()
	{
		return m_generationTime;
	}

	public void addArticle(Article a)
	{
		m_articles.add(a);
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("<clipping>\n");
		s.append("\t<profile>").append(m_profile.getName()).append("</profile>\n");
		s.append("\t<generationtime>").append(m_generationTime).append("</generationtime>\n");
		s.append("\t<articles>\n");
		for(Article a : m_articles)
		{
			s.append(a);
		}
		s.append("\t</articles>\n").append("</clipping>\n");
		return s.toString();
	}

	public String prettyPrint()
	{
		StringBuilder s = new StringBuilder();
		s.append("*************************************************************************Clipping" +
				         "*************************************************************************\n");
		s.append(m_generationTime).append("\n");
		s.append(m_profile);
		for(Article a : m_articles)
		{
			s.append(a).append("\n");
		}
		s.append("*********************************************************************************" +
				         "*************************************************************************\n");
		return s.toString();
	}

	public String prettyPrintHTML()
	{
		StringBuilder s = new StringBuilder();
		s.append(m_generationTime).append("\n");
		s.append(m_profile);
		for(Article a : m_articles)
		{
			s.append(a).append("\n");
		}
		return ResponsiveHTMLMessage.getInstance().createHTMLMail(s.toString());
	}
}
