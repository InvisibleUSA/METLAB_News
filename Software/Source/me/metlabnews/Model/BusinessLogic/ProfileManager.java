package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericFailureEvent;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



/**
 *
 */
public class ProfileManager
{
	public static ProfileManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ProfileManager();
		}
		return m_instance;
	}


	private ProfileManager()
	{
	}


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param name
	 * @param keywords
	 * @param sources
	 * @param clippingPeriod
	 */
	public void createNewProfile(Session session, IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String name, List<String> keywords,
	                             List<String> sources, Duration clippingPeriod)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}

		QueryAddProfile addQuery = new QueryAddProfile();
		addQuery.profile = new ObservationProfile(name, session.getUser().getEmail(),
		                                          String.valueOf(session.getUser().getId()),
		                                          keywords, sources, clippingPeriod);
		if(!addQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		// Lets assume the subscriber wants to use his newly created profile right away
		if(!addQuery.profile.activate())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();
	}


	public void activateProfile(String profileID)
	{

	}

	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param name
	 * @param keywords
	 * @param sources
	 */
	public void createNewTemplate(Session session, IGenericEvent onSuccess,
	                              IGenericFailureEvent onFailure,
	                              String name, ArrayList<String> keywords,
	                              ArrayList<String> sources)
	{

	}




	private static ProfileManager m_instance = null;
}
