package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Crawler.Helper;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.Article;
import org.basex.core.Command;
import org.basex.core.cmd.Add;

import java.util.Calendar;



/**
 * Query to add an article to basex
 * returns: nothing
 * @author Erik Hennig
 */
public class QueryAddArticle extends BaseXQueryBase
{
	static
	{
		Logger.getInstance().register(QueryAddArticle.class, Logger.Channel.DocDBMS);
	}

	private Article m_article;
	private String  m_result;

	public QueryAddArticle(Article article)
	{
		if (!article.isRSS())
			m_logger.logWarning(this, "Article " + article.getguid() + " is YaCy article.");
		m_article = article;
	}

	@Override
	protected Command createBaseXQuery()
	{
		return new Add("sources/" + m_article.getSource().getName()
				               + "/" + m_article.getPubDate().get(Calendar.YEAR) + "-"
				               + m_article.getPubDate().get(Calendar.MONTH) + "-"
				               + m_article.getPubDate().get(Calendar.DAY_OF_MONTH)
				               + "/" + Helper.formatForFileName(m_article.getTitle())
				, m_article.toString());
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
