package me.metlabnews.Presentation;



import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



public interface IUserInterface
{
	// region Events

	interface IGenericEvent
	{
		void execute();
	}

	interface IGenericFailureEvent
	{
		void execute(String errorMessage);
	}

	interface IGetStringArrayEvent
	{
		void execute(String[] result);
	}

	interface IFetchProfilesEvent
	{
		void execute(ProfileDataRepresentation[] data);
	}

	interface IFetchTemplatesEvent
	{
		void execute(ProfileTemplateDataRepresentation[] data);
	}

	interface IFetchClippingsEvent
	{
		void execute(ClippingDataRepresentation[] data);
	}

	interface IFetchPendingVerificationRequestsEvent
	{
		void execute(UserDataRepresentation[] data);
	}

	interface IFetchSubscribersEvent
	{
		void execute(UserDataRepresentation[] data);
	}

	interface IFetchSourcesEvent
	{
		void execute(SourceDataRepresentation[] data);
	}

	// endregion Events



	// region Callback Functions
	// Callback Functions are called by UserInterface
	// and executed by a class inside the Model package

	interface IGenericCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}



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
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String firstName,
		             String lastName,
		             String company,
		             String email,
		             String password,
		             boolean clientAdmin);
	}

	void registerCallbackSubscriberRegistration(ISubscriberRegisterCallback callback);


	interface IRemoveSubscriberCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email,
		             Date date);
	}

	void registerCallbackSubscriberRemoval(IRemoveSubscriberCallback callback);


	interface IFetchProfilesCallback
	{
		void execute(IFetchProfilesEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchProfiles(IFetchProfilesCallback callback);


	interface IFetchClippingsCallback
	{
		void execute(IFetchClippingsEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String profileID);
	}

	void registerCallbackFetchClippings(IFetchClippingsCallback callback);


	interface IFetchTemplatesCallback
	{
		void execute(IFetchTemplatesEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchTemplates(IFetchTemplatesCallback callback);


	interface IAddTemplateCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String templateName,
		             String[] keywords,
		             String[] sources);
	}

	void registerCallbackAddTemplate(IAddTemplateCallback callback);


	interface IRemoveTemplateCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String templateName);
	}

	void registerCallbackRemoveTemplate(IRemoveTemplateCallback callback);


	interface IAddProfileCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String profileName,
		             String[] sources,
		             String[] keywords,
		             Duration interval);
	}

	void registerCallbackAddProfile(IAddProfileCallback callback);

	interface IUpdateProfileCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String profileID,
		             String profileName,
		             String[] sources,
		             String[] keywords,
		             Duration interval,
		             boolean isActive);
	}
	void registerCallbackUpdateProfile(IUpdateProfileCallback callback);


	interface IDeleteProfileCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String ownerEmail,
		             String profileName);
	}

	void registerCallbackDeleteProfile(IDeleteProfileCallback callback);


	interface IShareProfileCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String senderEmail, String profileId, String receiverEmail);
	}

	void registerCallbackShareProfile(IShareProfileCallback callback);


	// endregion Subscriber Interaction


	// region Client Admin Interaction

	interface IFetchPendingVerificationRequestsCallback
	{
		void execute(IFetchPendingVerificationRequestsEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchPendingVerificationRequests(
			IFetchPendingVerificationRequestsCallback callback);


	interface IVerifySubscriberCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email, boolean grantAdminStatus);
	}

	void registerCallbackVerifySubscriber(IVerifySubscriberCallback callback);


	interface IDenySubscriberCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email,
		             Date date);
	}

	void registerCallbackDenySubscriber(IDenySubscriberCallback callback);


	interface IFetchSubscribersCallback
	{
		void execute(IFetchSubscribersEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchSubscribers(IFetchSubscribersCallback callback);

	// endregion Client Admin Interaction


	// region System Admin Interaction
	interface ISysAdminLoginCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email, String password);
	}

	void registerCallbackSysAdminLogin(ISysAdminLoginCallback callback);

	interface IAddOrganisationCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String organisationName,
		             String adminFirstName,
		             String adminLastName,
		             String adminEmail,
		             String adminPassword);
	}

	void registerCallbackAddOrganisation(IAddOrganisationCallback callback);


	interface IRemoveOrganisationCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String organisationName);
	}

	void registerCallbackRemoveOrganisation(IRemoveOrganisationCallback callback);


	interface IFetchOrganisationsCallback
	{
		void execute(IGetStringArrayEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchOrganisations(IFetchOrganisationsCallback callback);

	void registerCallbackChangePW(IChangePasswordCallback callback);


	interface IFetchSourcesCallback
	{
		void execute(IFetchSourcesEvent onSuccess,
		             IGenericFailureEvent onFailure);
	}

	void registerCallbackFetchSources(IFetchSourcesCallback callback);


	interface IAddSourceCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String name, String link, String rssLink);
	}

	void registerCallbackAddSource(IAddSourceCallback callback);


	interface IRemoveSourceCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String name);
	}


	interface IChangePasswordCallback
	{
		void execute(IGenericEvent onSuccess,
		             IGenericFailureEvent onFailure,
		             String email,
		             String oldPW,
		             String newPW);
	}

	void registerCallbackRemoveSource(IRemoveSourceCallback callback);


	interface IStartCrawlerCallback
	{
		void execute();
	}

	void registerCallbackStartCrawler(IStartCrawlerCallback callback);


	interface IStopCrawlerCallback
	{
		void execute();
	}

	void registerCallbackStopCrawler(IStopCrawlerCallback callback);

	// endregion System Admin Interaction


	// region Common Interaction

	interface ILogoutCallback
	{
		void execute(IGenericEvent onExecute);
	}

	void registerCallbackLogout(ILogoutCallback callback);

	// endregion Common Interaction

	// endregion Callback Functions
}
