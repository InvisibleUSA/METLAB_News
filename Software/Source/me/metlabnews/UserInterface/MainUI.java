package me.metlabnews.UserInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.Presenter;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.Views.*;

import java.sql.Date;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 *
 * @author Achim Groß
 */
//@Theme("maintheme")
@PreserveOnRefresh
//@Push(PushMode.AUTOMATIC)
public class MainUI extends UI implements IUserInterface
{
	private SubscriberLoginView        m_subscriberLoginView;
	private SubscriberRegistrationView m_subscriberRegistrationView;
	private SubscriberDashboardView    m_subscriberDashboardView;
	private SystemAdminLoginView       m_systemAdminLoginView;
	private SystemAdminDashboardView   m_systemAdminDashboardView;
	private LogoutView                 m_logoutView;


	public MainUI()
	{
	}

	/**
	 * Initialize the MainUI. This is done by vaadin
	 *
	 * @param vaadinRequest ask vaadin what this is
	 */
	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		m_subscriberLoginView = new SubscriberLoginView(this);
		m_systemAdminLoginView = new SystemAdminLoginView(this);
		m_subscriberRegistrationView = new SubscriberRegistrationView(this);
		m_subscriberDashboardView = new SubscriberDashboardView(this);
		m_systemAdminDashboardView = new SystemAdminDashboardView(this);
		m_logoutView = new LogoutView(this);

		Presenter.getInstance().connect(this);

		openSubscriberLoginView();
	}


	/**
	 * Opens the login view for subscribers and client administrators.
	 */
	// region GUI Methods
	public void openSubscriberLoginView()
	{
		m_subscriberLoginView.show();
		access(() -> Page.getCurrent().setTitle("Anmelden"));
	}

	/**
	 * Opens the login view for system administrators.
	 */
	public void openSystemAdminLoginView()
	{
		m_systemAdminLoginView.show();
		access(() -> Page.getCurrent().setTitle("Anmelden"));
	}

	/**
	 * Opens the registration view for subscribers and client administrators.
	 */
	public void openSubscriberRegisterView()
	{
		m_subscriberRegistrationView.show();
		access(() -> Page.getCurrent().setTitle("Registrieren"));
	}

	/**
	 * Opens the dashboard view for subscribers and client administrators.
	 */
	private void openSubscriberDashboardView()
	{
		m_subscriberDashboardView.show();
		access(() -> Page.getCurrent().setTitle("Dashboard"));
	}

	/**
	 * Opens the dashboard for system administrators.
	 */
	private void openSystemAdminDashboardView()
	{
		m_systemAdminDashboardView.show();
		access(() -> Page.getCurrent().setTitle("Dashboard"));
	}

	/**
	 * Opens the view displayed when any user logs out.
	 */
	public void openLogoutView()
	{
		m_logoutView.show();
		access(() -> Page.getCurrent().setTitle("Abgemeldet"));
	}



	/**
	 * Executes the action of trying to log in a subscriber or client administrator.
	 *
	 * @param email the users email
	 * @param pw    the users password
	 */
	public void loginSubscriber(String email, String pw)
	{
		m_subscriberLoginCallback.execute(this::loginSuccessfulEvent,
		                                  this::subscriberVerificationPendingEvent,
		                                  this::loginFailedEvent,
		                                  email, pw);
	}

	/**
	 * Executes the action of trying to log in a system administrator.
	 *
	 * @param email the administrators email
	 * @param pw    the administrators password
	 */
	public void loginSysAdmin(String email, String pw)
	{
		m_sysAdminLoginCallback.execute(this::loginSuccessfulEvent,
		                                this::loginFailedEvent,
		                                email, pw);
	}

	/**
	 * Executes the action of registering a subscriber
	 *
	 * @param firstName   first name of the subscriber
	 * @param lastName    last name of the subscriber
	 * @param company     name of the subscribers company
	 * @param email       email of the subscriber
	 * @param password    password of the subscriber
	 * @param clientAdmin boolean whether the subscriber applied for administrator rights
	 */
	public void registerSubscriber(String firstName, String lastName, String company,
	                               String email, String password, boolean clientAdmin)
	{
		m_subscriberRegistrationCallback.execute(this::subscriberVerificationPendingEvent,
		                                         this::registrationFailedEvent,
		                                         firstName, lastName, company, email,
		                                         password, clientAdmin);
	}

	/**
	 * Executes the action of registering a subscriber that does not not to be verified afterwards
	 *
	 * @param onSuccess   event to be executed on successful execution
	 * @param firstName   first name of the subscriber
	 * @param lastName    last name of the subscriber
	 * @param company     name of the subscribers company
	 * @param email       email of the subscriber
	 * @param password    password of the subscriber
	 * @param clientAdmin boolean whether the subscriber applied for administrator rights
	 */
	public void registerVerifiedSubscriber(IGenericEvent onSuccess,
	                                       String firstName, String lastName, String company,
	                                       String email, String password, boolean clientAdmin)
	{
		m_subscriberRegistrationCallback.execute(
				() -> m_verifySubscriberCallback.execute(onSuccess,
				                                         errorMessage -> access(() -> Notification.show(errorMessage)),
				                                         email, true),
				this::registrationFailedEvent,
				firstName, lastName, company, email,
				password, clientAdmin);
	}

	/**
	 * Executes the action of logging out a user
	 */
	public void logout()
	{
		m_logoutCallback.execute(this::logoutEvent);
	}

	/**
	 * Executes the action of changing a users password.
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param email     email of the subscriber
	 * @param oldPW     old password of the subscriber
	 * @param newPW1    password of the subscriber
	 * @param newPW2    password of the subscriber
	 */
	public void changePassword(IGenericEvent onSuccess, IGenericFailureEvent onFailure,
	                           String email, String oldPW, String newPW1, String newPW2)
	{
		if(!newPW1.equals(newPW2))
		{
			Notification.show("Passworter sind nicht gleich");
			return;
		}
		m_changePasswordCallback.execute(onSuccess, onFailure, email, oldPW, newPW1);
	}

	/**
	 * Executes the action of removing a subscriber
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param email     email of the subscriber
	 * @param date      date of the action
	 */
	public void removeSubscriber(IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String email,
	                             Date date)
	{
		m_removeSubscriberCallback.execute(onSuccess, onFailure, email, date);
	}

	/**
	 * Executes the action of fetching pending subscriber verifications.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchPendingSubscriberVerifications(
			IFetchPendingVerificationRequestsEvent onSuccess,
			IGenericFailureEvent onFailure)
	{
		m_fetchPendingVerificationRequestsCallback.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of verifying a subscriber.
	 *
	 * @param onSuccess        event to be executed on successful execution
	 * @param onFailure        event to be executed on unsuccessful execution
	 * @param subscriberEmail  email of the subscriber
	 * @param grantAdminStatus boolean whether the subscriber will have administrator rights
	 */
	public void verifySubscriber(IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String subscriberEmail, boolean grantAdminStatus)
	{
		m_verifySubscriberCallback.execute(onSuccess, onFailure, subscriberEmail, grantAdminStatus);
	}

	/**
	 * Executes the action of denying a subscriber
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param email     email of the subscriber
	 * @param date      date of the action
	 */
	public void denySubscriber(IGenericEvent onSuccess,
	                           IGenericFailureEvent onFailure,
	                           String email,
	                           Date date)
	{
		access(() -> m_denySubscriberCallback.execute(onSuccess, onFailure, email, date));
	}

	/**
	 * Executes the action of fetching subscribers.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchSubscribers(IFetchSubscribersEvent onSuccess,
	                             IGenericFailureEvent onFailure)
	{
		m_fetchSubscribers.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of adding an organisation including an initial administrator.
	 *
	 * @param onSuccess        event to be executed on successful execution
	 * @param onFailure        event to be executed on unsuccessful execution
	 * @param organisationName name of the organisation
	 * @param adminFirstName   first name of the administrator
	 * @param adminLastName    last name of the administrator
	 * @param adminEmail       email of the administrator
	 * @param adminPassword    password of the administrator
	 */
	public void addOrganisation(IGenericEvent onSuccess,
	                            IGenericFailureEvent onFailure,
	                            String organisationName,
	                            String adminFirstName,
	                            String adminLastName,
	                            String adminEmail,
	                            String adminPassword)
	{
		m_addOrganisationCallback.execute(onSuccess, onFailure,
		                                  organisationName, adminFirstName, adminLastName,
		                                  adminEmail, adminPassword);
	}

	/**
	 * Executes the action of removing an organisation.
	 *
	 * @param onSuccess        event to be executed on successful execution, contains the fetched items
	 * @param onFailure        event to be executed on unsuccessful execution
	 * @param organisationName name of the organisation
	 */
	public void removeOrganisation(IGenericEvent onSuccess,
	                               IGenericFailureEvent onFailure,
	                               String organisationName)
	{
		m_removeOrganisationCallback.execute(onSuccess, onFailure, organisationName);
	}

	/**
	 * Executes the action of fetching all organisations.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void getAllOrganisations(IGetStringArrayEvent onSuccess,
	                                IGenericFailureEvent onFailure)
	{
		m_fetchOrganisationsCallback.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of fetching all profiles of one subscriber.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchProfiles(IFetchProfilesEvent onSuccess,
	                          IGenericFailureEvent onFailure)
	{
		m_fetchProfilesCallback.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of fetching all clippings of one profile.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchClippings(IFetchClippingsEvent onSuccess,
	                           IGenericFailureEvent onFailure,
	                           String profileID)
	{
		m_fetchClippingsCallback.execute(onSuccess, onFailure, profileID);
	}

	/**
	 * Executes the action of fetching all templates of one organisation.
	 *
	 * @param onSuccess event to be executed on successful execution, contains the fetched items
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchTemplates(IFetchTemplatesEvent onSuccess,
	                           IGenericFailureEvent onFailure)
	{
		m_fetchTemplatesCallback.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of adding a template
	 *
	 * @param onSuccess        event to be executed on successful execution
	 * @param onFailure        event to be executed on unsuccessful execution
	 * @param templateName     name of the template
	 * @param keywords         keywords to be searched for
	 * @param sourcesAsObjects sources to be searched in
	 */
	public void addTemplate(IGenericEvent onSuccess,
	                        IGenericFailureEvent onFailure,
	                        String templateName,
	                        String[] keywords,
	                        Object[] sourcesAsObjects)
	{
		String[] sources = new String[sourcesAsObjects.length];
		int      i;
		for(i = 0; i < sources.length; i++)
		{
			sources[i] = sourcesAsObjects[i].toString();
		}
		m_addTemplateCallback.execute(onSuccess, onFailure, templateName, keywords, sources);
	}

	/**
	 * Executes the action of removing a template.
	 *
	 * @param onSuccess  event to be executed on successful execution
	 * @param onFailure  event to be executed on unsuccessful execution
	 * @param templateId ID of the template
	 */
	public void removeTemplate(IGenericEvent onSuccess,
	                           IGenericFailureEvent onFailure,
	                           String templateId)
	{
		m_removeTemplateCallback.execute(onSuccess, onFailure, templateId);
	}

	/**
	 * Executes the action of adding a profile.
	 *
	 * @param onSuccess        event to be executed on successful execution
	 * @param onFailure        event to be executed on unsuccessful execution
	 * @param profileName      name of the profile
	 * @param keywords         keywords to be searched for
	 * @param sourcesAsObjects sources to be searched in
	 * @param interval         time between clippings
	 */
	public void addProfile(IGenericEvent onSuccess,
	                       IGenericFailureEvent onFailure,
	                       String profileName,
	                       String[] keywords,
	                       Object[] sourcesAsObjects,
	                       Duration interval)
	{
		String[] sources = new String[sourcesAsObjects.length];
		int      i       = 0;
		while(i < sources.length)
		{
			sources[i] = sourcesAsObjects[i].toString();
			i++;
		}
		m_addProfileCallback.execute(onSuccess, onFailure, profileName, sources, keywords, interval);
	}

	/**
	 * Executes the action of deleting a profile.
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param ownerMail email of the owner
	 * @param profileId ID of the profile
	 */
	public void deleteProfile(IGenericEvent onSuccess,
	                          IGenericFailureEvent onFailure,
	                          String ownerMail,
	                          String profileId)
	{
		m_deleteProfileCallback.execute(onSuccess, onFailure, ownerMail, profileId);
	}

	/**
	 * Executes the action of sharing a profile.
	 *
	 * @param onSuccess     event to be executed on successful execution
	 * @param onFailure     event to be executed on unsuccessful execution
	 * @param profileId     ID of the profile
	 * @param receiverEmail email of the receiver
	 */
	public void shareProfile(IGenericEvent onSuccess,
	                         IGenericFailureEvent onFailure,
	                         String profileId,
	                         String receiverEmail)
	{
		m_shareProfileCallback.execute(onSuccess, onFailure, whoAmI().getEmail(), profileId, receiverEmail);
	}

	/**
	 * Executes the action of updating a profile.
	 *
	 * @param onSuccess         event to be executed on successful execution
	 * @param onFailure         event to be executed on unsuccessful execution
	 * @param profileName       name of the profile
	 * @param keywordsAsObjects keywords to be searched for
	 * @param sourcesAsObjects  sources to be searched in
	 * @param interval          time between clippings
	 * @param isActive          boolean whether the profile is active
	 */
	public void updateProfileAction(IGenericEvent onSuccess,
	                                IGenericFailureEvent onFailure,
	                                String profileID, String profileName,
	                                Object[] keywordsAsObjects, Object[] sourcesAsObjects,
	                                Duration interval, boolean isActive)
	{
		int      i;
		String[] sources  = new String[sourcesAsObjects.length];
		String[] keywords = new String[keywordsAsObjects.length];
		for(i = 0; i < sources.length; i++)
		{
			sources[i] = sourcesAsObjects[i].toString();
		}
		for(i = 0; i < keywords.length; i++)
		{
			keywords[i] = keywordsAsObjects[i].toString();
			i++;
		}
		m_updateProfileCallback.execute(onSuccess, onFailure,
		                                profileID, profileName,
		                                keywords, sources,
		                                interval, isActive);
	}

	/**
	 * Executes the action of fetch all sources.
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 */
	public void fetchSources(IFetchSourcesEvent onSuccess,
	                         IGenericFailureEvent onFailure)
	{
		m_fetchSourcesCallback.execute(onSuccess, onFailure);
	}

	/**
	 * Executes the action of adding a source.
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param name      name of the source
	 * @param link      web-link of the source
	 * @param rssLink   RSS-link of the source
	 */
	public void addSource(IGenericEvent onSuccess,
	                      IGenericFailureEvent onFailure,
	                      String name, String link, String rssLink)
	{
		m_addSourceCallback.execute(onSuccess, onFailure, name, link, rssLink);
	}

	/**
	 * Executes the action of removing a source.
	 *
	 * @param onSuccess event to be executed on successful execution
	 * @param onFailure event to be executed on unsuccessful execution
	 * @param name      name of the source
	 */
	public void removeSource(IGenericEvent onSuccess,
	                         IGenericFailureEvent onFailure,
	                         String name)
	{
		m_removeSourceCallback.execute(onSuccess, onFailure, name);
	}

	/**
	 * Executes the action of starting the crawler
	 */
	public void startCrawler()
	{
		m_startCrawlerCallback.execute();
	}

	/**
	 * Executes the action of stopping the crawler
	 */
	public void stopCrawler()
	{
		m_stopCrawlerCallback.execute();
	}
	// endregion GUI Methods

	/**
	 * Gets a the user currently logged in.
	 *
	 * @return the user currently logged in
	 */
	public UserDataRepresentation whoAmI()
	{
		return Presenter.getInstance().whoAmI(this);
	}


	// region Callbacks
	/**
	 * registers the callback for logging in a subscriber or client administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback)
	{
		m_subscriberLoginCallback = callback;
	}

	/**
	 * registers the callback for registering a subscriber or administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback)
	{
		m_subscriberRegistrationCallback = callback;
	}

	/**
	 * registers the callback for removing a subscriber or client administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackSubscriberRemoval(IRemoveSubscriberCallback callback)
	{
		m_removeSubscriberCallback = callback;
	}

	/**
	 * registers the callback for fetching pending verification requests
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchPendingVerificationRequests(IFetchPendingVerificationRequestsCallback callback)
	{
		m_fetchPendingVerificationRequestsCallback = callback;
	}

	/**
	 * registers the callback for verifying a subscriber or client administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackVerifySubscriber(IVerifySubscriberCallback callback)
	{
		m_verifySubscriberCallback = callback;
	}

	/**
	 * registers the callback for denying a subscriber or client administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackDenySubscriber(IDenySubscriberCallback callback)
	{
		m_denySubscriberCallback = callback;
	}

	/**
	 * registers the callback for fetching subscribers
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchSubscribers(IFetchSubscribersCallback callback)
	{
		m_fetchSubscribers = callback;
	}

	/**
	 * registers the callback for logging in a system administrator
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackSysAdminLogin(ISysAdminLoginCallback callback)
	{
		m_sysAdminLoginCallback = callback;
	}

	/**
	 * registers the callback for adding an organisation
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackAddOrganisation(IAddOrganisationCallback callback)
	{
		m_addOrganisationCallback = callback;
	}

	/**
	 * registers the callback for removing an organisation
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackRemoveOrganisation(IRemoveOrganisationCallback callback)
	{
		m_removeOrganisationCallback = callback;
	}

	/**
	 * registers the callback for fetching all organisations
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchOrganisations(IFetchOrganisationsCallback callback)
	{
		m_fetchOrganisationsCallback = callback;
	}

	/**
	 * registers the callback for changing a password
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackChangePW(IChangePasswordCallback callback)
	{
		m_changePasswordCallback = callback;
	}

	/**
	 * registers the callback for logging out any user
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackLogout(ILogoutCallback callback)
	{
		m_logoutCallback = callback;
	}

	/**
	 * registers the callback for fetching all profiles of a subscriber
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchProfiles(IFetchProfilesCallback callback)
	{
		m_fetchProfilesCallback = callback;
	}

	/**
	 * registers the callback for fetching all clippings of a profile
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchClippings(IFetchClippingsCallback callback)
	{
		m_fetchClippingsCallback = callback;
	}

	/**
	 * registers the callback for fetching all templates of a organisation
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchTemplates(IFetchTemplatesCallback callback)
	{
		m_fetchTemplatesCallback = callback;
	}

	/**
	 * registers the callback for adding a template
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackAddTemplate(IAddTemplateCallback callback)
	{
		m_addTemplateCallback = callback;
	}

	/**
	 * registers the callback for removing a template
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackRemoveTemplate(IRemoveTemplateCallback callback)
	{
		m_removeTemplateCallback = callback;
	}

	/**
	 * registers the callback for adding a profile
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackAddProfile(IAddProfileCallback callback)
	{
		m_addProfileCallback = callback;
	}

	/**
	 * registers the callback for deleting a profile
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackDeleteProfile(IDeleteProfileCallback callback)
	{
		m_deleteProfileCallback = callback;
	}

	/**
	 * registers the callback for sharing a profile
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackShareProfile(IShareProfileCallback callback)
	{
		m_shareProfileCallback = callback;
	}

	/**
	 * registers the callback for updating a profile
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackUpdateProfile(IUpdateProfileCallback callback)
	{
		m_updateProfileCallback = callback;
	}

	/**
	 * registers the callback for fetching all sources
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackFetchSources(IFetchSourcesCallback callback)
	{
		m_fetchSourcesCallback = callback;
	}

	/**
	 * registers the callback for adding a source
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackAddSource(IAddSourceCallback callback)
	{
		m_addSourceCallback = callback;
	}

	/**
	 * registers the callback for removing a source
	 *
	 * @param callback callback to be registered
	 */
	@Override
	public void registerCallbackRemoveSource(IRemoveSourceCallback callback)
	{
		m_removeSourceCallback = callback;
	}

	/**
	 * registers the callback for starting the crawler
	 */
	@Override
	public void registerCallbackStartCrawler(IStartCrawlerCallback callback)
	{
		m_startCrawlerCallback = callback;
	}

	/**
	 * registers the callback for stopping the crawler
	 */
	@Override
	public void registerCallbackStopCrawler(IStopCrawlerCallback callback)
	{
		m_stopCrawlerCallback = callback;
	}

	private ISubscriberLoginCallback                  m_subscriberLoginCallback;
	private ISysAdminLoginCallback                    m_sysAdminLoginCallback;
	private ISubscriberRegisterCallback               m_subscriberRegistrationCallback;
	private ILogoutCallback                           m_logoutCallback;
	private IRemoveSubscriberCallback                 m_removeSubscriberCallback;
	private IFetchPendingVerificationRequestsCallback m_fetchPendingVerificationRequestsCallback;
	private IVerifySubscriberCallback                 m_verifySubscriberCallback;
	private IDenySubscriberCallback                   m_denySubscriberCallback;
	private IFetchSubscribersCallback                 m_fetchSubscribers;
	private IFetchOrganisationsCallback               m_fetchOrganisationsCallback;
	private IAddOrganisationCallback                  m_addOrganisationCallback;
	private IRemoveOrganisationCallback               m_removeOrganisationCallback;
	private IFetchProfilesCallback                    m_fetchProfilesCallback;
	private IFetchClippingsCallback                   m_fetchClippingsCallback;
	private IFetchTemplatesCallback                   m_fetchTemplatesCallback;
	private IAddTemplateCallback                      m_addTemplateCallback;
	private IRemoveTemplateCallback                   m_removeTemplateCallback;
	private IAddProfileCallback                       m_addProfileCallback;
	private IDeleteProfileCallback                    m_deleteProfileCallback;
	private IShareProfileCallback                     m_shareProfileCallback;
	private IUpdateProfileCallback                    m_updateProfileCallback;
	private IFetchSourcesCallback                     m_fetchSourcesCallback;
	private IAddSourceCallback                        m_addSourceCallback;
	private IRemoveSourceCallback                     m_removeSourceCallback;
	private IChangePasswordCallback                   m_changePasswordCallback;
	private IStartCrawlerCallback                     m_startCrawlerCallback;
	private IStopCrawlerCallback                      m_stopCrawlerCallback;
	// endregion Callbacks



	// region Events
	private void loginSuccessfulEvent()
	{
		if(whoAmI().isSystemAdministrator())
		{
			openSystemAdminDashboardView();
		}
		else
		{
			openSubscriberDashboardView();
		}
	}

	private void loginFailedEvent(String errorMessage)
	{
		Notification notification = new Notification("Anmeldung fehlgeschlagen\n"
				                                             + errorMessage);
		notification.setDelayMsec(3000);
		access(() -> notification.show(Page.getCurrent()));
	}

	private void registrationFailedEvent(String errorMessage)
	{
		Notification notification = new Notification("Registrierung fehlgeschlagen\n"
				                                             + errorMessage);
		notification.setDelayMsec(3000);
		access(() -> notification.show(Page.getCurrent()));
	}

	private void logoutEvent()
	{
		access(this::openLogoutView);
	}


	private void subscriberVerificationPendingEvent()
	{
		Notification notification = new Notification("Verifizierung ausstehend\nWarten Sie auf die " +
				                                             "Verifikation durch einen Administrator");
		notification.setDelayMsec(3000);
		access(() -> notification.show(Page.getCurrent()));
		access(m_subscriberLoginView::clearFields);
		access(m_subscriberRegistrationView::clearFields);
	}
	// endregion Events


	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
	public static class MainUIServlet extends VaadinServlet
			implements SessionInitListener, SessionDestroyListener
	{
		@Override
		public void init() throws ServletException
		{
			super.init();
		}

		@Override
		protected void servletInitialized() throws ServletException
		{
			super.servletInitialized();
			getService().addSessionInitListener(this);
			getService().addSessionDestroyListener(this);
		}

		@Override
		public void sessionInit(SessionInitEvent event)
		{
		}

		@Override
		public void sessionDestroy(SessionDestroyEvent event)
		{
			Collection<UI> sessionUIs = event.getSession().getUIs();
			for(UI ui : sessionUIs)
			{
				if(ui != null)
				{
					Presenter.getInstance().disconnect((MainUI)ui);
				}
			}
		}
	}
}
