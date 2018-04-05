package me.metlabnews.Model.DataAccess;



public class DataUpdateFailedException extends Exception
{

	public DataUpdateFailedException()
	{
		super();
	}

	public DataUpdateFailedException(String message)
	{
		super(message);
	}

	public DataUpdateFailedException(Exception e)
	{
		super(e);
	}
}
