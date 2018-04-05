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
import me.metlabnews.Presentation.SubscriberDataRepresentation;
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
	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		MainUIServlet.ui = this;
		Presenter.getInstance().connect(this);
		openSubscriberLoginView();
	}


	// region GUI Methods
	public void openSubscriberLoginView()
	{
		SubscriberLoginView view = new SubscriberLoginView(this);
		setContent(view);
	}

	public void openSystemAdminLoginView()
	{
		SystemAdminLoginView view = new SystemAdminLoginView(this);
		setContent(view);
	}

	public void openSubscriberRegisterView()
	{
		SubscriberRegisterView view = new SubscriberRegisterView(this);
		setContent(view);
	}

	public void openSubscriberDashboardView()
	{
		SubscriberDashboardView view = new SubscriberDashboardView(this);
		setContent(view);
	}

	public void openClientAdminDashboardView()
	{
		ClientAdminDashboardView view = new ClientAdminDashboardView(this);
		setContent(view);
	}

	public void openSystemAdminDashboardView()
	{
		SystemAdminDashboardView view = new SystemAdminDashboardView(this);
		setContent(view);
	}

	public void openLogoutView()
	{
		LogoutView view = new LogoutView(this);
		setContent(view);
	}



	public void subscriberLoginAction(String email, String pw)
	{
		subscriberLoginCallback.execute(email, pw);
	}

	public void sysAdminLoginAction(String email, String pw)
	{
		sysAdminLoginCallback.execute(email, pw);
	}

	public void subscriberRegisterAction(String firstName, String lastName, String company,
	                                     String email, String password, boolean clientAdmin)
	{
		subscriberRegisterCallback.execute(firstName, lastName, company, email,
		                                   password, clientAdmin);
	}

	public void userLogoutAction()
	{
		logoutCallback.execute();
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
		subscriberRegisterCallback = callback;
	}

	@Override
	public void registerCallbackSysAdminLogin(ISubscriberLoginCallback callback)
	{
		sysAdminLoginCallback = callback;
	}

	@Override
	public void registerCallbackLogout(ICallback callback)
	{
		logoutCallback = callback;
	}


	private ISubscriberLoginCallback    subscriberLoginCallback;
	private ISubscriberLoginCallback    sysAdminLoginCallback;
	private ISubscriberRegisterCallback subscriberRegisterCallback;

	private ICallback logoutCallback;
	// endregion Callbacks



	// region Events

	@Override
	public void subscriberLoginSuccessfulEvent(boolean asAdmin)
	{
		if(asAdmin)
		{
			openClientAdminDashboardView();
		}
		else
		{
			openSubscriberDashboardView();
		}
	}

	@Override
	public void subscriberLoginFailedEvent(String errorMessage)
	{
		Notification.show("Anmeldung fehlgeschlagen\n" + errorMessage);
	}

	@Override
	public void userLogoutEvent()
	{
		openLogoutView();
	}

	@Override
	public void subscriberVerificationPendingEvent()
	{
		Notification.show("Registrierung abgeschlossen\nWarte auf " +
				                  "Verifikation durch Administrator");
	}

	@Override
	public void subscriberRegistrationFailedEvent(String errorMessage)
	{
		Notification.show("Registrierung fehlgeschlagen\n" + errorMessage);
	}

	@Override
	public void subscriberVerificationSuccessfulEvent()
	{

	}

	@Override
	public void subscriberVerificationFailedEvent(String errorMessage)
	{

	}

	@Override
	public void subscriberVerificationDenialSuccessfulEvent()
	{

	}

	@Override
	public void getPendingVerificationRequestsSuccessfulEvent(SubscriberDataRepresentation[] data)
	{

	}

	@Override
	public void getPendingVerificationRequestsFailedEvent(String errorMessage)
	{

	}

	@Override
	public void sysAdminLoginSuccessfulEvent()
	{

	}

	@Override
	public void sysAdminLoginFailedEvent(String errorMessage)
	{

	}

	@Override
	public void addingOrganisationSuccessfulEvent()
	{

	}

	@Override
	public void addingOrganisationFailedEvent(String errorMessage)
	{

	}

	@Override
	public void deletingOrganisationSuccessfulEvent()
	{

	}

	@Override
	public void deletingOrganisationFailedEvent(String errorMessage)
	{

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
