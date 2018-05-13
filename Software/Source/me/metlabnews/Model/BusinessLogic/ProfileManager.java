package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Queries.BaseX.*;
import me.metlabnews.Model.Entities.ObservationProfile;
import me.metlabnews.Model.Entities.ObservationProfileTemplate;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Presentation.*;
import me.metlabnews.Presentation.IUserInterface.IGenericEvent;
import me.metlabnews.Presentation.IUserInterface.IGenericFailureEvent;

import java.time.Duration;
import java.util.List;



/**
 * Manages Observation Profiles and Sources
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
	 * @param session Session
	 * @param onSuccess event to call in case of success
	 * @param onFailure event to call in case of failure
	 * @param name non-unique name of the profile
	 * @param keywords non-null list of keywords to search for
	 * @param sources non-null list of sources to acquire news from
	 * @param clippingPeriod period of clipping generation
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		Subscriber subscriber = (Subscriber)session.getUser();
		QueryAddProfile addQuery = new QueryAddProfile();
		addQuery.profile = new ObservationProfile(name, subscriber.getEmail(),
		                                          String.valueOf(subscriber.getOrganisationId()),
		                                          keywords, sources, clippingPeriod);

		// Lets assume the subscriber wants to use his newly created profile right away
		if(!addQuery.profile.activate())
		{
			onFailure.execute(Messages.InvalidOberservationProfile);
			return;
		}

		if(!addQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();
	}


	/**
	 * @param session Session
	 * @param onSuccess event to call in case of success
	 * @param onFailure event to call in case of failure
	 * @param id unique ID to identify the observation profile
	 * @param name (new) name of the profile
	 * @param keywords (new) list of keywords
	 * @param sources (new) list of sources
	 * @param clippingPeriod (new) period of clipping generation
	 * @param isActive (new) state
	 */
	public void updateProfile(Session session, IGenericEvent onSuccess,
	                          IGenericFailureEvent onFailure,
	                          String id, String name, List<String> keywords,
	                          List<String> sources, Duration clippingPeriod,
	                          boolean isActive)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = id;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
			return;
		}
		ObservationProfile profile = fetchQuery.getProfile();
		profile.changeName(name);
		profile.replaceKeywords(keywords);
		profile.replaceSources(sources);
		profile.setGenerationPeriod(clippingPeriod);
		if(isActive)
		{
			profile.activate();
		}
		else
		{
			profile.deactivate();
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


	/**
	 * @param session Session
	 * @param onSuccess event to call in case of success
	 * @param onFailure event to call in case of failure
	 * @param profileID unique ID to identify the observation profile
	 * @param recipientEmail email address of the recipient
	 */
	public void shareProfile(Session session, IGenericEvent onSuccess,
	                         IGenericFailureEvent onFailure,
	                         String profileID, String recipientEmail)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
			return;
		}
		ObservationProfile sharedProfile = new ObservationProfile(recipientEmail, fetchQuery.getProfile());
		QueryAddProfile addQuery = new QueryAddProfile();
		addQuery.profile = sharedProfile;
		if(!addQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void createNewProfileTemplate(Session session, IGenericEvent onSuccess,
	                                IGenericFailureEvent onFailure,
	                                String name, List<String> keywords,
	                                List<String> sources)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();
		if(!subscriber.isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		QueryAddProfileTemplate addQuery = new QueryAddProfileTemplate();
		addQuery.template = new ObservationProfileTemplate(name, String.valueOf(subscriber.getOrganisationId()),
		                                                   keywords, sources);
		if(!addQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();
	}


	/**
	 * @param session Session
	 * @param onSuccess event to call in case of success, takes an array of ProfileDataRepresentation
	 * @param onFailure event to call in case of failure
	 */
	public void getOwnProfiles(Session session, IUserInterface.IFetchProfilesEvent onSuccess,
	                           IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfilesByEmail fetchQuery = new QueryGetProfilesByEmail();
		fetchQuery.subscriberEmail = session.getUser().getEmail();
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		int resultCount = fetchQuery.getResults().size();
		ProfileDataRepresentation[] resultSet = new ProfileDataRepresentation[resultCount];
		for(int idx = 0; idx < resultCount; ++idx)
		{
			resultSet[idx] = new ProfileDataRepresentation(fetchQuery.getResults().get(idx));
		}
		onSuccess.execute(resultSet);
	}


	/**
	 * @param session Session
	 * @param onSuccess event to call in case of success, takes an array of ProfileTemplateDataRepresentation
	 * @param onFailure event to call in case of failure
	 */
	public void getAvailableTemplates(Session session, IUserInterface.IFetchTemplatesEvent onSuccess,
	                                  IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();

		QueryGetProfileTemplatesByOrganisation fetchQuery = new QueryGetProfileTemplatesByOrganisation();
		fetchQuery.organisationId = subscriber.getOrganisationId().getName();
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		int resultCount = fetchQuery.getResults().size();
		ProfileTemplateDataRepresentation[] resultSet = new ProfileTemplateDataRepresentation[resultCount];
		for(int idx = 0; idx < resultCount; ++idx)
		{
			resultSet[idx] = new ProfileTemplateDataRepresentation(fetchQuery.getResults().get(idx));
		}
		onSuccess.execute(resultSet);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetProfileById fetchQuery = new QueryGetProfileById();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
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
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
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


	public void addSourcesToProfileTemplate(Session session, IGenericEvent onSuccess,
	                                        IGenericFailureEvent onFailure, String templateID,
	                                        List<String> newSources)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();
		if(!subscriber.isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		QueryGetProfileTemplateById fetchQuery = new QueryGetProfileTemplateById();
		fetchQuery.templateID = templateID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
			return;
		}
		ObservationProfileTemplate template = fetchQuery.getTemplate();
		template.addSources(newSources);
		QueryUpdateProfileTemplate updateQuery = new QueryUpdateProfileTemplate();
		updateQuery.template = template;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void addKeywordsToProfileTemplate(Session session, IGenericEvent onSuccess,
	                                 IGenericFailureEvent onFailure, String templateID,
	                                 List<String> newKeywords)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();
		if(!subscriber.isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		QueryGetProfileTemplateById fetchQuery = new QueryGetProfileTemplateById();
		fetchQuery.templateID = templateID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
			return;
		}
		ObservationProfileTemplate template = fetchQuery.getTemplate();
		template.addKeywords(newKeywords);
		QueryUpdateProfileTemplate updateQuery = new QueryUpdateProfileTemplate();
		updateQuery.template = template;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void removeSourcesFromProfileTemplate(Session session, IGenericEvent onSuccess,
	                                     IGenericFailureEvent onFailure, String templateID,
	                                     List<String> sourcesToRemove)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();
		if(!subscriber.isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		QueryGetProfileTemplateById fetchQuery = new QueryGetProfileTemplateById();
		fetchQuery.templateID = templateID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ObservationProfileDoesNotExist);
			return;
		}
		ObservationProfileTemplate template = fetchQuery.getTemplate();
		template.removeSources(sourcesToRemove);
		QueryUpdateProfileTemplate updateQuery = new QueryUpdateProfileTemplate();
		updateQuery.template = template;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}


	public void removeKeywordsFromProfileTemplate(Session session, IGenericEvent onSuccess,
	                                      IGenericFailureEvent onFailure, String templateID,
	                                      List<String> keywordsToRemove)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}
		Subscriber subscriber = (Subscriber)session.getUser();
		if(!subscriber.isOrganisationAdministrator())
		{
			onFailure.execute(Messages.NotClientAdmin);
			return;
		}

		QueryGetProfileTemplateById fetchQuery = new QueryGetProfileTemplateById();
		fetchQuery.templateID = templateID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.ProfileTemplateDoesNotExist);
			return;
		}
		ObservationProfileTemplate template = fetchQuery.getTemplate();
		template.removeKeywords(keywordsToRemove);
		QueryUpdateProfileTemplate updateQuery = new QueryUpdateProfileTemplate();
		updateQuery.template = template;
		if(!updateQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}



	public void removeProfileTemplate(Session session, IGenericEvent onSuccess,
	                          IGenericFailureEvent onFailure, String templateID)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		QueryRemoveProfileTemplate removalQuery = new QueryRemoveProfileTemplate();
		removalQuery.templateID = templateID;
		if(!removalQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}
		onSuccess.execute();
	}



	private static ProfileManager m_instance = null;
}
