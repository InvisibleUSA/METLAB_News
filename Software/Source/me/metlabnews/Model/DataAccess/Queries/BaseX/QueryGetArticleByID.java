package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.Entities.Article;
import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



/**
 * finds an article in basex by its ID
 * returns: m_article instance
 * @author Erik Hennig
 */
public class QueryGetArticleByID extends BaseXQueryBase
{
	static
	{
		m_logger.register(QueryGetArticleByID.class, Logger.Channel.DocDBMS);
	}

	public  String  articleID = "";
	private Article m_article;

	public Article getArticle()
	{
		return m_article;
	}

	@Override
	protected Command createBaseXQuery()
	{
		final String query = "(for $art in /article\n" +
				" where $art/guid = '" + articleID + "'\n" +
				" return $art)[1]";
		return new XQuery(query);
	}


	@Override
	protected void processResults(String str)
	{
		try
		{
			XMLTag res = new XMLTag(str);
			m_article = new Article(res);
		}
		catch(IllegalArgumentException e)
		{
			m_logger.logError(this, "Database contains invalid article. " + e.toString());
		}
	}
}
