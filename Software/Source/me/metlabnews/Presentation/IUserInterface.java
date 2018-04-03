package me.metlabnews.Presentation;



public interface IUserInterface extends IEventHandler
{
	// region Callback Functions
	// Callback Functions are called by a UserInterface
	// and executed by a class inside the Model package

	interface IUserLoginCallback{ void execute(String email,
	                                           String password); }
	void registerUserLoginCallback(IUserLoginCallback callback);


	interface IUserRegisterCallback{ void execute(String firstName,
	                                              String lastName,
	                                              String company,
	                                              String email,
	                                              String password); }
	void registerUserRegisterCallback(IUserRegisterCallback callback);

	// endregion Callback Functions
}
