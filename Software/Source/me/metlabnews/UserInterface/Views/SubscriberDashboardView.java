package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.Helpers.VerifySubscriber_GridHelper;
import me.metlabnews.UserInterface.MainUI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



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

		m_textTime.setDateFormat("HH:mm");
		m_textTime.setValue(LocalDateTime.now());

		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonQuitAccount.addClickListener(
				(Button.ClickEvent event) -> m_parent.removeSubscriber(m_parent::openLogoutView,
				                                                       errorMessage -> Notification.show(errorMessage),
				                                                       m_parent.whoAmI().getEmail(),
				                                                       java.sql.Date.valueOf(LocalDate.now())));

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
				                                                 m_textTime.getValue().getHour() + ":" + m_textTime.getValue().getMinute() + ":00"));

		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getFirstName).setCaption("Vorname");
		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getLastName).setCaption("Nachname");
		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getEmail).setCaption("Email");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getAdminCheckBox).setCaption(
				"Adminrechte gewähren");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getVerifyButton).setCaption(
				"Verifizieren");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getDenyButton).setCaption(
				"Ablehnen");
		m_gridSubscriberVerification.setBodyRowHeight(42.0);
		m_gridSubscriberVerification.setSizeFull();

		m_tabLayout.addTab(m_layoutProfileBar, "Abonnenten - Dashboard");
		m_tabLayout.addTab(m_adminLayout, "Administrator - Dashboard");
		m_adminLayout.addComponents(m_gridSubscriberVerification, m_buttonShowPendingVerificationRequests);
		m_layoutHeaderBar.addComponents(m_title, m_buttonQuitAccount, m_buttonLogout);
		this.addComponent(m_layoutHeaderBar);
		m_layoutProfileBar.addComponents(m_panelProfiles, m_layoutProfileSidebar);
		m_layoutProfileSidebar.addComponents(m_textProfileName, m_textSources, m_textKeywords,
		                                     m_textTime, m_buttonProfileCreate);
	}

	public void showAdminLayout()
	{
		this.addComponent(m_tabLayout);
		m_parent.fetchPendingSubscriberVerifications(
				data -> showPendingVerificationRequests(data),
				errorMessage -> Notification.show(errorMessage));
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

	private final TabSheet m_tabLayout = new TabSheet();

	private final HorizontalLayout m_layoutProfileBar     = new HorizontalLayout();
	private final Panel            m_panelProfiles        = new Panel("Profile");
	private final VerticalLayout   m_layoutProfileSidebar = new VerticalLayout();
	private final TextField        m_textProfileName      = new TextField("Name:");
	private final TextField        m_textSources          = new TextField(
			"Quellen: (mit Komma getennt)");
	private final TextField        m_textKeywords         = new TextField(
			"Suchbegriffe: (siehe Quellen)");
	private final DateTimeField    m_textTime             = new DateTimeField("Zeitpunkt HH:MM");
	private final Button           m_buttonProfileCreate  = new Button("Profil erstellen");

	private final VerticalLayout                    m_adminLayout                           = new VerticalLayout();
	private final Grid<VerifySubscriber_GridHelper> m_gridSubscriberVerification            = new Grid<>(
			"Ausstehende Verifikationen");
	private final Button                            m_buttonShowPendingVerificationRequests = new Button(
			"Ausstehende Verifikationen aktualisieren");


	private void showPendingVerificationRequests(UserDataRepresentation[] data)
	{
		List<VerifySubscriber_GridHelper> subs = new ArrayList<>();

		for(UserDataRepresentation subscriber : data)
		{
			subs.add(new VerifySubscriber_GridHelper(m_parent,
			                                         subscriber.getFirstName(),
			                                         subscriber.getLastName(),
			                                         subscriber.getEmail(),
			                                         subscriber.isOrganisationAdministrator()));
		}

		m_gridSubscriberVerification.setItems(subs);
	}
}