package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Model.DataAccess.Queries.BaseX.*;
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


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 */
	public void getOwnProfiles(Session session, IGenericEvent onSuccess,
	                           IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfilesByEmail fetchQuery = new QueryGetProfilesByEmail();
		fetchQuery.subscriberEmail = session.getUser().getEmail();
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

	}


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param profileID
	 */
	public void activateProfile(Session session, IGenericEvent onSuccess,
	                            IGenericFailureEvent onFailure, String profileID)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.OberservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		if(!profile.activate())
		{
			onFailure.execute(Messages.IncompleteObservationProfile);
			return;
		}
		QueryUpdateProfile updateQuery = new QueryUpdateProfile();
		updateQuery.profile = profile;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void addSourcesToProfile(Session session, IGenericEvent onSuccess,
	                                IGenericFailureEvent onFailure, String profileID,
	                                List<String> newSources)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.OberservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		profile.addSources(newSources);
		QueryUpdateProfile updateQuery = new QueryUpdateProfile();
		updateQuery.profile = profile;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void addKeywordsToProfile(Session session, IGenericEvent onSuccess,
	                                IGenericFailureEvent onFailure, String profileID,
	                                List<String> newKeywords)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.OberservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		profile.addKeywords(newKeywords);
		QueryUpdateProfile updateQuery = new QueryUpdateProfile();
		updateQuery.profile = profile;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void removeSourcesFromProfile(Session session, IGenericEvent onSuccess,
	                                     IGenericFailureEvent onFailure, String profileID,
	                                     List<String> sourcesToRemove)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.OberservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		profile.removeSources(sourcesToRemove);
		QueryUpdateProfile updateQuery = new QueryUpdateProfile();
		updateQuery.profile = profile;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void removeKeywordsFromProfile(Session session, IGenericEvent onSuccess,
	                                     IGenericFailureEvent onFailure, String profileID,
	                                     List<String> keywordsToRemove)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.OberservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		profile.removeKeywords(keywordsToRemove);
		QueryUpdateProfile updateQuery = new QueryUpdateProfile();
		updateQuery.profile = profile;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void removeProfile(Session session, IGenericEvent onSuccess,
	                          IGenericFailureEvent onFailure, String profileID)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryRemoveProfile removalQuery = new QueryRemoveProfile();
		removalQuery.profileID = profileID;
		if(!removalQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
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
