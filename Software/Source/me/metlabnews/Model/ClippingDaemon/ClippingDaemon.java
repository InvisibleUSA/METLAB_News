package me.metlabnews.Model.ClippingDaemon;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetEnqueuingProfileCandidates;
import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Model.ResourceManagement.IResource;



public class ClippingDaemon implements IResource
{
	static
	{
		Logger.getInstance().register(ClippingDaemon.class, Logger.Channel.ClippingDaemon);
		Logger.getInstance().register(ClippingGenerationManager.class, Logger.Channel.ClippingDaemon);
	}
	private ExecutorService                m_threadPool      = Executors.newCachedThreadPool();
	private LinkedList<ObservationProfile> m_pendingProfiles = new LinkedList<>();
	private Timer                          m_clippingManager = new Timer();
	private long                           m_waitingPeriod; //in seconds

	public ClippingDaemon()
	{

	}

	@Override
	public void initialize()
	{
		ConfigurationManager cm = ConfigurationManager.getInstance();
		m_waitingPeriod = cm.getClippingDaemonEnqueuingTimeOut(); // in seconds
		m_clippingManager.scheduleAtFixedRate(new ClippingGenerationManager(), 0, m_waitingPeriod * 1000);
	}

	@Override
	public void close() throws Exception
	{
		m_threadPool.shutdown();
	}


	private class ClippingGenerationManager extends TimerTask
	{
		@Override
		public void run()
		{
			//Run due clipping
			if(!m_pendingProfiles.isEmpty())
			{
				ObservationProfile p = m_pendingProfiles.removeFirst();
				m_threadPool.submit(new ClippingGenerator(p));
			}


			//Add new profiles to queue
			LocalTime                          start           = LocalTime.now();
			LocalTime                          end             = start.plusSeconds(m_waitingPeriod);
			QueryGetEnqueuingProfileCandidates queryCandidates = new QueryGetEnqueuingProfileCandidates(start, end);
			if(!queryCandidates.execute())
			{
				Logger.getInstance().logError(this, "Unknown database error when fetching new profiles.");
				return;
			}

			queryCandidates.getResults().forEach((op) -> {
				if(!m_pendingProfiles.contains(op))
				{
					m_pendingProfiles.add(op);
				}
			});
			Logger.getInstance().logDebug(this, m_pendingProfiles.size() + " profiles in queue.");
		}
	}
}
