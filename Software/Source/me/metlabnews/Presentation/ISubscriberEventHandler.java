package me.metlabnews.Presentation;



// Events are called within the Model package and handled by the
// User Interface

// Events that occur during interaction with a subscriber
interface ISubscriberEventHandler
{
	// means that the user is now logged in
	void subscriberLoginSuccessfulEvent(boolean asAdmin);

	// means that the users attempt to login failed
	void subscriberLoginFailedEvent(String errorMessage);

	// means that the user was registered and now has to wait until
	// he has been verified by a administrator
	void subscriberVerificationPendingEvent();

	// means that the data entered by the user was invalid
	// (or the user simply is an annoying dipshit)
	void subscriberRegistrationFailedEvent(String errorMessage);
}
