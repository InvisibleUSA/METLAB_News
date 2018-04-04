package me.metlabnews.UserInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.Presenter;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.UserInterface.Views.SubscriberDashboardView;
import me.metlabnews.UserInterface.Views.SubscriberLoginView;
import me.metlabnews.UserInterface.Views.SubscriberRegisterView;



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
		Presenter.getInstance().connect(this);

		openUserLoginView();
	}


	// region GUI Methods
	public void openUserLoginView()
	{
		SubscriberLoginView view = new SubscriberLoginView(this);
		setContent(view);
	}

	public void openUserRegisterView()
	{
		SubscriberRegisterView view = new SubscriberRegisterView(this);
		setContent(view);
	}

	public void openUserDashboardView()
	{
		SubscriberDashboardView view = new SubscriberDashboardView(this);
		setContent(view);
	}

	public void userLoginAction(String email, String pw)
	{
		subscriberLoginCallback.execute(email, pw);
	}

	public void userRegisterAction(String firstName, String lastName, String company,
	                               String email, String password)
	{
		subscriberRegisterCallback.execute(firstName, lastName, company, email, password);
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
	public void registerCallbackClientAdminLogin(ISubscriberLoginCallback callback)
	{

	}

	@Override
	public void registerCallbackClientAdminRegistration(ISubscriberRegisterCallback callback)
	{

	}

	@Override
	public void registerCallbackLogout(ICallback callback)
	{
		logoutCallback = callback;
	}


	private ISubscriberLoginCallback    subscriberLoginCallback;
	private ISubscriberRegisterCallback subscriberRegisterCallback;

	private ICallback logoutCallback;
	// endregion Callbacks



	// region Events
	@Override
	public void subscriberLoginSuccessfulEvent()
	{
		openUserDashboardView();
	}

	@Override
	public void subscriberLoginFailedEvent(String errorMessage)
	{
		Notification.show("Anmeldung fehlgeschlagen\n" + errorMessage);
	}

	@Override
	public void subscriberLogoutEvent()
	{

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
	public void subscriberVerificationDeniedEvent()
	{

	}

	@Override
	public void clientAdminLoginSuccessfulEvent()
	{

	}

	@Override
	public void clientAdminLoginFailedEvent(String errorMessage)
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
	// endregion Events




	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
	public static class MainUIServlet extends VaadinServlet
	{
		@Override
		public void init() throws ServletException
		{
			super.init();
		}
	}
}
