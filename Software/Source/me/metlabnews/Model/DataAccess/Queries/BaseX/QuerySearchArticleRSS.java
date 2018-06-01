package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * This query takes a profile and searches for new articles matching the profile.
 *
 * returns: all matching articles from RSS data
 *
 * @author Erik Hennig
 */
public class QuerySearchArticleRSS extends BaseXQueryBase
{
	static
	{
		m_logger.register(QuerySearchArticleRSS.class, Logger.Channel.DocDBMS);
	}

	private ObservationProfile m_op;
	private ArrayList<Article> m_candidates = new ArrayList<>();

	public QuerySearchArticleRSS(ObservationProfile op)
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

		final String query = "let $newest := (for $clipping in /clippings\n" +
				" where $clipping/profileID = '" + m_op.getID() + "'\n" +
				" order by $clipping/generationtime\n" +
				" return $clipping/generationtime)[1]\n" +
				" for $article in /article\n" +
				" where ($article/(title|description) contains text {" + keywords + "} all) and ($article/source=(" + src + "))\n" + // this is where ALL articles get send
				//" where (($article/title|$article/description) contains text {" + keywords + "} all) and ($article/source=(" + src + "))\n" + // this is where NO articles are send
				" and ($newest < xs:dateTime($article/pubDate) or fn:empty($newest))\n" +
				" and ($article/isRSS = 'true')\n" +
				" return $article";
		return new XQuery(query);
	}

	@Override
	protected void processResults(String str)
	{
		XMLTag           articles = new XMLTag("<articles>" + str + "</articles>");
		for(XMLTag article : articles.children("article"))
		{
			try
			{
				Article a = new Article(article);
				m_candidates.add(a);
			}
			catch(IllegalArgumentException e)
			{
				m_logger.logError(this, "Article badly formatted: " + e.toString());
			}
		}
	}

	public List<Article> getCandidates()
	{
		return Collections.unmodifiableList(m_candidates);
	}
}
