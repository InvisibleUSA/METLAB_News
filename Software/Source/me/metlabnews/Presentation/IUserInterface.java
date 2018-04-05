package me.metlabnews.Presentation;



public interface IUserInterface extends ISubscriberEventHandler,
                                        IClientAdminEventHandler,
                                        ISysAdminEventHandler,
                                        ICommonEventHandler
{
	// region Callback Functions
	// Callback Functions are called by UserInterface
	// and executed by a class inside the Model package

	interface ICallback
	{ void execute(); }

	// region Subscriber Interaction
	interface ISubscriberLoginCallback
	{ void execute(String email,
	               String password); }
	void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback);


	interface ISubscriberRegisterCallback
	{ void execute(String firstName,
	               String lastName,
	               String company,
	               String email,
	               String password,
	               boolean clientAdmin); }
	void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback);

	// endregion Subscriber Interaction


	// region Client Admin Interaction

	// endregion Client Admin Interaction


	// region System Admin Interaction

	void registerCallbackSysAdminLogin(ISubscriberLoginCallback callback);

	// endregion System Admin Interaction


	// region Common Interaction

	void registerCallbackLogout(ICallback callback);

	// endregion Common Interaction

	// endregion Callback Functions
}
