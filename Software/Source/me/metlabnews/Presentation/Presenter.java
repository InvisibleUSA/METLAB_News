package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.*;
import me.metlabnews.Model.Crawler.CrawlerController;
import me.metlabnews.Model.ResourceManagement.IResource;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Presenter implements IResource
{
	public static Presenter getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new Presenter();
		}
		return m_instance;
	}


	private Presenter()
	{
		m_sessions = new ConcurrentHashMap<>(initialSessionCapacity);
	}


	@Override
	public void initialize()
	{
		m_hasBeenInitialized = true;
	}

	@Override
	public void close()
	{
		m_sessions.forEach((ui, session) -> session.close() );
		m_sessions.clear();
		m_threadPool.shutdown();
	}


	/**
	 * Connect an instance of IUserInterface to the application
	 * and associate it with a session.
	 * Register all callback functions.
	 * @see IUserInterface
	 *
	 * @param ui user interface instance
	 */
	public void connect(IUserInterface ui)
	{
		if(!m_hasBeenInitialized)
		{
			throw new IllegalStateException("Presenter has not been initialized");
		}
		Session session = new Session();
		registerCallbacks(ui, session);
		m_sessions.put(ui, session);
	}

	private void registerCallbacks(IUserInterface ui, Session session)
	{
		UserManager userManager = UserManager.getInstance();
		ProfileManager profileManager = ProfileManager.getInstance();
		ClippingManager clippingManager = ClippingManager.getInstance();

		ui.registerCallbackSubscriberLogin(
				(onSuccess, onVerificationPending, onFailure, email, password) ->
						m_threadPool.execute(() -> userManager.subscriberLogin(
								session, onSuccess, onVerificationPending, onFailure, email, password)));

		ui.registerCallbackSubscriberRegistration((onSuccess, onFailure, fName, lName, org,
		                                           email, pw, admin) ->
			m_threadPool.execute(() -> userManager.registerNewSubscriber(session, onSuccess, onFailure, email, pw,
			                                                             fName, lName, org, admin)));

		ui.registerCallbackSysAdminLogin((onSuccess, onFailure, email, pw) ->
			m_threadPool.execute(() ->
				userManager.systemAdministratorLogin(session, onSuccess, onFailure, email, pw)));


		ui.registerCallbackLogout(session::logout);


		ui.registerCallbackFetchSubscribers((onSuccess, onFailure) -> userManager.getSubscribersOfOrganisation(
				session, onSuccess, onFailure));

		ui.registerCallbackFetchPendingVerificationRequests((onSuccess, onFailure) ->
			m_threadPool.execute(() -> userManager.getPendingVerificationRequests(session, onSuccess, onFailure)));

		ui.registerCallbackVerifySubscriber((onSuccess, onFailure, subscriberEmail, grantAdminStatus) ->
			m_threadPool.execute(() -> userManager.verifySubscriber(session, onSuccess, onFailure, subscriberEmail,
			                                                        grantAdminStatus)));


		ui.registerCallbackDenySubscriber((onSuccess, onFailure, email, date) ->
			m_threadPool.execute(() -> userManager.denySubscriber(session, onSuccess, onFailure, email, date)));


		ui.registerCallbackSubscriberRemoval((onSuccess, onFailure, email, date) ->
			m_threadPool.execute(() -> userManager.deactivateSubscriber(session, onSuccess, onFailure, email, date)));


		ui.registerCallbackAddOrganisation((onSuccess, onFailure, organisationName,
		                                    adminFirstName, adminLastName, adminEmail,
		                                    adminPassword) ->
			m_threadPool.execute(() -> userManager.addOrganisation(session, onSuccess, onFailure, organisationName,
			                                                       adminFirstName, adminLastName, adminEmail,
			                                                       adminPassword)));

		ui.registerCallbackRemoveOrganisation((onSuccess, onFailure, organisationName) ->
			m_threadPool.execute(() -> userManager.removeOrganisation(session, onSuccess, onFailure, organisationName)));

		ui.registerCallbackFetchOrganisations((onSuccess, onFailure) ->
			m_threadPool.execute(() -> userManager.getAllOrganisations(session, onSuccess, onFailure)));

		ui.registerCallbackChangePW(((onSuccess, onFailure, email, oldPW, newPW) ->
				m_threadPool.execute(() -> userManager.changePassword(session, onSuccess, onFailure, email, oldPW,
						                                                newPW))));

		ui.registerCallbackAddProfile(((onSuccess, onFailure, profileID, sources, keywords, interval) ->
		                              m_threadPool.execute(() -> profileManager.createNewProfile(
		                              		session, onSuccess, onFailure, profileID, Arrays.asList(keywords),
			                                Arrays.asList(sources), interval))));

		ui.registerCallbackUpdateProfile((onSuccess, onFailure, profileID, profileName, sources, keywords, interval, isActive)
				                                 -> profileManager.updateProfile(session, onSuccess, onFailure,
				                                                                 profileID, profileName,
				                                                                 Arrays.asList(keywords),
				                                                                 Arrays.asList(sources),
				                                                                 interval, isActive));

		ui.registerCallbackShareProfile((onSuccess, onFailure, senderEmail, profileId, receiverEmail) ->
				                                profileManager.shareProfile(session, onSuccess, onFailure,
				                                                            profileId, receiverEmail));

		ui.registerCallbackDeleteProfile((onSuccess, onFailure, ownerEmail, profileID) -> profileManager.removeProfile(
				session, onSuccess, onFailure, profileID));

		ui.registerCallbackFetchProfiles((onSuccess, onFailure) -> profileManager.getOwnProfiles(
				session, onSuccess, onFailure));


		ui.registerCallbackFetchClippings((onSuccess, onFailure, profileID) ->
				                                  clippingManager.getAllClippingsForObservationProfile(
				                                  		session, onSuccess, onFailure, profileID));

		ui.registerCallbackFetchTemplates((onSuccess, onFailure) -> profileManager.getAvailableTemplates(
				session, onSuccess, onFailure));

		ui.registerCallbackAddTemplate((onSuccess, onFailure, templateName, keywords, sources) ->
				                               profileManager.createNewProfileTemplate(session, onSuccess, onFailure,
				                                                                       templateName,
				                                                                       Arrays.asList(keywords),
				                                                                       Arrays.asList(sources)));

		ui.registerCallbackRemoveTemplate((onSuccess, onFailure, templateName) ->
				                                  profileManager.removeProfileTemplate(
				                                  		session, onSuccess, onFailure, templateName));

		ui.registerCallbackAddSource((onSuccess, onFailure, name, link, rssLink) -> clippingManager.addSource(
				session, onSuccess, onFailure, name, link, rssLink));

		ui.registerCallbackFetchSources((onSuccess, onFailure) -> clippingManager.getAvailableSources(
				session, onSuccess, onFailure));

		ui.registerCallbackRemoveSource((onSuccess, onFailure, name) -> clippingManager.removeSource(
				session, onSuccess, onFailure, name));

		ui.registerCallbackStartCrawler(() -> CrawlerController.getInstance().start());

		ui.registerCallbackStopCrawler(() -> CrawlerController.getInstance().stop());
	}


	/**
	 * Disconnect an instance of IUserInterface from the application
	 * and close the associated session
	 *
	 * @param ui user interface instance
	 */
	public void disconnect(IUserInterface ui)
	{
		m_sessions.get(ui).close();
		m_sessions.remove(ui);
	}


	/**
	 * Get information about the user associated to a IUserInterface instance.
	 *
	 * @param sender user interface instance
	 * @return UserDataRepresentation (null if session is not associated with a user)
	 * @throws IllegalArgumentException if sender is not associated with a session
	 */
	public UserDataRepresentation whoAmI(IUserInterface sender)
			throws IllegalArgumentException
	{
		Session session = m_sessions.get(sender);
		if(session == null)
		{
			throw new IllegalArgumentException("sender is not associated with a session");
		}
		if(session.isLoggedIn())
		{
			return new UserDataRepresentation(session.getUser());
		}
		else
		{
			return null;
		}
	}



	private static Presenter m_instance = null;
	private boolean m_hasBeenInitialized;
	private ConcurrentHashMap<IUserInterface, Session> m_sessions;
	@SuppressWarnings("FieldCanBeLocal")
	private final int initialSessionCapacity = 20;
	private final ExecutorService m_threadPool = Executors.newCachedThreadPool();
}
