package me.metlabnews.Model.DataAccess.Exceptions;

public class LoggerFailedException extends Exception
{

	public LoggerFailedException()
	{
		super();
	}

	public LoggerFailedException(String message)
	{
		super(message);
	}

	public LoggerFailedException(Exception e)
	{
		super(e);
	}
}
