package me.metlabnews.Model.DataAccess.Exceptions;



public class UnexpectedNonUniqueDataException extends Exception
{
	public UnexpectedNonUniqueDataException()
	{
		super();
	}

	public UnexpectedNonUniqueDataException(String message)
	{
		super(message);
	}

	public UnexpectedNonUniqueDataException(Exception e)
	{
		super(e);
	}
}
