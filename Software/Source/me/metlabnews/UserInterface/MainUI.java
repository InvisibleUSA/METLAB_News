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



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("maintheme")
@PreserveOnRefresh
@Push(PushMode.AUTOMATIC)
public class MainUI extends UI implements IUserInterface
{
	private SubscriberLoginView          m_subscriberLoginView;
	private SubscriberRegistrationView   m_subscriberRegistrationView;
	private SubscriberDashboardView      m_subscriberDashboardView;
	private SystemAdminLoginView         m_systemAdminLoginView;
	private SubscriberAdminDashboardView m_subscriberAdminDashboardView;
	private SystemAdminDashboardView     m_systemAdminDashboardView;
	private LogoutView                   m_logoutView;


	public MainUI()
	{
	}

	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		MainUIServlet.ui = this;

		m_subscriberLoginView = new SubscriberLoginView(this);
		m_systemAdminLoginView = new SystemAdminLoginView(this);
		m_subscriberRegistrationView = new SubscriberRegistrationView(this);
		m_subscriberDashboardView = new SubscriberDashboardView(this);
		m_subscriberAdminDashboardView = new SubscriberAdminDashboardView(this);
		m_systemAdminDashboardView = new SystemAdminDashboardView(this);
		m_logoutView = new LogoutView(this);

		Presenter.getInstance().connect(this);

		openSubscriberLoginView();
	}


	// region GUI Methods
	public void openSubscriberLoginView()
	{
		setContent(m_subscriberLoginView);
	}

	public void openSystemAdminLoginView()
	{

		setContent(m_systemAdminLoginView);
	}

	public void openSubscriberRegisterView()
	{

		setContent(m_subscriberRegistrationView);
	}

	private void openSubscriberDashboardView()
	{
		access(() -> setContent(m_subscriberDashboardView));
	}

	private void openClientAdminDashboardView()
	{
		access(() -> setContent(m_subscriberAdminDashboardView));
	}

	private void openSystemAdminDashboardView()
	{
		access(() -> setContent(m_systemAdminDashboardView));
	}

	public void openLogoutView()
	{
		access(() -> setContent(m_logoutView));
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

	public void logout()
	{
		m_logoutCallback.execute(this::logoutEvent);
	}


	public void removeSubscriber(IGenericEvent onSuccess,
	                             IGenericFailureEvent onFailure,
	                             String email)
	{
		m_removeSubscriberCallback.execute(onSuccess, onFailure, email);
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
	                           String email)
	{
		access(() -> m_denySubscriberCallback.execute(onSuccess, onFailure, email));
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
		m_FetchOrganisationsCallback.execute(onSuccess, onFailure);
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
	public void registerCallbackFetchPendingVerificationRequests(
			IFetchPendingVerificationRequestsCallback callback)
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
		m_FetchOrganisationsCallback = callback;
	}

	@Override
	public void registerCallbackLogout(ILogoutCallback callback)
	{
		m_logoutCallback = callback;
	}


	private ISubscriberLoginCallback                  m_subscriberLoginCallback;
	private ISysAdminLoginCallback                    m_sysAdminLoginCallback;
	private ISubscriberRegisterCallback               m_subscriberRegistrationCallback;
	private ILogoutCallback                           m_logoutCallback;
	private IRemoveSubscriberCallback                 m_removeSubscriberCallback;
	private IFetchPendingVerificationRequestsCallback m_fetchPendingVerificationRequestsCallback;
	private IVerifySubscriberCallback                 m_verifySubscriberCallback;
	private IDenySubscriberCallback                   m_denySubscriberCallback;
	private IFetchOrganisationsCallback               m_FetchOrganisationsCallback;
	private IAddOrganisationCallback                  m_addOrganisationCallback;
	private IRemoveOrganisationCallback               m_removeOrganisationCallback;

	// endregion Callbacks



	// region Events

	private void loginSuccessfulEvent()
	{
		UserDataRepresentation myself = Presenter.getInstance().whoAmI(this);
		if(myself.isSystemAdministrator())
		{
			openSystemAdminDashboardView();
		}
		else if(myself.isOrganisationAdministrator())
		{
			access(m_subscriberLoginView::clearFields);
			openClientAdminDashboardView();
		}
		else
		{
			access(m_subscriberLoginView::clearFields);
			openSubscriberDashboardView();
		}
	}

	private void loginFailedEvent(String errorMessage)
	{
		Notification popup = new Notification("Anmeldung fehlgeschlagen\n" + errorMessage,
		                                      Notification.Type.WARNING_MESSAGE);
		popup.setDelayMsec(3000);
		access(() -> popup.show(Page.getCurrent()));
	}


	private void logoutEvent()
	{
		access(this::openLogoutView);
	}


	private void subscriberVerificationPendingEvent()
	{
		access(() ->
		       {
			       Notification popup = new Notification("Verifizierung ausstehend\nWarten Sie auf die " +
					                                             "Verifikation durch einen Administrator",
			                                             Notification.Type.WARNING_MESSAGE);
			       popup.setDelayMsec(3000);
			       access(() -> popup.show(Page.getCurrent()));
			       m_subscriberLoginView.clearFields();
		       });
	}


	private void registrationFailedEvent(String errorMessage)
	{
		Notification popup = new Notification("Registrierung fehlgeschlagen\n" + errorMessage,
		                                      Notification.Type.WARNING_MESSAGE);
		popup.setDelayMsec(3000);
		access(() -> popup.show(Page.getCurrent()));
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

		static MainUI ui = null;

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
			Presenter.getInstance().disconnect(ui);
		}
	}
}
