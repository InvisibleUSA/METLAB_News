package me.metlabnews.Model.DataAccess.Exceptions;



public class UnexpectedDataException extends Exception
{
	public UnexpectedDataException()
	{
		super();
	}

	public UnexpectedDataException(String message)
	{
		super(message);
	}

	public UnexpectedDataException(Exception e)
	{
		super(e);
	}
}
