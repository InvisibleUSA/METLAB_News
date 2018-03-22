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
					userLoginEvent((String)param[0], (String)param[1]);
					return null;
				},
				(Object[] param) ->
				{
					userLogoutEvent();
					return null;
				});
	}


	private void userLoginEvent(String email, String password)
	{
		System.out.println("Message from Controller: user " + email
				           + " logged in with password " + password);
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
