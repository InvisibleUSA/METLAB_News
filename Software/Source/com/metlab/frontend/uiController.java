package com.metlab.frontend;



import com.metlab.clippingDaemon.ClippingGenerator;
import com.metlab.crawler.CrawlerController;
import com.metlab.crawler.Source;
import com.metlab.frontend.controller.SQLController;
import com.metlab.frontend.view.IView;
import org.basex.query.value.item.Int;



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
		ClippingGenerator cg = new ClippingGenerator();
		new Thread(cg).start();

		CrawlerController cc = CrawlerController.getInstance();
		cc.setSleeptime(2 * 60 * 1000);
		cc.start();

		this.userInterface = userInterface;
		registerCallbackFunctionsInUI();

		userInterface.showUserLoginForm();

		System.out.print(
				"\n\n\n****************************************************************************************************\n");
		System.out.println(messageStart + "software is running");
	}


	private void registerCallbackFunctionsInUI()
	{
		userInterface.registerCallbackFunctions(
				(Object[] param) ->
				{
					userLoginEnterForm();
					return null;
				},
				(Object[] param) ->
				{
					userLoginEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					userRegisterEnterForm();
					return null;
				},
				(Object[] param) ->
				{
					userRegisterEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					userLogoutEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					sysAdminLoginEnterForm();
					return null;
				},
				(Object[] param) ->
				{
					sysAdminLoginEvent(param);
					return null;
				},
				(Object[] param) ->
				{
					sysAdminLogoutEvent(param);
					return null;
				});
	}

	private void userLoginEnterForm()
	{
		System.out.println(messageStart + "user entered login form");
		userInterface.showUserLoginForm();
	}

	private void userLoginEvent(Object[] param)
	{
		String email    = (String)param[0];
		String password = (String)param[1];
		System.out.println(messageStart + "user " + email +
				                   " logged in with password " + password);
		switch(SQLController.getInstance().loginUser(email, password))
		{
			case 0:
				userInterface.showUserLoginError_NotRegistered();
				return;
			case 1:
				userInterface.showUserLoginError_PasswordIncorrect();
				return;
			case 2:
				Boolean isAdmin = SQLController.getInstance().isAdmin(email);
				userInterface.showDashboardForm(email, isAdmin);
				return;
			case -1:
			default:
				userInterface.showUserLoginError_UnknownError();
		}
	}

	private void userRegisterEnterForm()
	{
		System.out.println(messageStart + "user entered register form");
		userInterface.showUserRegisterForm();
	}

	private void userRegisterEvent(Object[] param)
	{
		String email     = (String)param[0];
		String password  = (String)param[1];
		String nameFirst = (String)param[2];
		String nameLast  = (String)param[3];
		String company   = (String)param[4];
		System.out.println(messageStart + "user " + email +
				                   " named '" + nameFirst + "' '" + nameLast +
				                   "' registered using password " + password +
				                   " with company code " + company);
		if(SQLController.getInstance().registerUser(email, company, "N/A", nameLast, password, nameFirst))
		{
			Boolean isAdmin = SQLController.getInstance().isAdmin(email);
			userInterface.showDashboardForm(email, isAdmin);
		}
		else
		{
			userInterface.showUserRegisterError_RegistrationFailed();
		}
	}

	private void userLogoutEvent(Object[] param)
	{
		String email = (String)param[0];
		System.out.println(messageStart + "user " + email + " logged out");
		userInterface.showUserLoginForm();
	}

	private void sysAdminLoginEnterForm()
	{
		System.out.println(messageStart + "system admin entered login form");
		userInterface.showSysAdminLoginForm();
	}

	private void sysAdminLoginEvent(Object[] param)
	{
		String email    = (String)param[0];
		String password = (String)param[1];
		System.out.println(messageStart + "system admin " + email +
				                   " logged in with password " + password);
		userInterface.showSysAdminForm(email);
	}

	private void sysAdminLogoutEvent(Object[] param)
	{
		String email = (String)param[0];
		System.out.println(messageStart + "system admin " + email + " logged out");
		userInterface.showSysAdminLoginForm();
	}
}
