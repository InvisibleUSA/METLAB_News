package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.Entities.ObservationProfile;



public class AddObservationProfileQuery implements IQuery<Void>
{
	public AddObservationProfileQuery(ObservationProfile profile)
	{
		m_profile = profile;
	}

	private AddObservationProfileQuery()
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



	private ObservationProfile m_profile;
}
