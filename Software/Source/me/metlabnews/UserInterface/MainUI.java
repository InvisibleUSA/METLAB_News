package me.metlabnews.UserInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
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
public class MainUI extends UI implements IUserInterface
{
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
		m_clientAdminDashboardView = new ClientAdminDashboardView(this);
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
		setContent(m_subscriberDashboardView);
	}

	private void openClientAdminDashboardView()
	{
		setContent(m_clientAdminDashboardView);
	}

	private void openSystemAdminDashboardView()
	{
		setContent(m_systemAdminDashboardView);
	}

	private void openLogoutView()
	{
		setContent(m_logoutView);
	}



	public void subscriberLoginAction(String email, String pw)
	{
		subscriberLoginCallback.execute(this::loginSuccessfulEvent,
		                                this::subscriberVerificationPendingEvent,
		                                this::loginFailedEvent,
		                                email, pw);
	}

	public void sysAdminLoginAction(String email, String pw)
	{
		sysAdminLoginCallback.execute(this::loginSuccessfulEvent,
		                              this::loginFailedEvent,
		                              email, pw);
	}

	public void subscriberRegistrationAction(String firstName, String lastName, String company,
	                                         String email, String password, boolean clientAdmin)
	{
		subscriberRegistrationCallback.execute(this::subscriberVerificationPendingEvent,
		                                       this::registrationFailedEvent,
		                                       firstName, lastName, company, email,
		                                       password, clientAdmin);
	}

	public void userLogoutAction()
	{
		logoutCallback.execute(this::logoutEvent);
	}
	// endregion GUI Methods



	// region Callbacks
	@Override
	public void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback)
	{
		subscriberLoginCallback = callback;
	}

	@Override
	public void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback)
	{
		subscriberRegistrationCallback = callback;
	}

	@Override
	public void registerCallbackSubscriberRemoval(IRemoveSubscriberCallback callback)
	{
		removeSubscriberCallback = callback;
	}

	@Override
	public void registerCallbackFetchPendingVerificationRequests(
			IFetchPendingVerificationRequestsCallback callback)
	{
		fetchPendingVerificationRequestsCallback = callback;
	}

	@Override
	public void registerCallbackVerifySubscriber(IVerifySubscriberCallback callback)
	{
		verifySubscriberCallback = callback;
	}

	@Override
	public void registerCallbackDenySubscriber(IDenySubscriberCallback callback)
	{
		denySubscriberCallback = callback;
	}

	@Override
	public void registerCallbackSysAdminLogin(ISysAdminLoginCallback callback)
	{
		sysAdminLoginCallback = callback;
	}

	@Override
	public void registerCallbackAddOrganisation(IAddOrganisationCallback callback)
	{
		addOrganisationCallback = callback;
	}

	@Override
	public void registerCallbackRemoveOrganisation(IRemoveOrganisationCallback callback)
	{
		removeOrganisationCallback = callback;
	}

	@Override
	public void registerCallbackLogout(ILogoutCallback callback)
	{
		logoutCallback = callback;
	}


	private ISubscriberLoginCallback    subscriberLoginCallback;
	private ISysAdminLoginCallback      sysAdminLoginCallback;
	private ISubscriberRegisterCallback subscriberRegistrationCallback;
	private ILogoutCallback             logoutCallback;
	private IRemoveSubscriberCallback   removeSubscriberCallback;
	private IFetchPendingVerificationRequestsCallback fetchPendingVerificationRequestsCallback;
	private IVerifySubscriberCallback   verifySubscriberCallback;
	private IDenySubscriberCallback     denySubscriberCallback;
	private IAddOrganisationCallback    addOrganisationCallback;
	private IRemoveOrganisationCallback removeOrganisationCallback;

	// endregion Callbacks



	// region Events
	public void genericErrorEvent(String errorMessage)
	{
		Notification.show("Fehler\n" + errorMessage);
	}


	public void loginSuccessfulEvent()
	{
		UserDataRepresentation myself = Presenter.getInstance().whoAmI(this);
		if(myself.isSystemAdministrator())
		{
			openSystemAdminDashboardView();
		}
		else if(myself.isOrganisationAdministrator())
		{
			openClientAdminDashboardView();
		}
		else
		{
			openSubscriberDashboardView();
		}
	}

	public void loginFailedEvent(String errorMessage)
	{
		Notification.show("Anmeldung fehlgeschlagen\n" + errorMessage);
	}


	public void logoutEvent()
	{
		openLogoutView();
	}


	public void subscriberVerificationPendingEvent()
	{
		Notification.show("Verifizierung ausstehend\nWarten Sie auf die " +
				                  "Verifikation durch einen Administrator");
	}


	public void registrationFailedEvent(String errorMessage)
	{
		Notification.show("Registrierung fehlgeschlagen\n" + errorMessage);
	}


	public void subscriberRemovedEvent()
	{

	}

	public void subscriberRemovalFailedEvent(String errorMessage)
	{

	}

	public void subscriberVerificationSuccessfulEvent()
	{

	}

	public void subscriberVerificationFailedEvent(String errorMessage)
	{

	}

	public void subscriberVerificationDenialSuccessfulEvent()
	{

	}

	public void getPendingVerificationRequestsSuccessfulEvent(UserDataRepresentation[] data)
	{

	}

	public void getPendingVerificationRequestsFailedEvent(String errorMessage)
	{

	}

	public void addingOrganisationSuccessfulEvent()
	{

	}

	public void addingOrganisationFailedEvent(String errorMessage)
	{

	}

	public void deletingOrganisationSuccessfulEvent()
	{

	}

	public void deletingOrganisationFailedEvent(String errorMessage)
	{

	}

	// endregion Events



	private SubscriberLoginView        m_subscriberLoginView;
	private SystemAdminLoginView       m_systemAdminLoginView;
	private SubscriberRegistrationView m_subscriberRegistrationView;
	private SubscriberDashboardView    m_subscriberDashboardView;
	private ClientAdminDashboardView   m_clientAdminDashboardView;
	private SystemAdminDashboardView   m_systemAdminDashboardView;
	private LogoutView                 m_logoutView;




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
		public void sessionInit(SessionInitEvent event)
		{
		}

		@Override
		protected void servletInitialized() throws ServletException
		{
			super.servletInitialized();
			getService().addSessionInitListener(this);
			getService().addSessionDestroyListener(this);
		}

		@Override
		public void sessionDestroy(SessionDestroyEvent event)
		{
			Presenter.getInstance().disconnect(ui);
		}
	}
}
