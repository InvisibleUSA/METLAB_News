package me.metlabnews.Presentation;



public interface IUserInterface
{
	// region Events

	interface IGenericEvent
	{ void execute(); }

	interface IGenericFailureEvent
	{ void execute(String errorMessage); }

	interface IGetStringArrayEvent
	{ void execute(String[] result); }

	interface IFetchPendingVerificationRequestsEvent
	{ void execute(UserDataRepresentation[] data); }

	// endregion Events



	// region Callback Functions
	// Callback Functions are called by UserInterface
	// and executed by a class inside the Model package

	interface IGenericCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure); }



	// region Subscriber Interaction

	interface ISubscriberLoginCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericEvent onVerificationPending,
		             IGenericFailureEvent onFailure,
		             String email,
		             String password);
	}
	void registerCallbackSubscriberLogin(ISubscriberLoginCallback callback);


	interface ISubscriberRegisterCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure,
	               String firstName,
	               String lastName,
	               String company,
	               String email,
	               String password,
	               boolean clientAdmin); }
	void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback);


	interface IRemoveSubscriberCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure,
	               String email); }
	void registerCallbackSubscriberRemoval(IRemoveSubscriberCallback callback);

	// endregion Subscriber Interaction


	// region Client Admin Interaction

	interface IFetchPendingVerificationRequestsCallback
	{ void execute(IFetchPendingVerificationRequestsEvent onSuccess,
	               IGenericFailureEvent onFailure); }
	void registerCallbackFetchPendingVerificationRequests(
			IFetchPendingVerificationRequestsCallback callback);

	interface IVerifySubscriberCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure,
	               String subscriberEmail, boolean grantAdminStatus);
	}
	void registerCallbackVerifySubscriber(IVerifySubscriberCallback callback);

	interface IDenySubscriberCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure,
	               String email); }
	void registerCallbackDenySubscriber(IDenySubscriberCallback callback);

	// endregion Client Admin Interaction


	// region System Admin Interaction
	interface ISysAdminLoginCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email, String password); }
	void registerCallbackSysAdminLogin(ISysAdminLoginCallback callback);

	interface IAddOrganisationCallback { void execute(IGenericEvent onSuccess,
	                                                  IGenericFailureEvent onFailure,
	                                                  String organisationName,
	                                                  String adminFirstName,
	                                                  String adminLastName,
	                                                  String adminEmail,
	                                                  String adminPassword); }
	void registerCallbackAddOrganisation(IAddOrganisationCallback callback);


	interface IRemoveOrganisationCallback
	{ void execute(IGenericEvent onSuccess,
	               IGenericFailureEvent onFailure,
	               String organisationName); }
	void registerCallbackRemoveOrganisation(IRemoveOrganisationCallback callback);


	interface IFetchOrganisationsCallback
	{void execute(IGetStringArrayEvent onSuccess,
	              IGenericFailureEvent onFailure); }
	void registerCallbackFetchOrganisations(IFetchOrganisationsCallback callback);

	// endregion System Admin Interaction


	// region Common Interaction

	interface ILogoutCallback
	{ void execute(IGenericEvent onExecute); }
	void registerCallbackLogout(ILogoutCallback callback);

	// endregion Common Interaction

	// endregion Callback Functions
}
