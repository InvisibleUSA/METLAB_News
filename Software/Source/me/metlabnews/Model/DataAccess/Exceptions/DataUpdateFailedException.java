package me.metlabnews.Model.DataAccess.Exceptions;



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
