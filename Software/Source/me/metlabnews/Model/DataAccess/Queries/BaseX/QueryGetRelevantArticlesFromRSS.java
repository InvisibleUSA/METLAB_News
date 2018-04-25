package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.NewsSource;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;



public class QueryGetRelevantArticlesFromRSS extends BaseXQueryBase
{
	private ObservationProfile m_op;
	private ArrayList<Article> m_candidates;

	public QueryGetRelevantArticlesFromRSS(ObservationProfile op)
	{
		m_op = op;
	}

	private static String concatenate(List<String> words)
	{
		if(words.isEmpty())
		{
			return null;
		}
		StringBuilder s      = new StringBuilder();
		String        prefix = "";
		for(String k : words)
		{
			s.append(prefix).append("'").append(k).append("'");
			prefix = ",";
		}
		return s.toString();
	}

	@Override
	protected Command createBaseXQuery()
	{
		final String keywords = concatenate(m_op.getKeywords());
		final String src      = concatenate(m_op.getSources());
		final String query = "for $article in /article " +
				"where ((title|description) contains text {" + keywords + "} all) and (source=(" + src + ")) " +
				"return $article";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag           articles = new XMLTag("<articles>" + str + "</articles>");
		SimpleDateFormat sdf      = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		for(XMLTag article : articles.children("article"))
		{
			try
			{
				final String title = article.child("title").value();
				final String link  = article.child("link").value();
				final String guid  = article.child("guid").value();

				final Calendar pubDate = Calendar.getInstance();
				pubDate.setTime(sdf.parse(article.child("pubDate").value()));

				final String     description = article.child("description").value();
				final NewsSource source      = new NewsSource();

				Article a = new Article(title, source, link, description, guid, pubDate);
				m_candidates.add(a);
			}
			catch(ParseException e)
			{
				Logger.getInstance().logError(this, "Invalid Date format");
			}
		}
	}

	public List<Article> getCandidates()
	{
		return Collections.unmodifiableList(m_candidates);
	}
}
