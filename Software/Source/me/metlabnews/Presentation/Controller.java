package me.metlabnews.Presentation;

public class Controller
{
	public static Controller getInstance()
	{
		if(instance == null)
		{
			instance = new Controller();
		}
		return instance;
	}

	private Controller()
	{
	}



	private static Controller instance = null;
}
