package me.metlabnews.Presentation;



// Events are called within the Model package and handled by the
// User Interface

// Events that occur during interaction with an system administrator
public interface ISysAdminEventHandler
{
	// means that the admin is now logged in
	void sysAdminLoginSuccessfulEvent();

	// means that the admins attempt to login failed
	void sysAdminLoginFailedEvent(String errorMessage);

	void addingOrganisationSuccessfulEvent();

	void addingOrganisationFailedEvent(String errorMessage);

	void deletingOrganisationSuccessfulEvent();

	void deletingOrganisationFailedEvent(String errorMessage);
}
