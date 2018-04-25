package me.metlabnews.Model.DataAccess.Queries.BaseX;

import org.basex.core.Command;
import org.basex.core.cmd.XQuery;



public class QueryGetClippings extends BaseXQueryBase
{

	@Override
	protected Command createBaseXQuery()
	{
		return new XQuery("/clipping");
	}

	@Override
	protected void processResults(String str)
	{

	}
}
