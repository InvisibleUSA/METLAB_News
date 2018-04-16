package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.NewsSource;

import java.util.List;



public class GetAllSourcesQuery implements IQuery<List<NewsSource>>
{
	public GetAllSourcesQuery()
	{}


	@Override
	public boolean execute()
	{
		// TODO: implement
		boolean success = false;
		return success;
	}


	@Override
	public List<NewsSource> getResult()
	{
		return m_result;
	}



	protected List<NewsSource>  m_result;
}
