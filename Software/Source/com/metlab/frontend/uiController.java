package com.metlab.frontend;



import com.metlab.frontend.view.IView;


public class uiController
{
	private static uiController instance = null;
	private IView userInterface;
	private final String messageStart = "Message from uiController: ";

	public static uiController getInstance(final IView userInterface)
	{
		if(instance == null)
		{
			instance = new uiController(userInterface);
		}
		return instance;
	}


	private uiController(final IView userInterface)
	{
		System.out.print("\n\n\n****************************************************************************************************\n");
		System.out.println(messageStart + "software is running");

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
		System.out.println(messageStart + "user entered login form");
		userInterface.showUserLoginForm();
	}

	private void userEnterRegisterForm()
	{
		System.out.println(messageStart + "user entered register form");
		userInterface.showUserRegisterForm();
	}

	private void userLoginEvent(Object[] param)
	{
		String email = (String)param[0];
		String password = (String)param[1];
		System.out.println(messageStart + "user " + email +
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
		System.out.println(messageStart + "user " + email +
				" named '" + nameFirst + "' '" + nameLast + "' registered using password " +
				password + " with company code " + company);
		userInterface.showDashboardForm(email);
	}

	private void userLogoutEvent()
	{
		System.out.println(messageStart + "user logged out");
		userInterface.showUserLoginForm();
	}
}
