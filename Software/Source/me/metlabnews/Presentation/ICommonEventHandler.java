package me.metlabnews.Presentation;



// Events are called within the Model package and handled by the
// User Interface

// Events that are common along different kinds of interaction
public interface ICommonEventHandler
{
	// means that the user is now logged out
	void userLogoutEvent();
}
