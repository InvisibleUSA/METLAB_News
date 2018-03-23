package com.metlab.controller;



import com.metlab.view.IView;
import com.metlab.view.MainUI;



public class Controller
{
	public static Controller getInstance(final IView userInterface)
	{
		if(instance == null)
		{
			instance = new Controller(userInterface);
		}
		return instance;
	}


	private Controller(final IView userInterface)
	{
		System.out.print("\n\n\nMessage from Controller: software is running\n\n");

		this.userInterface = userInterface;
		registerCallbackFunctionsInUI();

		userInterface.showUserLoginForm();
	}


	private void registerCallbackFunctionsInUI()
	{
		userInterface.registerCallbackFunctions(
				(Object[] param) ->
				{
					userEnterLoginForm();
					return null;
				},
				(Object[] param) ->
				{
					userLoginEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					userEnterRegisterForm();
					return null;
				},
				(Object[] param) ->
				{
					userRegisterEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					userLogoutEvent();
					return null;
				});
	}

	private void userEnterLoginForm()
	{
		System.out.println("Message from Controller: user entered login form");
		userInterface.showUserLoginForm();
	}

	private void userEnterRegisterForm()
	{
		System.out.println("Message from Controller: user entered register form");
		userInterface.showUserRegisterForm();
	}

	private void userLoginEvent(Object[] param)
	{
		String email = (String)param[0];
		String password = (String)param[1];
		System.out.println("Message from Controller: user " + email +
				" logged in with password " + password);
		userInterface.showDashboardForm(email);
	}

	private void userRegisterEvent(Object[] param)
	{
		String email = (String) param[0];
		String password = (String) param[1];
		String nameFirst = (String) param[2];
		String nameLast = (String) param[3];
		String company = (String) param[4];
		System.out.println("Message from Controller: user " + email +
				" named '" + nameFirst + "' '" + nameLast + "' registered using password " +
				password + " with company code " + company);
		userInterface.showDashboardForm(email);
	}

	private void userLogoutEvent()
	{
		System.out.println("Message from Controller: user logged out");
		userInterface.showUserLoginForm();
	}


	private static Controller instance = null;
	private IView userInterface;
}
