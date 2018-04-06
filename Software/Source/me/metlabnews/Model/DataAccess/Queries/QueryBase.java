package me.metlabnews.Model.DataAccess.Queries;


import java.sql.ResultSet;



public abstract class QueryBase
{
	public boolean execute()
	{
		//TODO: Implement
		return false;
	}

	protected abstract String createBaseXQuery();
	protected abstract String createSQLQuery();
	protected abstract void processResults(ResultSet rs, String str);

}
