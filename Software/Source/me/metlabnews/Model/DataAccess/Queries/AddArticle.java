package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.Article;
import org.basex.core.cmd.Add;

import java.sql.ResultSet;



public class AddArticle extends QueryBase
{
	private Article m_article;
	private String  m_result;

	public AddArticle(Article article)
	{
		m_article = article;
	}

	@Override
	protected String createBaseXQuery()
	{
		return new Add("Artikel/" + m_article.getSource().getName()
				               + "/" + m_article.getDateFormatted()
				               + "/" + m_article.getFileName()
				, m_article.toString()).toString();
	}

	@Override
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{
		m_result = str;
	}

	public String getResult()
	{
		return m_result;
	}
}
