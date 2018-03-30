package me.metlabnews.Presentation;



import me.metlabnews.Model.BusinessLogic.UserManager;



public class Presenter
{
	public static Presenter getInstance()
	{
		if(instance == null)
		{
			instance = new Presenter();
		}
		return instance;
	}

	private Presenter()
	{
	}


	public void connect(IUserInterface ui)
	{
		ui.registerUserLoginCallback(UserManager.getInstance()::userLogin);
	}



	private static Presenter instance = null;
}
