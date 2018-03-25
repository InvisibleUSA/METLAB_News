package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class DashboardForm extends VerticalLayout
{
	private final Label            title               = new Label();
	private final Button           buttonLogout        = new Button("Abmelden");
	private final HorizontalLayout headerBar           = new HorizontalLayout();
	private final TabSheet         layoutOptional      = new TabSheet();
	private final HorizontalLayout layoutUser          = new HorizontalLayout();
	private final Panel            placeholder         = new Panel("Platzhalter");
	private final VerticalLayout   profileSidebar      = new VerticalLayout();
	private final TextField        profileName         = new TextField("Profilbezeichnung");
	private final TextField        email               = new TextField("Email des Empfängers");
	private final TextField        sources             = new TextField("Quellen mit Komma getrennt");
	private final TextField        keywords            = new TextField("Suchbegriffe mit Komma getrennt");
	private final TextField        time                = new TextField("Zeitpunkt HH:MM:SS");
	private final Button           buttonCreateProfile = new Button("Profil erstellen");
	private final VerticalLayout   layoutAdmin         = new VerticalLayout();
	private final CheckBoxGroup    userList            = new CheckBoxGroup();
	private final HorizontalLayout buttonBar           = new HorizontalLayout();
	private final Button           buttonAddUser       = new Button("Nutzer hinzufügen");
	private final Button           buttonRemoveUser    = new Button("Ausgewählte Nutzer entfernen");



	public DashboardForm(String userName,
	                     Boolean isAdmin,
	                     ICallbackFunction userLogoutCallback, ICallbackFunction createProfileCallback)
	{
		Page.getCurrent().setTitle("METLAB News");
		title.setCaption("Nutzeranmeldung erfolgreich - Hallo " + userName);

		buttonLogout.addClickListener((Button.ClickEvent event) ->
				                              userLogoutCallback.execute(new String[] {userName}));
		buttonCreateProfile.addClickListener((Button.ClickEvent event) ->
				                                     createProfileCallback.execute(
						                                     (new String[] {profileName.getValue(), email.getValue(),
								                                     sources.getValue(), keywords.getValue(),
								                                     time.getValue()})));

		profileSidebar.addComponents(profileName, email, sources, keywords, time, buttonCreateProfile);
		layoutUser.addComponents(placeholder, profileSidebar);
		headerBar.addComponents(title, buttonLogout);
		if(isAdmin)
		{
			layoutOptional.addTab(layoutUser, "Nutzer");
			layoutOptional.addTab(layoutAdmin, "Admin");
			layoutAdmin.addComponents(userList, buttonBar);
			buttonBar.addComponents(buttonAddUser, buttonRemoveUser);
			this.addComponents(headerBar, layoutOptional);
		}
		else
		{
			this.addComponents(headerBar, layoutUser);
		}
	}
}