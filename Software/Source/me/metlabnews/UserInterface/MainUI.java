package me.metlabnews.UserInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.Presenter;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.UserInterface.Views.UserDashboardView;
import me.metlabnews.UserInterface.Views.UserLoginView;
import me.metlabnews.UserInterface.Views.UserRegisterView;



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("maintheme")
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
		UserLoginView view = new UserLoginView(this);
		setContent(view);
	}

	public void openUserRegisterView()
	{
		UserRegisterView view = new UserRegisterView(this);
		setContent(view);
	}

	public void openUserDashboardView()
	{
		UserDashboardView view = new UserDashboardView(this);
		setContent(view);
	}

	public void userLoginAction(String email, String pw)
	{
		userLoginCallback.execute(email, pw);
	}

	public void userRegisterAction(String firstName, String lastName, String company,
	                               String email, String password)
	{
		userRegisterCallback.execute(firstName, lastName, company, email, password);
	}
	// endregion GUI Methods



	// region Callbacks
	@Override
	public void registerUserLoginCallback(IUserLoginCallback callback)
	{
		userLoginCallback = callback;
	}

	@Override
	public void registerUserRegisterCallback(IUserRegisterCallback callback)
	{
		userRegisterCallback = callback;
	}

	private IUserLoginCallback userLoginCallback;
	private IUserRegisterCallback userRegisterCallback;
	// endregion Callbacks



	// region Events
	@Override
	public void userLoginSuccessfulEvent()
	{
		openUserDashboardView();
	}

	@Override
	public void userLoginFailedEvent(String errorMessage)
	{
		Notification.show("Anmeldung fehlgeschlagen\n" + errorMessage);
	}

	@Override
	public void userRegistrationSuccessfulEvent()
	{
		openUserDashboardView();
	}

	@Override
	public void userRegistrationFailedEvent(String errorMessage)
	{
		Notification.show("Registrierung fehlgeschlagen\n" + errorMessage);
	}

	@Override
	public void userVerificationSuccessfulEvent()
	{

	}

	@Override
	public void userVerificationDeniedEvent()
	{

	}

	@Override
	public void adminLoginSuccessfulEvent()
	{

	}

	@Override
	public void adminLoginFailedEvent(String errorMessage)
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
