package me.metlabnews.Presentation;



public interface IUserInterface
{
	interface IUserLoginCallback{ void execute(String email,
	                                           String password); }
	void registerUserLoginCallback(IUserLoginCallback callback);


	interface IUserRegisterCallback{ void execute(String firstName,
	                                              String lastName,
	                                              String email,
	                                              String password); }
	void registerUserRegisterCallback(IUserRegisterCallback callback);
}
