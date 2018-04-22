package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Entities.ObservationProfile;



class ClippingGenerator implements Runnable
{
	private ObservationProfile m_profile;

	public ClippingGenerator(ObservationProfile op)
	{
		m_profile = op;
	}

	@Override
	public void run()
	{

	}
}
