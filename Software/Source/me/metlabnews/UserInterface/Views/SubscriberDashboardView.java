package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.ProfileDataRepresentation;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.Helpers.Profile_GridHelper;
import me.metlabnews.UserInterface.Helpers.VerifySubscriber_GridHelper;
import me.metlabnews.UserInterface.MainUI;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



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

		setupGrids();
		m_dateTime.setValue(LocalDateTime.now());
		m_dateTime.setLocale(Locale.GERMANY);
		m_dateTime.setResolution(DateTimeResolution.MINUTE);

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

		m_buttonShowProfiles.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchProfiles(
						data -> showProfiles(data),
						errorMessage -> Notification.show(errorMessage)));

		m_buttonProfileCreate.addClickListener(
				(Button.ClickEvent event) -> m_parent.addProfile(
						() -> {
							m_textProfileName.setValue("");
							m_textKeywords.setValue("");
							m_textSources.setValue("");
						},
						errorMessage -> Notification.show(errorMessage),
						m_textProfileName.getValue(),
						m_textSources.getValue().split(" "),
						m_textKeywords.getValue().split(" "),
						m_dateTime.getValue(),
						Duration.ofHours(m_textTime.getValue().getHour())
								.plus(Duration.ofMinutes(m_textTime.getValue().getMinute()))
				                                                ));


		this.addComponent(m_layoutHeaderBar);
		m_layoutHeaderBar.addComponents(m_title, m_buttonQuitAccount, m_buttonLogout);

		m_tabLayout.addTab(m_tabsSubscriber, "Abonnenten - Dashboard");
		m_tabsSubscriber.addTab(m_layoutProfileDisplay, "Profile anzeigen");
		m_layoutProfileDisplay.addComponents(m_gridProfiles, m_buttonShowProfiles);
		m_tabsSubscriber.addTab(m_layoutProfileCreation, "Profil erstellen");
		m_layoutProfileCreation.addComponents(m_textProfileName, m_textSources, m_textKeywords,
		                                      m_dateTime, m_textTime, m_buttonProfileCreate);
		m_tabLayout.addTab(m_adminLayout, "Administrator - Dashboard");
		m_adminLayout.addComponents(m_gridSubscriberVerification, m_buttonShowPendingVerificationRequests);
	}

	public void showAdminLayout()
	{
		this.addComponent(m_tabLayout);
		m_parent.fetchProfiles(
				data -> showProfiles(data),
				errorMessage -> Notification.show(errorMessage));
		m_parent.fetchPendingSubscriberVerifications(
				data -> showPendingVerificationRequests(data),
				errorMessage -> Notification.show(errorMessage));
	}

	public void showSubscriberLayout()
	{
		m_parent.fetchProfiles(
				data -> showProfiles(data),
				errorMessage -> Notification.show(errorMessage));
		this.addComponent(m_tabsSubscriber);
	}



	private MainUI m_parent;

	private final Label            m_title             = new Label("Willkommen bei METLAB-News - Dashboard");
	private final Button           m_buttonQuitAccount = new Button("Konto löschen");
	private final Button           m_buttonLogout      = new Button("Abmelden");
	private final HorizontalLayout m_layoutHeaderBar   = new HorizontalLayout();

	private final TabSheet m_tabLayout      = new TabSheet();
	private final TabSheet m_tabsSubscriber = new TabSheet();

	private final VerticalLayout           m_layoutProfileDisplay  = new VerticalLayout();
	private final Grid<Profile_GridHelper> m_gridProfiles          = new Grid<>("Profile");
	private final Button                   m_buttonShowProfiles    = new Button("Profile aktualisieren");
	private final VerticalLayout           m_layoutProfileCreation = new VerticalLayout();
	private final TextField                m_textProfileName       = new TextField("Name:");
	private final TextField                m_textSources           = new TextField(
			"Quellen: (mit Komma getennt)");
	private final TextField                m_textKeywords          = new TextField(
			"Suchbegriffe: (siehe Quellen)");
	private final InlineDateTimeField      m_dateTime              = new InlineDateTimeField("partially implemented");
	private final DateTimeField            m_textTime              = new DateTimeField("Intervall HH:MM");
	private final Button                   m_buttonProfileCreate   = new Button("Profil erstellen");

	private final VerticalLayout                    m_adminLayout                           = new VerticalLayout();
	private final Grid<VerifySubscriber_GridHelper> m_gridSubscriberVerification            = new Grid<>(
			"Ausstehende Verifikationen");
	private final Button                            m_buttonShowPendingVerificationRequests = new Button(
			"Ausstehende Verifikationen aktualisieren");

	private void setupGrids()
	{
		m_gridProfiles.addColumn(Profile_GridHelper::getName).setCaption("Profilname");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getKeywords).setCaption("Suchbegriffe");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getSources).setCaption("Quellen");
		m_gridProfiles.addColumn(Profile_GridHelper::getNextTime).setCaption("Nächste Zustellung");
		m_gridProfiles.addColumn(Profile_GridHelper::getInterval).setCaption("Zustellungsintervall");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getDeleteButton).setCaption("Löschen");
		m_gridProfiles.setBodyRowHeight(42.0);
		m_gridProfiles.setSizeFull();

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
	}

	private void showProfiles(ProfileDataRepresentation[] data)
	{
		List<Profile_GridHelper> profiles = new ArrayList<>();

		for(ProfileDataRepresentation profile : data)
		{
			profiles.add(new Profile_GridHelper(m_parent,
			                                    profile.getEmail(),
			                                    profile.getName(),
			                                    profile.getKeywords(),
			                                    profile.getSources(),
			                                    profile.getLastGenerationTime(),
			                                    profile.getInterval()));
		}

		m_gridProfiles.setItems(profiles);
	}

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