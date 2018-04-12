package me.metlabnews.Presentation;

import me.metlabnews.Model.BusinessLogic.*;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;



public class Presenter
{
	public static Presenter getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new Presenter();
		}
		return m_instance;
	}


	private Presenter()
	{
		m_sessions = new ConcurrentHashMap<>(initialSessionCapacity);
	}


	public void connect(@NotNull IUserInterface ui)
	{
		Session session = new Session();

		registerCallbacks(ui, session);

		m_sessions.put(ui, session);
	}

	private void registerCallbacks(IUserInterface ui, Session session)
	{
		UserManager userManager = new UserManager();
		ui.registerCallbackSubscriberLogin((onSuccess, onVerificationPending,
		                                    onFailure, email, password) ->
				                                   userManager.subscriberLogin(session, onSuccess, email, password));

		ui.registerCallbackSubscriberRegistration((onSuccess, onFailure,
		                                           fName, lName, org, email, pw, admin) ->
				                                          userManager.registerNewSubscriber(session, email, pw,
				                                                                            fName, lName, org,
				                                                                            admin));

		ui.registerCallbackSysAdminLogin((onSuccess, onFailure, email, pw) ->
				                                 userManager.systemAdministratorLogin(session, email, pw));

		ui.registerCallbackLogout(session::logout);


		ui.registerCallbackFetchPendingVerificationRequests((onSuccess, onFailure) ->
				                                                    userManager.getPendingVerificationRequests(
						                                                    session));

		ui.registerCallbackVerifySubscriber((onSuccess, onFailure, email, grantAdminStatus) ->
				                                    userManager.verifySubscriber(session, email, grantAdminStatus));

		ui.registerCallbackDenySubscriber((onSuccess, onFailure, email) ->
				                                  userManager.denySubscriberVerification(session, email));

		//ui.registerCallbackSubscriberRemoval((onSuccess, onFailure, email) ->
		//userManager.removeSubscriber(session, onSuccess, onFailure, email));


		ui.registerCallbackAddOrganisation((onSuccess, onFailure, organisationName,
		                                    adminFirstName, adminLastName, adminEmail,
		                                    adminPassword) ->
				                                   userManager.addOrganisation(session,
				                                                               organisationName, adminFirstName,
				                                                               adminLastName,
				                                                               adminEmail, adminPassword));

		//ui.registerCallbackRemoveOrganisation((onSuccess, onFailure, organisationName) ->
		//userManager.removeOrganisation(session, onSuccess, onFailure, organisationName));

		//ui.registerCallbackFetchOrganisations((onSuccess, onFailure) ->
		//userManager.getAllOrganisations(session, onSuccess, onFailure));

	}


	public void disconnect(IUserInterface ui)
	{
		m_sessions.get(ui).close();
		m_sessions.remove(ui);
	}


	public UserDataRepresentation whoAmI(@NotNull IUserInterface sender)
			throws IllegalArgumentException
	{
		Session session = m_sessions.get(sender);
		if(session == null)
		{
			throw new IllegalArgumentException("invalid sender");
		}
		return new UserDataRepresentation(session.getUser());
	}



	private static Presenter m_instance = null;
	private ConcurrentHashMap<IUserInterface, Session> m_sessions;
	private final int initialSessionCapacity = 50;
}
