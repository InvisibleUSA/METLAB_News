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

		setupGrids();
		m_dateTime.setValue(LocalDateTime.now());
		m_dateTime.setLocale(Locale.GERMANY);
		m_dateTime.setResolution(DateTimeResolution.MINUTE);

		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonQuitAccount.addClickListener((Button.ClickEvent event) -> m_parent.removeSubscriber(
				m_parent::openLogoutView,
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
				(Button.ClickEvent event) -> createProfileAction());


		this.addComponent(m_layoutHeaderBar);
		m_layoutHeaderBar.addComponents(m_title, m_buttonQuitAccount, m_buttonLogout);

		m_tabsSubscriber.addTab(m_layoutProfileDisplay, "Profile anzeigen");
		m_layoutProfileDisplay.addComponents(m_gridProfiles, m_buttonShowProfiles);
		m_tabsSubscriber.addTab(m_layoutProfileCreationColumns, "Profil erstellen");
		m_layoutProfileCreationColumns.addComponents(m_layoutProfileCreation1, m_layoutProfileCreation2);
		m_layoutProfileCreation1.addComponents(m_textProfileName, m_textSources, m_textKeywords, m_buttonProfileCreate);
		m_layoutProfileCreation2.addComponents(m_dateTime, m_textTime);
		m_adminLayout.addComponents(m_gridSubscriberVerification, m_buttonShowPendingVerificationRequests);
	}

	public void showAdminLayout()
	{
		this.addComponent(m_tabLayout);
		m_tabLayout.removeAllComponents();
		m_tabLayout.addTab(m_tabsSubscriber, "Abonnenten - Dashboard");
		m_tabLayout.addTab(m_adminLayout, "Administrator - Dashboard");
		/*
		m_parent.fetchProfiles(
				data -> showProfiles(data),
				errorMessage -> Notification.show(errorMessage));
		*/
		m_parent.fetchPendingSubscriberVerifications(
				data -> showPendingVerificationRequests(data),
				errorMessage -> Notification.show(errorMessage));
	}

	public void showSubscriberLayout()
	{
		this.addComponent(m_tabLayout);
		m_tabLayout.removeAllComponents();
		m_tabLayout.addTab(m_tabsSubscriber, "Abonnenten - Dashboard");
		/*
		m_parent.fetchProfiles(
				data -> showProfiles(data),
				errorMessage -> Notification.show(errorMessage));
		*/
	}



	private MainUI m_parent;

	private final Label            m_title             = new Label("Willkommen bei METLAB-News - Dashboard");
	private final Button           m_buttonQuitAccount = new Button("Konto löschen");
	private final Button           m_buttonLogout      = new Button("Abmelden");
	private final HorizontalLayout m_layoutHeaderBar   = new HorizontalLayout();

	private final TabSheet m_tabLayout      = new TabSheet();
	private final TabSheet m_tabsSubscriber = new TabSheet();

	private final VerticalLayout           m_layoutProfileDisplay         = new VerticalLayout();
	private final Grid<Profile_GridHelper> m_gridProfiles                 = new Grid<>("Profile");
	private final Button                   m_buttonShowProfiles           = new Button("Profile aktualisieren");
	private final HorizontalLayout         m_layoutProfileCreationColumns = new HorizontalLayout();
	private final VerticalLayout           m_layoutProfileCreation1       = new VerticalLayout();
	private final VerticalLayout           m_layoutProfileCreation2       = new VerticalLayout();
	private final TextField                m_textProfileName              = new TextField("Name:");
	private final TextField                m_textKeywords                 = new TextField(
			"Suchbegriffe: (mit Komma getennt)");
	private final TextField                m_textSources                  = new TextField(
			"Quellen: (mit Komma getennt)");
	private final InlineDateTimeField      m_dateTime                     = new InlineDateTimeField(
			"Zeitpunkt für erste Zustellung wählen");
	private final TextField                m_textTime                     = new TextField(
			"Zustellungsntervall DD:HH:MM");
	private final Button                   m_buttonProfileCreate          = new Button("Profil erstellen");

	private final VerticalLayout                    m_adminLayout                           = new VerticalLayout();
	private final Grid<VerifySubscriber_GridHelper> m_gridSubscriberVerification            = new Grid<>(
			"Ausstehende Verifikationen");
	private final Button                            m_buttonShowPendingVerificationRequests = new Button(
			"Ausstehende Verifikationen aktualisieren");

	private void createProfileAction()
	{
		String[] intervalString = m_textTime.getValue().split(":");

		if(m_textProfileName.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Profilnamen ein!");
		}
		else if(m_textKeywords.isEmpty())
		{
			Notification.show("Bitte geben Sie Suchbegriffe ein!");
		}
		else if(m_textSources.isEmpty())
		{
			Notification.show("Bitte geben Sie Quellen ein!");
		}
		else if(!m_textTime.getValue().matches("[0-9]+:[0-9]+:[0-9]+"))
		{
			Notification.show("Bitte geben Sie ein Intervall im Format DD:HH:MM ein!");
		}
		else
		{
			Duration days     = Duration.ofDays(Long.parseLong(intervalString[0]));
			Duration hours    = Duration.ofHours(Long.parseLong(intervalString[1]));
			Duration minutes  = Duration.ofMinutes(Long.parseLong(intervalString[2]));
			Duration interval = days.plus(hours).plus(minutes);
			m_parent.addProfile(
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
					interval);
		}
	}

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
		m_gridProfiles.recalculateColumnWidths();
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
		m_gridSubscriberVerification.recalculateColumnWidths();
	}
}