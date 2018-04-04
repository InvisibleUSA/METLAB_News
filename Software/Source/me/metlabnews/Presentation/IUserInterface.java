package me.metlabnews.Presentation;



public interface IUserInterface extends IEventHandler
{
	// region Callback Functions
	// Callback Functions are called by a UserInterface
	// and executed by a class inside the Model package

	interface ICallback
	{ void execute(); }

	interface ISubscriberLoginCallback
	{ void execute(String email,
	               String password); }
	void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback);


	interface ISubscriberRegisterCallback
	{ void execute(String firstName,
	               String lastName,
	               String company,
	               String email,
	               String password); }
	void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback);


	void registerCallbackClientAdminLogin(ISubscriberLoginCallback callback);


	void registerCallbackClientAdminRegistration(ISubscriberRegisterCallback callback);


	void registerCallbackLogout(ICallback callback);



	// endregion Callback Functions
}
