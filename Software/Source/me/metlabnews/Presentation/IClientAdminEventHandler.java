package me.metlabnews.Presentation;



// Events are called within the Model package and handled by the
// User Interface

// Events that occur during interaction with an client administrator
public interface IClientAdminEventHandler extends ISubscriberEventHandler
{
	// means that the admin verified a subscriber, who can log in now
	void subscriberVerificationSuccessfulEvent();

	// means that the admin tried to verify a subscriber, but an error occurred
	void subscriberVerificationFailedEvent(String errorMessage);

	// means that the admin denied a subscribers verification,
	// so now that subscriber can go fuck himself
	void subscriberVerificationDenialSuccessfulEvent();

	void getPendingVerificationRequestsSuccessfulEvent(SubscriberDataRepresentation[] data);

	void getPendingVerificationRequestsFailedEvent(String errorMessage);
}
