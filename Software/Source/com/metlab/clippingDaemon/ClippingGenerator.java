package com.metlab.clippingDaemon;

import com.metlab.crawler.Profile;

import java.util.Queue;



public class ClippingGenerator implements Runnable
{

	boolean m_stop = false;
	Queue<Profile> m_profiles; //profiles sorted by time to generate clipping

	public void stopProcessing()
	{
		m_stop = true;
	}

	@Override
	public void run()
	{
		while(!m_stop)
		{
			//XQUERY for next time a profile must be clipped
			//fn:min(for $time in /profile/keywords/keyword where (fn:current-time() < xs:time($time)) return xs:time($time))
			//queue front check
			m_profiles.peek();
		}
	}

	private void enqueueNextProfile()
	{
		//fetch profile
		//enqueue
	}

	private void sendClipping(Clipping c)
	{

	}

	private Clipping generateClipping(Profile p)
	{
		return new Clipping(p);
	}
}
