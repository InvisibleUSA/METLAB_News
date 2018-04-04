package me.metlabnews.Presentation;



interface IEventHandler
{
	// region Events
	// Events occur within the Model or Presentation
	// and are handled by a User Interface

	// region Subscriber Interaction

	// means that the user is now logged in
	void subscriberLoginSuccessfulEvent();

	// means that the users attempt to login failed
	void subscriberLoginFailedEvent(String errorMessage);

	// means that the user is now logged out
	void subscriberLogoutEvent();

	// means that the user was registered and now has to wait until
	// he has been verified by a administrator
	void subscriberVerificationPendingEvent();

	// means that the data entered by the user was invalid
	// (or the user simply is an annoying dipshit)
	void subscriberRegistrationFailedEvent(String errorMessage);

	// means that the user was registered and verified, so now he can login
	void subscriberVerificationSuccessfulEvent();

	// means that the user tried to register but was denied by an admin,
	// so now he can go fuck himself
	void subscriberVerificationDeniedEvent();

	// endregion Subscriber Interaction


	// region Client Admin Interaction
	// Deals with Administrators of a client company

	// means that the admin is now logged in
	void clientAdminLoginSuccessfulEvent();

	// means that the admins attempt to login failed
	void clientAdminLoginFailedEvent(String errorMessage);
	// endregion Client Admin Interaction


	// region System Admin Interaction
	// Deals with System Administrators

	// means that the admin is now logged in
	void sysAdminLoginSuccessfulEvent();

	// means that the admins attempt to login failed
	void sysAdminLoginFailedEvent(String errorMessage);
	// endregion Admin Interaction
	// endregion Events
}
