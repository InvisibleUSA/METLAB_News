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
import java.util.Collection;



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 *
 * @author Achim Groß
 */
@Theme("maintheme")
@PreserveOnRefresh
@Push(PushMode.AUTOMATIC)
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


	// region GUI Methods
	public void openSubscriberLoginView()
	{
		m_subscriberLoginView.show();
		access(() -> Page.getCurrent().setTitle("Anmelden"));
	}

	public void openSystemAdminLoginView()
	{
		m_systemAdminLoginView.show();
		access(() -> Page.getCurrent().setTitle("Anmelden"));
	}

	public void openSubscriberRegisterView()
	{
		m_subscriberRegistrationView.show();
		access(() -> Page.getCurrent().setTitle("Registrieren"));
	}

	private void openSubscriberDashboardView()
	{
		m_subscriberDashboardView.show();
		access(() -> Page.getCurrent().setTitle("Dashboard"));
	}

	private void openSystemAdminDashboardView()
	{
		m_systemAdminDashboardView.show();
		access(() -> Page.getCurrent().setTitle("Dashboard"));
	}

	public void openLogoutView()
	{
		m_logoutView.show();
		access(() -> Page.getCurrent().setTitle("Abgemeldet"));
	}



	public void loginSubscriber(String email, String pw)
	{
		m_subscriberLoginCallback.execute(this::loginSuccessfulEvent,
		                                  this::subscriberVerificationPendingEvent,
		                                  this::loginFailedEvent,
		                                  email, pw);
	}

	public void loginSysAdmin(String email, String pw)
	{
		m_sysAdminLoginCallback.execute(this::loginSuccessfulEvent,
		                                this::loginFailedEvent,
		                                email, pw);
	}

	public void registerSubscriber(String firstName, String lastName, String company,
	                               String email, String password, boolean clientAdmin)
	{
		m_subscriberRegistrationCallback.execute(this::subscriberVerificationPendingEvent,
		                                         this::registrationFailedEvent,
		                                         firstName, lastName, company, email,
		                                         password, clientAdmin);
	}

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

	public void logout()
	{
		m_logoutCallback.execute(this::logoutEvent);
	}

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

	public void removeSubscriber(IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String email,
	                             Date date)
	{
		m_removeSubscriberCallback.execute(onSuccess, onFailure, email, date);
	}

	public void fetchPendingSubscriberVerifications(
			IFetchPendingVerificationRequestsEvent onSuccess,
			IGenericFailureEvent onFailure)
	{
		m_fetchPendingVerificationRequestsCallback.execute(onSuccess, onFailure);
	}

	public void verifySubscriber(IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String subscriberEmail, boolean grantAdminStatus)
	{
		m_verifySubscriberCallback.execute(onSuccess, onFailure, subscriberEmail, grantAdminStatus);
	}

	public void denySubscriber(IGenericEvent onSuccess,
	                           IGenericFailureEvent onFailure,
	                           String email,
	                           Date date)
	{
		access(() -> m_denySubscriberCallback.execute(onSuccess, onFailure, email, date));
	}

	public void fetchSubscribers(IFetchSubscribersEvent onSuccess,
	                             IGenericFailureEvent onFailure)
	{
		m_fetchSubscribers.execute(onSuccess, onFailure);
	}

	public void addOrganisation(IGenericEvent onSuccess,
	                            IGenericFailureEvent onFailure,
	                            String organisationName,
	                            String adminFirstName,
	                            String adminLastName,
	                            String adminEmail,
	                            String adminPassword)
	{
		m_addOrganisationCallback.execute(onSuccess, onFailure, organisationName,
		                                  adminFirstName, adminLastName, adminEmail,
		                                  adminPassword);
	}

	public void removeOrganisation(IGenericEvent onSuccess,
	                               IGenericFailureEvent onFailure,
	                               String organisationName)
	{
		m_removeOrganisationCallback.execute(onSuccess, onFailure, organisationName);
	}

	public void getAllOrganisations(IGetStringArrayEvent onSuccess,
	                                IGenericFailureEvent onFailure)
	{
		m_fetchOrganisationsCallback.execute(onSuccess, onFailure);
	}

	public void fetchProfiles(IFetchProfilesEvent onSuccess,
	                          IGenericFailureEvent onFailure)
	{
		m_fetchProfilesCallback.execute(onSuccess, onFailure);
	}

	public void fetchClippings(IFetchClippingsEvent onSuccess,
	                           IGenericFailureEvent onFailure)
	{
		m_fetchClippingsCallback.execute(onSuccess, onFailure);
	}

	public void fetchTemplates(IFetchProfilesEvent onSuccess,
	                           IGenericFailureEvent onFailure)
	{
		m_fetchTemplatesCallback.execute(onSuccess, onFailure);
	}

	public void addTemplate(IGenericEvent onSuccess,
	                        IGenericFailureEvent onFailure,
	                        String templateName,
	                        String[] keywords,
	                        String[] sources)
	{
		m_addTemplateCallback.execute(onSuccess, onFailure, templateName, keywords, sources);
	}

	public void removeTemplate(IGenericEvent onSuccess,
	                           IGenericFailureEvent onFailure,
	                           String templateId)
	{
		m_removeTemplateCallback.execute(onSuccess, onFailure, templateId);
	}

	public void addProfile(IGenericEvent onSuccess,
	                       IGenericFailureEvent onFailure,
	                       String profileName,
	                       String[] sources,
	                       String[] keywords,
	                       Duration interval)
	{
		m_addProfileCallback.execute(onSuccess, onFailure, profileName, sources, keywords, interval);
	}

	public void deleteProfile(IGenericEvent onSuccess,
	                          IGenericFailureEvent onFailure,
	                          String ownerMail,
	                          String profileId)
	{
		m_deleteProfileCallback.execute(onSuccess, onFailure, ownerMail, profileId);
	}

	public void shareProfile(IGenericEvent onSuccess,
	                         IGenericFailureEvent onFailure,
	                         String profileId,
	                         String receiverEmail)
	{
		m_shareProfileCallback.execute(onSuccess, onFailure, whoAmI().getEmail(), profileId, receiverEmail);
	}

	public void updateProfileAction(IGenericEvent onSuccess,
	                                IGenericFailureEvent onFailure, String profileName,
	                                String[] keywords, String[] sources, Duration interval, boolean isActive)
	{
		m_updateProfileCallback.execute(onSuccess, onFailure, profileName, keywords, sources, interval, isActive);
	}

	public void fetchSources(IFetchSourcesEvent onSuccess,
	                         IGenericFailureEvent onFailure)
	{
		m_fetchSourcesCallback.execute(onSuccess, onFailure);
	}

	public void addSource(IGenericEvent onSuccess,
	                      IGenericFailureEvent onFailure,
	                      String name, String link, String rssLink)
	{
		m_addSourceCallback.execute(onSuccess, onFailure, name, link, rssLink);
	}

	public void removeSource(IGenericEvent onSuccess,
	                         IGenericFailureEvent onFailure,
	                         String name)
	{
		m_removeSourceCallback.execute(onSuccess, onFailure, name);
	}

	// endregion GUI Methods

	public UserDataRepresentation whoAmI()
	{
		return Presenter.getInstance().whoAmI(this);
	}


	// region Callbacks
	@Override
	public void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback)
	{
		m_subscriberLoginCallback = callback;
	}

	@Override
	public void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback)
	{
		m_subscriberRegistrationCallback = callback;
	}

	@Override
	public void registerCallbackSubscriberRemoval(IRemoveSubscriberCallback callback)
	{
		m_removeSubscriberCallback = callback;
	}

	@Override
	public void registerCallbackFetchPendingVerificationRequests(IFetchPendingVerificationRequestsCallback callback)
	{
		m_fetchPendingVerificationRequestsCallback = callback;
	}

	@Override
	public void registerCallbackVerifySubscriber(IVerifySubscriberCallback callback)
	{
		m_verifySubscriberCallback = callback;
	}

	@Override
	public void registerCallbackDenySubscriber(IDenySubscriberCallback callback)
	{
		m_denySubscriberCallback = callback;
	}

	@Override
	public void registerCallbackFetchSubscribers(IFetchSubscribersCallback callback)
	{
		m_fetchSubscribers = callback;
	}

	@Override
	public void registerCallbackSysAdminLogin(ISysAdminLoginCallback callback)
	{
		m_sysAdminLoginCallback = callback;
	}

	@Override
	public void registerCallbackAddOrganisation(IAddOrganisationCallback callback)
	{
		m_addOrganisationCallback = callback;
	}

	@Override
	public void registerCallbackRemoveOrganisation(IRemoveOrganisationCallback callback)
	{
		m_removeOrganisationCallback = callback;
	}

	@Override
	public void registerCallbackFetchOrganisations(IFetchOrganisationsCallback callback)
	{
		m_fetchOrganisationsCallback = callback;
	}

	@Override
	public void registerCallbackChangePW(IChangePasswordCallback callback)
	{
		m_changePasswordCallback = callback;
	}

	@Override
	public void registerCallbackLogout(ILogoutCallback callback)
	{
		m_logoutCallback = callback;
	}

	@Override
	public void registerCallbackFetchProfiles(IFetchProfilesCallback callback)
	{
		m_fetchProfilesCallback = callback;
	}

	@Override
	public void registerCallbackFetchClippings(IFetchClippingsCallback callback)
	{
		m_fetchClippingsCallback = callback;
	}

	@Override
	public void registerCallbackFetchTemplates(IFetchTemplatesCallback callback)
	{
		m_fetchTemplatesCallback = callback;
	}

	@Override
	public void registerCallbackAddTemplate(IAddTemplateCallback callback)
	{
		m_addTemplateCallback = callback;
	}

	@Override
	public void registerCallbackRemoveTemplate(IRemoveTemplateCallback callback)
	{
		m_removeTemplateCallback = callback;
	}

	@Override
	public void registerCallbackAddProfile(IAddProfileCallback callback)
	{
		m_addProfileCallback = callback;
	}

	@Override
	public void registerCallbackDeleteProfile(IDeleteProfileCallback callback)
	{
		m_deleteProfileCallback = callback;
	}

	@Override
	public void registerCallbackShareProfile(IShareProfileCallback callback)
	{
		m_shareProfileCallback = callback;
	}

	@Override
	public void registerCallbackUpdateProfile(IUpdateProfileCallback callback)
	{
		m_updateProfileCallback = callback;
	}

	@Override
	public void registerCallbackFetchSources(IFetchSourcesCallback callback)
	{
		m_fetchSourcesCallback = callback;
	}

	@Override
	public void registerCallbackAddSource(IAddSourceCallback callback)
	{
		m_addSourceCallback = callback;
	}

	@Override
	public void registerCallbackRemoveSource(IRemoveSourceCallback callback)
	{
		m_removeSourceCallback = callback;
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
	// endregion Callbacks



	// region Events
	private void loginSuccessfulEvent()
	{
		access(m_systemAdminLoginView::clearFields);
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
