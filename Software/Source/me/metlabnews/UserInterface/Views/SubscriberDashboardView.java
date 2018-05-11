package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.ProfileDataRepresentation;
import me.metlabnews.Presentation.SourceDataRepresentation;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.Helpers.Profile_GridHelper;
import me.metlabnews.UserInterface.Helpers.Subscriber_GridHelper;
import me.metlabnews.UserInterface.Helpers.VerifySubscriber_GridHelper;
import me.metlabnews.UserInterface.MainUI;

import javax.swing.text.TabSet;
import javax.xml.soap.Text;
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
		m_dateTime.setResolution(DateTimeResolution.SECOND);

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


		m_buttonShowSubscribers.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchSubscribers(
						data -> showSubscribers(data),
						errorMessage -> Notification.show(errorMessage)));

		m_buttonShowProfiles.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchProfiles(
						data -> showProfiles(data),
						errorMessage -> Notification.show(errorMessage)));

		m_buttonProfileCreate.addClickListener(
				(Button.ClickEvent event) -> createProfileAction());


		this.addComponent(m_layoutHeaderBar);
		m_layoutHeaderBar.addComponents(m_title, m_buttonQuitAccount, m_buttonLogout);

		m_tabsSubscriber.addTab(m_displayProfiles, "Profile anzeigen");
		m_displayProfiles.addComponents(m_gridProfiles, m_buttonShowProfiles);
		m_tabsSubscriber.addTab(m_displayProfileCreation, "Profil erstellen");
		m_displayProfileCreation.addComponents(m_panelProfileCreation1,
		                                       m_panelProfileCreation2,
		                                       m_panelProfileCreation3);
		m_panelProfileCreation1.setContent(m_layoutProfileCreation1);
		m_layoutProfileCreation1.addComponents(m_textProfileName, m_textKeywords, m_buttonProfileCreate);
		m_panelProfileCreation2.setContent(m_layoutProfileCreation2);
		m_layoutProfileCreation2.addComponents(m_selectSources);
		m_panelProfileCreation3.setContent(m_layoutProfileCreation3);
		m_layoutProfileCreation3.addComponents(m_dateTime, m_textTime);
		m_selectSources.setLeftColumnCaption("Verfügbare Quellen");
		m_selectSources.setRightColumnCaption("Ausgewählte Quellen");

		m_tabsAdmin.addTab(m_displayVerifications, "Ausstehende Verifikationen");
		m_displayVerifications.addComponents(m_gridSubscriberVerification, m_buttonShowPendingVerificationRequests);

		m_tabsAdmin.addTab(m_displaySubscribers, "Abonnenten");
		m_displaySubscribers.addComponents(m_gridSubscribers, m_buttonShowSubscribers);

		m_tabsSettings.addTab(m_displayPWReset, "Passwort zurücksetzen");
		m_displayPWReset.addComponents(m_textCurrentPW, m_textNewPW1, m_textNewPW2, m_buttonPWReset);
		m_buttonPWReset.addClickListener((Button.ClickEvent event) -> m_parent.changePassword(null,
		                                                                                      errorMessage -> Notification.show(
				                                                                                      "test"),
		                                                                                      m_parent.whoAmI().getEmail(),
		                                                                                      m_textCurrentPW.getValue(),
		                                                                                      m_textNewPW1.getValue(),
		                                                                                      m_textNewPW2.getValue()));

		m_tabsSubscriber.addSelectedTabChangeListener(event -> updateGridSub());
		m_tabsAdmin.addSelectedTabChangeListener(event -> updateGridAdmin());
	}

	public void showAdminLayout()
	{
		this.addComponent(m_tabLayout);
		m_tabLayout.removeAllComponents();
		m_tabLayout.addTab(m_tabsSubscriber, "Abonnenten - Dashboard");
		m_tabLayout.addTab(m_tabsAdmin, "Administrator - Dashboard");
		m_tabLayout.addTab(m_tabsSettings, "Einstellungen");
		/*
		m_parent.fetchProfiles(
				data -> showProfiles(data),
				errorMessage -> Notification.show(errorMessage));
		*/
	}

	public void showSubscriberLayout()
	{
		this.addComponent(m_tabLayout);
		m_tabLayout.removeAllComponents();
		m_tabLayout.addTab(m_tabsSubscriber, "Abonnenten - Dashboard");
		m_tabLayout.addTab(m_tabsSettings, "Einstellungen");
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
	private final TabSheet m_tabsAdmin      = new TabSheet();
	private final TabSheet m_tabsSettings   = new TabSheet();

	private final VerticalLayout              m_displayProfiles                       = new VerticalLayout();
	private final Grid<Profile_GridHelper>    m_gridProfiles                          = new Grid<>("Profile");
	private final Button                      m_buttonShowProfiles                    = new Button(
			"Profile aktualisieren");
	private final HorizontalLayout            m_displayProfileCreation                = new HorizontalLayout();
	private final Panel                       m_panelProfileCreation1                 = new Panel("Allgemeines");
	private final VerticalLayout                    m_layoutProfileCreation1                = new VerticalLayout();
	private final TextField                         m_textProfileName                       = new TextField("Name:");
	private final TextField                         m_textKeywords                          = new TextField(
			"Suchbegriffe: (mit Komma getennt)");
	private final Button                            m_buttonProfileCreate                   = new Button(
			"Profil erstellen");
	private final Panel                             m_panelProfileCreation2                 = new Panel("Quellen");
	private final VerticalLayout                    m_layoutProfileCreation2                = new VerticalLayout();
	private final TwinColSelect<String>             m_selectSources                         = new TwinColSelect<>();
	private final Panel                             m_panelProfileCreation3                 = new Panel("Zeiten");
	private final VerticalLayout                    m_layoutProfileCreation3                = new VerticalLayout();
	private final InlineDateTimeField               m_dateTime                              = new InlineDateTimeField(
			"Zeitpunkt für erste Zustellung wählen");
	private final TextField                         m_textTime                              = new TextField(
			"Zustellungsntervall DD:HH:MM:SS");
	private final VerticalLayout                    m_displayVerifications                  = new VerticalLayout();
	private final Grid<VerifySubscriber_GridHelper> m_gridSubscriberVerification            = new Grid<>(
			"Ausstehende Verifikationen");
	private final Button                      m_buttonShowPendingVerificationRequests = new Button(
			"Ausstehende Verifikationen aktualisieren");
	private final VerticalLayout              m_displaySubscribers                    = new VerticalLayout();
	private final Grid<Subscriber_GridHelper> m_gridSubscribers                       = new Grid<>();
	private final Button                      m_buttonShowSubscribers                 = new Button(
			"Abonnenten aktualisieren");
	private final VerticalLayout              m_displayPWReset                        = new VerticalLayout();
	private final TextField                   m_textCurrentPW                         = new TextField(
			"Aktuelles Passwort:");
	private final TextField                   m_textNewPW1                            = new TextField(
			"Neues Passwort:");
	private final TextField                   m_textNewPW2                            = new TextField(
			"Passwort wiederholen:");
	private final Button                      m_buttonPWReset                         = new Button(
			"Passwort zurücksetzen");

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
		else if(m_selectSources.isEmpty())
		{
			Notification.show("Bitte geben Sie Quellen ein!");
		}
		else if(!m_textTime.getValue().matches("[0-9]+:[0-9]+:[0-9]:[0-9]+"))
		{
			Notification.show("Bitte geben Sie ein Intervall im Format DD:HH:MM:SS ein!");
		}
		else
		{
			Duration days     = Duration.ofDays(Long.parseLong(intervalString[0]));
			Duration hours    = Duration.ofHours(Long.parseLong(intervalString[1]));
			Duration minutes  = Duration.ofMinutes(Long.parseLong(intervalString[2]));
			Duration seconds  = Duration.ofSeconds(Long.parseLong(intervalString[3]));
			Duration interval = days.plus(hours).plus(minutes).plus(seconds);
			m_parent.addProfile(
					() -> {
						m_textProfileName.setValue("");
						m_textKeywords.setValue("");
						m_selectSources.deselectAll();
					},
					errorMessage -> Notification.show(errorMessage),
					m_textProfileName.getValue(),
					(String[])m_selectSources.getSelectedItems().toArray(),
					m_textKeywords.getValue().split(" "),
					m_dateTime.getValue(),
					interval);
		}
	}

	private void setupGrids()
	{
		m_gridProfiles.addColumn(Profile_GridHelper::getName)
				.setCaption("Profilname");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getKeywords)
				.setCaption("Suchbegriffe");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getSources)
				.setCaption("Quellen");
		m_gridProfiles.addColumn(Profile_GridHelper::getNextTime)
				.setCaption("Nächste Zustellung");
		m_gridProfiles.addColumn(Profile_GridHelper::getInterval)
				.setCaption("Zustellungsintervall");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getDeleteButton)
				.setCaption("Löschen");
		m_gridProfiles.setBodyRowHeight(42.0);
		m_gridProfiles.setSizeFull();

		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getFirstName)
				.setCaption("Vorname");
		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getLastName)
				.setCaption("Nachname");
		m_gridSubscriberVerification.addColumn(VerifySubscriber_GridHelper::getEmail)
				.setCaption("Email");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getAdminCheckBox)
				.setCaption("Adminrechte gewähren");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getVerifyButton)
				.setCaption("Verifizieren");
		m_gridSubscriberVerification.addComponentColumn(VerifySubscriber_GridHelper::getDenyButton)
				.setCaption("Ablehnen");
		m_gridSubscriberVerification.setBodyRowHeight(42.0);
		m_gridSubscriberVerification.setSizeFull();

		m_gridSubscribers.addColumn(Subscriber_GridHelper::getFirstName)
				.setCaption("Vorname");
		m_gridSubscribers.addColumn(Subscriber_GridHelper::getLastName)
				.setCaption("Nachname");
		m_gridSubscribers.addColumn(Subscriber_GridHelper::getEmail)
				.setCaption("Email");
		m_gridSubscribers.addComponentColumn(Subscriber_GridHelper::getAdminCheckBox)
				.setCaption("Adminrechte");
		m_gridSubscribers.addComponentColumn(Subscriber_GridHelper::getRemoveButton)
				.setCaption("Entfernen");
		m_gridSubscribers.setBodyRowHeight(42.0);
		m_gridSubscribers.setSizeFull();
	}

	private void updateGridSub()
	{
		if(m_tabsSubscriber.getSelectedTab().equals(m_displayProfiles))
		{
			m_parent.fetchProfiles(
					data -> showProfiles(data),
					errorMessage -> Notification.show(errorMessage));
		}
		if(m_tabsSubscriber.getSelectedTab().equals(m_displayProfileCreation))
		{
			m_parent.fetchSources(
					data -> showSources(data),
					errorMessage -> Notification.show(errorMessage));
		}
	}

	private void updateGridAdmin()
	{
		if(m_tabsAdmin.getSelectedTab().equals(m_displayVerifications))
		{
			m_parent.fetchPendingSubscriberVerifications(
					data -> showPendingVerificationRequests(data),
					errorMessage -> Notification.show(errorMessage));
		}
		else if(m_tabsAdmin.getSelectedTab().equals(m_displaySubscribers))
		{
			m_parent.fetchSubscribers(
					data -> showSubscribers(data),
					errorMessage -> Notification.show(errorMessage));
		}
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

	private void showSubscribers(UserDataRepresentation[] data)
	{
		List<Subscriber_GridHelper> subs = new ArrayList<>();
		for(UserDataRepresentation subscriber : data)
		{
			subs.add(new Subscriber_GridHelper(m_parent,
			                                   subscriber.getFirstName(),
			                                   subscriber.getLastName(),
			                                   subscriber.getEmail(),
			                                   subscriber.isOrganisationAdministrator()));
		}
		m_gridSubscribers.setItems(subs);
		m_gridSubscribers.recalculateColumnWidths();
	}

	private void showSources(SourceDataRepresentation[] data)
	{
		List<String> sources = new ArrayList<>();
		for(SourceDataRepresentation source : data)
		{
			sources.add(source.getName());
		}
		m_selectSources.setItems(sources);
	}
}