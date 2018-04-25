package me.metlabnews.Model.Common;

public class MainForDebugging
{

	public MainForDebugging()
	{
		Logger.getInstance().register(this, Logger.Channel.Logger);
		Logger.getInstance().logError(this, "hallo");
	}


	public static void main(String[] args)
	{
		MainForDebugging m = new MainForDebugging();
	}
}