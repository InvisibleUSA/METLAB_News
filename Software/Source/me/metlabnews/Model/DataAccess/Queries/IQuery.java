package me.metlabnews.Model.DataAccess.Queries;



public interface IQuery<T>
{
	/**
	 * @return returns false in case of an error
	 *         returns true otherwise
	 */
	boolean execute();


	/**
	 * @return returns null if execute() has not been executed
	 */
	T getResult();
}
