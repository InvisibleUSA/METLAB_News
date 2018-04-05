package me.metlabnews.Model.DataAccess;



public class RequestedDataDoesNotExistException extends Exception
{
	public RequestedDataDoesNotExistException()
	{
		super();
	}

	public RequestedDataDoesNotExistException(String message)
	{
		super(message);
	}

	public RequestedDataDoesNotExistException(Exception e)
	{
		super(e);
	}
}
