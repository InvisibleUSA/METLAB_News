package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.Article;
import org.basex.core.Command;
import org.basex.core.cmd.Add;

import java.util.Calendar;



/**
 * Adds a yacy result to basex
 * returns: nothing
 * @author Erik Hennig
 */
public class QueryAddYaCyArticle extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryAddArticle.class, Logger.Channel.DocDBMS);
	}

	private Article m_article;
	private String  m_result;

	public QueryAddYaCyArticle(Article article)
	{
		if (article.isRSS())
			m_logger.logWarning(this, "Article " + article.getguid() + " is no YaCy article.");
		m_article = article;
	}

	@Override
	protected Command createBaseXQuery()
	{
		String art = m_article.toString();
		return new Add("sources/" + "YaCy"
				               + "/" + m_article.getPubDate().get(Calendar.YEAR) + "-"
				               + m_article.getPubDate().get(Calendar.MONTH) + "-"
				               + m_article.getPubDate().get(Calendar.DAY_OF_MONTH)
				               + "/" + Helper.formatForFileName(m_article.getTitle())
				, art);
	}

	@Override
	protected void processResults(String str)
	{
		m_result = str;
	}

	public String getResult()
	{
		return m_result;
	}
}
