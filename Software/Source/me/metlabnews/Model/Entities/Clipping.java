package me.metlabnews.Model.Entities;



import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.Mail.MailBuilder;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetArticleByID;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetProfileById;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



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
			QueryGetProfileById fetchQuery = new QueryGetProfileById();
			fetchQuery.profileID = tag.child("profileID").value();
			if(!fetchQuery.execute())
			{
				Logger.getInstance().logError(this, "Unknown Database error.");
			}
			else
			{
				m_profile = fetchQuery.getProfile();
			}

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
		s.append("\t<profileID>").append(m_profile.getID()).append("</profileID>\n");
		s.append("\t<generationtime>");
		s.append(m_generationTime.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")));
		s.append("</generationtime>\n");
		s.append("\t<articles>\n");
		for(Article a : m_articles)
		{
			s.append("\t\t<articleID>");
			s.append(a.getguid());
			s.append("</articleID>\n");
		}
		s.append("\t</articles>\n").append("</clipping>\n");
		return s.toString();
	}


	/**
	 * This method will take all articles to return a responsive HTML String.
	 *
	 * @return HTML String
	 */
	public String prettyPrintHTML()
	{
		List<String>  content  = new ArrayList<>();
		List<Article> articles = new ArrayList<>(m_articles);

		content.add("Artikelgenerierung: " + m_generationTime.format(
				DateTimeFormatter.ofPattern("dd.MM.YYYY  HH:mm:ss")) + "<br>");
		content.add(m_profile.toString());

		return MailBuilder.getFinalHTMLString(content, articles);
	}
}
