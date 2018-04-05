package me.metlabnews.Model.DataAccess;



public class DataCouldNotBeAddedException extends Exception
{
	public DataCouldNotBeAddedException()
	{
		super();
	}

	public DataCouldNotBeAddedException(String message)
	{
		super(message);
	}

	public DataCouldNotBeAddedException(Exception e)
	{
		super(e);
	}
}
