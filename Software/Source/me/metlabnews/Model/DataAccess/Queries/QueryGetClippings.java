package me.metlabnews.Model.DataAccess.Queries;

import org.basex.core.Command;
import org.basex.core.cmd.XQuery;

import java.sql.ResultSet;




public class QueryGetClippings extends QueryBase
{

	@Override
	protected Command createBaseXQuery()
	{
		return new XQuery("/clipping");
	}

	@Override
	protected String createSQLQuery()
	{
		return null;
	}

	@Override
	protected void processResults(ResultSet rs, String str)
	{

	}
}
