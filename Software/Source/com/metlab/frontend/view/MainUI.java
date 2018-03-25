package com.metlab.frontend.view;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.metlab.frontend.uiController;
import com.metlab.frontend.ICallbackFunction;
import com.metlab.frontend.view.forms.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import javafx.beans.binding.ObjectExpression;



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("maintheme")
public class MainUI extends UI implements IView
{
	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		uiController.getInstance(this);
	}

	private ICallbackFunction userLoginEnterFormCallback     = null;
	private ICallbackFunction userLoginCallback              = null;
	private ICallbackFunction userRegisterEnterFormCallback  = null;
	private ICallbackFunction userRegisterCallback           = null;
	private ICallbackFunction userLogoutCallback             = null;
	private ICallbackFunction sysAdminLoginEnterFormCallback = null;
	private ICallbackFunction sysAdminLoginCallback          = null;
	private ICallbackFunction sysAdminLogoutCallback         = null;

	private UserLoginForm userLoginForm = null;
	private UserRegisterForm userRegisterForm = null;


	public void registerCallbackFunctions(
			ICallbackFunction userLoginEnterForm,
			ICallbackFunction userLogin,
			ICallbackFunction userRegisterEnterForm,
			ICallbackFunction userRegister,
			ICallbackFunction userLogout,
			ICallbackFunction sysAdminLoginEnterForm,
			ICallbackFunction sysAdminLogin,
			ICallbackFunction sysAdminLogout)
	{
		userLoginEnterFormCallback = userLoginEnterForm;
		userLoginCallback = userLogin;
		userRegisterEnterFormCallback = userRegisterEnterForm;
		userRegisterCallback = userRegister;
		userLogoutCallback = userLogout;
		sysAdminLoginEnterFormCallback = sysAdminLoginEnterForm;
		sysAdminLoginCallback = sysAdminLogin;
		sysAdminLogoutCallback = sysAdminLogout;
	}

	public void showUserLoginForm()
	{
		userLoginForm = new UserLoginForm(userLoginCallback, userRegisterEnterFormCallback, sysAdminLoginEnterFormCallback);
		setContent(userLoginForm);
	}

	public void showUserLoginError_NotRegistered()
	{
		userLoginForm.displayError_NotRegistered();
	}

	public void showUserLoginError_PasswordIncorrect()
	{
		userLoginForm.displayError_PasswordIncorrect();
	}

	public void showUserLoginError_UnknownError()
	{
		userLoginForm.displayError_UnknownError();
	}

	public void showUserRegisterForm()
	{
		setContent(new UserRegisterForm(userRegisterCallback, userLoginEnterFormCallback));
	}

	public void showUserRegisterError_RegistrationFailed()
	{
		userRegisterForm.displayError_RegistrationFailed();
	}

	public void showDashboardForm(String username, Boolean isAdmin)
	{
		setContent(new DashboardForm(username, isAdmin, userLogoutCallback));
	}

	public void showSysAdminLoginForm()
	{
		setContent(new SysAdminLoginForm(sysAdminLoginCallback, userLoginEnterFormCallback));
	}

	public void showSysAdminForm(String username)
	{
		setContent(new SysAdminForm(username, sysAdminLogoutCallback));
	}




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
