package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.NewsSource;



public class AddSourceQuery implements IQuery<Void>
{
	public AddSourceQuery(NewsSource source)
	{
		m_source = source;
	}

	private AddSourceQuery()
	{}


	@Override
	public boolean execute()
	{
		// TODO: implement
		boolean success = false;
		return success;
	}


	@Override
	public Void getResult()
	{
		return null;
	}



	protected NewsSource m_source;
}
