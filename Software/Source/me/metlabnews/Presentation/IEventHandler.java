package me.metlabnews.Presentation;



interface IEventHandler
{
	// region Events
	// Events occur within the Model or Presentation
	// and are handled by a User Interface

	// region User Interaction
	// Deals with ordinary Users (Subscribers)

	// means that the user is now logged in
	void userLoginSuccessfulEvent();

	// means that the users attempt to login failed
	void userLoginFailedEvent(String errorMessage);

	// means that the user was registered and now has to wait until
	// he has been verified by a administrator
	void userRegistrationSuccessfulEvent();

	// means that the data entered by the user was invalid
	// (or the user simply is an annoying dipshit)
	void userRegistrationFailedEvent(String errorMessage);

	// means that the user was registered and verified, so now he can login
	void userVerificationSuccessfulEvent();

	// means that the user tried to register but was denied by an admin,
	// so now he can go fuck himself
	void userVerificationDeniedEvent();

	// endregion User Interaction


	// region Admin Interaction
	// Deals with Administrators of a client company

	// means that the admin is now logged in
	void adminLoginSuccessfulEvent();

	// means that the admins attempt to login failed
	void adminLoginFailedEvent(String errorMessage);
	// endregion Admin Interaction


	// region System Admin Interaction
	// Deals with System Administrators

	// means that the admin is now logged in
	void sysAdminLoginSuccessfulEvent();

	// means that the admins attempt to login failed
	void sysAdminLoginFailedEvent(String errorMessage);
	// endregion Admin Interaction
	// endregion Events
}
