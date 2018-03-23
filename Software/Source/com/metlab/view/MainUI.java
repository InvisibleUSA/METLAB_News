package com.metlab.view;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.metlab.controller.*;
import com.metlab.view.forms.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;



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
		Controller.getInstance(this);
	}

	public void registerCallbackFunctions(
			ICallbackFunction userEnterLoginForm,
			ICallbackFunction userLogin,
			ICallbackFunction userEnterRegisterForm,
			ICallbackFunction userRegister,
			ICallbackFunction userLogout)
	{
		userEnterLoginFormCallback = userEnterLoginForm;
		userLoginCallback = userLogin;
		userEnterRegisterFormCallback = userEnterRegisterForm;
		userRegisterCallback = userRegister;
		userLogoutCallback = userLogout;
	}

	public void showUserLoginForm()
	{
		setContent(new UserLoginForm(userLoginCallback, userEnterRegisterFormCallback));
	}

	public void showUserRegisterForm()
	{
		setContent(new UserRegisterForm(userRegisterCallback, userEnterLoginFormCallback));
	}

	public void showDashboardForm(String username)
	{
		setContent(new DashboardForm(username, userLogoutCallback));
	}



	private ICallbackFunction userEnterLoginFormCallback = null;
	private ICallbackFunction userLoginCallback = null;
	private ICallbackFunction userEnterRegisterFormCallback = null;
	private ICallbackFunction userRegisterCallback = null;
	private ICallbackFunction userLogoutCallback = null;



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
