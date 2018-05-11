package me.metlabnews.Model.Entities;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.Mail.ResponsiveHTMLMessage;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetArticleByID;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



public class Clipping
{
	static
	{
		Logger.getInstance().register(Clipping.class, Logger.Channel.Entities);
	}

	private ArrayList<Article> m_articles       = new ArrayList<>();
	private ObservationProfile m_profile        = null;
	private LocalDateTime      m_generationTime = LocalDateTime.now();

	public Clipping(ObservationProfile profile)
	{
		m_profile = profile;
	}

	public Clipping(XMLTag tag) throws IllegalArgumentException
	{
		try
		{
			//m_profile = tag.child("profileID").value();
			m_generationTime = LocalDateTime.parse(tag.child("generationtime").value());
			for(XMLTag articles : tag.children("articles"))
			{
				QueryGetArticleByID q = new QueryGetArticleByID();
				q.articleID = articles.child("articleID").value();
				if(!q.execute())
				{
					Logger.getInstance().logError(this, "Unknown Database error.");
				}
				else
				{
					m_articles.add(q.getArticle());
				}
			}
		}
		catch(NullPointerException e)
		{
			Logger.getInstance().logError(this, "Not a valid clipping.");
			throw new IllegalArgumentException("Parameter tag does not accurately represent a clipping.");
		}
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

	public ArrayList<Article> getArticles()
	{
		return m_articles;
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder("<clipping>\n");
		s.append("\t<profileID>").append(m_profile.getName()).append("</profileID>\n"); //FIXME only add profileID
		s.append("\t<generationtime>");
		s.append(m_generationTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss")));
		s.append("</generationtime>\n");
		s.append("\t<articles>\n");
		for(Article a : m_articles)
		{
			s.append("\t\t<articleID>\n");
			s.append(a); //FIXME only add article ID
			s.append("</articleID>\n");
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
