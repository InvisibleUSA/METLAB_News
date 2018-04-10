package me.metlabnews.Model.DataAccess.Queries;



public interface IQuery<T>
{
	boolean execute();
	T getResult();
}
