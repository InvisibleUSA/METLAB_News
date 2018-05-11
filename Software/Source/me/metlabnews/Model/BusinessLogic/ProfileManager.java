package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;



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
	public void createNewProfile(Session session, IUserInterface.IGenericEvent onSuccess,
	                             IUserInterface.IGenericFailureEvent onFailure,
	                             String name, ArrayList<String> keywords,
	                             ArrayList<String> sources, Duration clippingPeriod)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}

		QueryAddProfile addQuery = new QueryAddProfile();
		addQuery.profile = new ObservationProfile(name, session.getUser().getEmail(), keywords, sources,
		                                          null, clippingPeriod);
		// Lets assume the subscriber wants to use his newly created profile right away
		addQuery.profile.setActive(true);

		if(!addQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();
	}


	public void createNewTemplate(Session session, IUserInterface.IGenericEvent onSuccess,
	                              IUserInterface.IGenericFailureEvent onFailure,
	                              String name, ArrayList<String> keywords,
	                              ArrayList<String> sources)
	{

	}




	private static ProfileManager m_instance = null;
}
