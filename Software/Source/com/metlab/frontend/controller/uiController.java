package com.metlab.frontend.controller;

import com.metlab.clippingDaemon.ClippingGenerator;
import com.metlab.controller.BaseXController;
import com.metlab.crawler.CrawlerController;
import com.metlab.crawler.Profile;
import com.metlab.crawler.Source;
import com.metlab.frontend.view.IView;
import org.basex.core.cmd.Add;
import org.basex.query.value.item.Str;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;



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
		cc.addSource(new Source("Spiegel", "http://www.spiegel.de/schlagzeilen/tops/index.rss"));
		cc.addSource(new Source("Süddeutsche", "http://rss.sueddeutsche.de/app/service/rss/alles/index.rss"));
		cc.addSource(new Source("Zeit", "http://newsfeed.zeit.de/index"));
		cc.addSource(new Source("Stuttgarter Zeitung", "https://www.stuttgarter-zeitung.de/news.rss.feed"));
		cc.addSource(new Source("MAZ", "http://www.maz-online.de/rss/feed/maz_brandenburg"));
		cc.addSource(new Source("Gamestar", "http://www.gamestar.de/news/rss/news.rss"));
		cc.addSource(new Source("Kino.de", "https://www.kino.de/rss/neu-im-kino"));
		cc.addSource(new Source("Sumikai", "https://sumikai.com/feed/"));
		cc.addSource(new Source("Netzpolitik.org", "https://netzpolitik.org/feed"));
		cc.addSource(new Source("Nachdenkseiten", "https://www.nachdenkseiten.de/?feed=rss2"));
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
				},
				(Object[] param) ->
				{
					createProfileEvent(param);
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

	private void createProfileEvent(Object[] param)
	{
		String       profileName = (String)param[0];
		String       email       = (String)param[1];
		List<String> sources     = Arrays.asList(((String)param[2]).split(","));
		List<String> keywords    = Arrays.asList(((String)param[3]).split(","));
		String       time        = (String)param[4];

		StringBuilder xml = new StringBuilder();
		xml.append("<profile><name>" + profileName + "</name>" +
				           "<owner>" + email + "</owner>" +
				           "<generationtime>" + time + "</generationtime>" +
				           "<keywords>");
		for(String key : keywords)
		{
			xml.append("<keyword>" + key + "</keyword>");
		}
		xml.append("</keywords><sources>");
		for(String src : sources)
		{
			xml.append("<source>" + src + "</source>");
		}
		xml.append("</sources></profile>");

		Add add = new Add("/profiles/" + profileName, xml.toString());
		BaseXController.getInstance().execute(add);
	}
}
