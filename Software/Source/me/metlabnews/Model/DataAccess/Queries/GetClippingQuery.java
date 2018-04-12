package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.Clipping;



public class GetClippingQuery implements IQuery<Clipping>
{
	@Override
	public boolean execute()
	{
		// TODO: implement
		boolean success = false;
		return success;
	}

	@Override
	public Clipping getResult()
	{
		return m_result;
	}



	private Clipping m_result;
}
