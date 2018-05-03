package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.MainUI;



/**
 * The dashboard for subscribers
 * Contains clippings and profiles options
 */
public class SubscriberDashboardView extends VerticalLayout
{
	public SubscriberDashboardView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Dashboard");

		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonQuitAccount.addClickListener(
				(Button.ClickEvent event) -> m_parent.removeSubscriber(m_parent::openLogoutView,
				                                                       errorMessage -> Notification.show(errorMessage),
				                                                       m_parent.whoAmI().getEmail()));

		m_buttonShowPendingVerificationRequests.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchPendingSubscriberVerifications(
						data -> showPendingVerificationRequests(data),
						errorMessage -> Notification.show(errorMessage)));

		m_buttonProfileCreate.addClickListener(
				(Button.ClickEvent event) -> m_parent.addProfile(null,
				                                                 errorMessage -> Notification.show(errorMessage),
				                                                 m_textProfileName.getValue(),
				                                                 m_textSources.getValue().split(" "),
				                                                 m_textKeywords.getValue().split(" "),
				                                                 m_textTime.getValue()));

		m_layoutHeaderBar.addComponents(m_title, m_buttonQuitAccount, m_buttonLogout);
		this.addComponent(m_layoutHeaderBar);
		m_layoutProfileBar.addComponents(m_panelProfiles, m_layoutProfileSidebar);
		m_layoutProfileSidebar.addComponents(m_textProfileName, m_textSources, m_textKeywords,
		                                     m_textTime, m_buttonProfileCreate);

	}

	public void showAdminLayout()
	{
		m_tabLayout.addTab(m_layoutProfileBar, "Abonnenten - Dashboard");
		m_tabLayout.addTab(m_adminLayout, "Administrator - Dashboard");
		m_adminLayout.addComponent(m_buttonShowPendingVerificationRequests);
		m_adminLayout.addComponent(m_panelSubscriberVerification);
		this.addComponent(m_tabLayout);
	}

	public void showSubscriberLayout()
	{
		this.addComponent(m_layoutProfileBar);
	}



	private MainUI m_parent;

	private final Label            m_title             = new Label("Willkommen bei METLAB-News - Dashboard");
	private final Button           m_buttonQuitAccount = new Button("Konto löschen");
	private final Button           m_buttonLogout      = new Button("Abmelden");
	private final HorizontalLayout m_layoutHeaderBar   = new HorizontalLayout();

	private final TabSheet         m_tabLayout                             = new TabSheet();
	private final HorizontalLayout m_layoutProfileBar                      = new HorizontalLayout();
	private final Panel            m_panelProfiles                         = new Panel("Profile");
	private final VerticalLayout   m_layoutProfileSidebar                  = new VerticalLayout();
	private final TextField        m_textProfileName                       = new TextField("Name:");
	private final TextField        m_textSources                           = new TextField(
			"Quellen: (mit Komma getennt)");
	private final TextField        m_textKeywords                          = new TextField(
			"Suchbegriffe: (siehe Quellen)");
	private final TextField        m_textTime                              = new TextField("Zeitpunkt HH:MM:SS");
	private final Button           m_buttonProfileCreate                   = new Button("Profil erstellen");
	private final VerticalLayout   m_adminLayout                           = new VerticalLayout();
	private final Button           m_buttonShowPendingVerificationRequests = new Button("Offene Anfragen abrufen");
	private final Panel            m_panelSubscriberVerification           = new Panel("Ausstehende Verifikationen");



	private void showPendingVerificationRequests(UserDataRepresentation[] data)
	{
		VerticalLayout table = new VerticalLayout();

		for(UserDataRepresentation subscriber : data)
		{
			HorizontalLayout row = new HorizontalLayout();
			row.addComponent(new Label(subscriber.getFirstName()));
			row.addComponent(new Label(subscriber.getLastName()));
			row.addComponent(new Label(subscriber.getEmail()));
			CheckBox grantAdminStatus = new CheckBox("Admin:");
			row.addComponent(grantAdminStatus);
			grantAdminStatus.setEnabled(subscriber.isOrganisationAdministrator());
			Button verify = new Button("Verifizieren");
			row.addComponent(verify);
			Button deny = new Button("Ablehnen");
			row.addComponent(deny);
			verify.addClickListener(
					event -> m_parent.verifySubscriber(() ->
					                                   {
						                                   verify.setEnabled(false);
						                                   deny.setEnabled(false);
					                                   },
					                                   errorMessage ->
							                                   Notification.show(errorMessage),
					                                   subscriber.getEmail(),
					                                   grantAdminStatus.getValue()));
			deny.addClickListener(
					event -> m_parent.denySubscriber(() ->
					                                 {
						                                 verify.setEnabled(false);
						                                 deny.setEnabled(false);
					                                 },
					                                 errorMessage ->
							                                 Notification.show(errorMessage),
					                                 subscriber.getEmail()));
			table.addComponent(row);
		}
		m_panelSubscriberVerification.setContent(table);
	}
}
