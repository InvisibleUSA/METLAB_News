package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.ClippingDataRepresentation;
import me.metlabnews.Presentation.ProfileDataRepresentation;
import me.metlabnews.Presentation.SourceDataRepresentation;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.Helpers.*;
import me.metlabnews.UserInterface.MainUI;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;



/**
 * The dashboard for subscribers and client administrators
 */
public class SubscriberDashboardView extends VerticalLayout implements IView
{
	/**
	 * Constructs the dashboard for subscribers and client administrators
	 * @param parent the object owning this view
	 */
	public SubscriberDashboardView(MainUI parent)
	{
		m_parent = parent;

		setupGrids();
		m_dateTime.setValue(LocalDateTime.now());
		m_dateTime.setLocale(Locale.GERMANY);
		m_dateTime.setResolution(DateTimeResolution.SECOND);
		m_textProfileName.setWidth("350");
		m_textProfileKeywords.setWidth("350");


		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonQuitAccount.addClickListener((Button.ClickEvent event) -> m_parent.removeSubscriber(
				m_parent::openLogoutView,
				Notification::show,
				m_parent.whoAmI().getEmail(),
				java.sql.Date.valueOf(LocalDate.now())));

		m_buttonShowPendingVerificationRequests.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchPendingSubscriberVerifications(
						this::showPendingVerificationRequests,
						errorMessage -> m_parent.access(() -> Notification.show(errorMessage))));

		m_buttonShowProfiles.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchProfiles(
						this::showProfiles,
						Notification::show));

		m_buttonShowSubscribers.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchSubscribers(
						this::showSubscribers,
						Notification::show));

		m_buttonShowProfilesForClippings.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchProfiles(
						this::showProfiles,
						Notification::show));

		m_buttonShowClippings.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchClippings(
						this::showClippings,
						Notification::show));

		m_buttonShowShares.addClickListener(
				(Button.ClickEvent event) -> m_parent.fetchSubscribers(
						this::showSubscribers,
						Notification::show));

		m_buttonProfileCreate.addClickListener(
				(Button.ClickEvent event) -> createProfileAction());

		m_buttonShowProfileSources.addClickListener(
				(Button.ClickEvent event) ->
						m_parent.fetchSources(
								this::showSources,
								Notification::show));

		m_buttonShowTemplatesForSubscribers.addClickListener(
				(Button.ClickEvent event) ->
						m_parent.fetchTemplates(
								this::showTemplatesForSubscribers,
								errorMessage -> {
									Notification.show(errorMessage);
									showTemplatesForSubscribers(null);
								}));

		m_buttonShowTemplatesForAdmins.addClickListener(
				(Button.ClickEvent event) ->
						m_parent.fetchTemplates(
								this::showTemplatesForAdmins,
								Notification::show));

		m_buttonTemplateCreate.addClickListener(
				(Button.ClickEvent event) -> createTemplateAction());

		m_buttonShowTemplateSources.addClickListener(
				(Button.ClickEvent event) ->
						m_parent.fetchSources(
								this::showSources,
								Notification::show));

		m_buttonShare.addClickListener((Button.ClickEvent event) -> shareAction());

		this.addComponents(m_title, m_layoutHeaderBar);
		m_layoutHeaderBar.addComponents(m_buttonLogout);

		m_tabsSubscriber.addTab(m_displayClippings, "Pressespiegel anzeigen");
		m_displayClippings.addComponents(m_layoutClippings, m_panelClipping);
		m_layoutClippings.addComponents(m_layoutClippings1, m_layoutClippings2);
		m_layoutClippings1.addComponents(m_gridProfilesForClippings, m_buttonShowProfilesForClippings);
		m_layoutClippings2.addComponents(m_gridClippings, m_buttonShowClippings);
		m_panelClipping.setContent(m_textClipping);

		m_tabsSubscriber.addTab(m_displayProfiles, "Profile anzeigen");
		m_displayProfiles.addComponents(m_gridProfiles, m_buttonShowProfiles,
		                                m_gridShare, m_buttonShowShares, m_buttonShare);

		m_tabsSubscriber.addTab(m_displayProfileCreation, "Profil erstellen");
		m_displayProfileCreation.addComponents(m_panelProfileCreation1,
		                                       m_panelProfileCreation2,
		                                       m_panelProfileCreation3,
		                                       m_panelProfileCreation4);
		m_panelProfileCreation1.setContent(m_layoutProfileCreation1);
		m_layoutProfileCreation1.addComponents(m_textProfileName, m_textProfileKeywords, m_buttonProfileCreate);
		m_panelProfileCreation2.setContent(m_layoutProfileCreation2);
		m_layoutProfileCreation2.addComponents(m_selectProfileSources, m_buttonShowProfileSources);
		m_panelProfileCreation3.setContent(m_layoutProfileCreation3);
		m_layoutProfileCreation3.addComponents(m_dateTime, m_textTime);
		m_panelProfileCreation4.setContent(m_layoutProfileCreation4);
		m_layoutProfileCreation4.addComponents(m_listTemplates, m_buttonShowTemplatesForSubscribers);
		m_selectProfileSources.setLeftColumnCaption("Verfügbare Quellen");
		m_selectProfileSources.setRightColumnCaption("Ausgewählte Quellen");

		m_tabsAdmin.addTab(m_displayVerifications, "Ausstehende Verifikationen");
		m_displayVerifications.addComponents(m_gridSubscriberVerification, m_buttonShowPendingVerificationRequests);

		m_tabsAdmin.addTab(m_displaySubscribers, "Abonnenten");
		m_displaySubscribers.addComponents(m_gridSubscribers, m_buttonShowSubscribers);

		m_tabsAdmin.addTab(m_displayTemplates, "Vorlagen");
		m_displayTemplates.addComponents(m_gridTemplates, m_buttonShowTemplatesForAdmins);

		m_tabsAdmin.addTab(m_displayTemplateCreation, "Vorlage erstellen");
		m_displayTemplateCreation.addComponents(m_panelTemplateCreation1, m_panelTemplateCreation2);
		m_panelTemplateCreation1.setContent(m_layoutTemplateCreation1);
		m_layoutTemplateCreation1.addComponents(m_textTemplateName, m_textTemplateKeywords, m_buttonTemplateCreate);
		m_panelTemplateCreation2.setContent(m_layoutTemplateCreation2);
		m_layoutTemplateCreation2.addComponents(m_selectTemplateSources, m_buttonShowTemplateSources);

		m_tabsSettings.addTab(m_displayPWReset, "Passwort zurücksetzen");
		m_tabsSettings.addTab(m_displayQuitAccount, "Gefahrenzone");
		m_displayPWReset.addComponents(m_textCurrentPW, m_textNewPW1, m_textNewPW2, m_buttonPWReset);
		m_displayQuitAccount.addComponent(m_buttonQuitAccount);
		m_buttonPWReset.addClickListener((Button.ClickEvent event) -> m_parent.changePassword(null,
		                                                                                      errorMessage -> Notification.show(
				                                                                                      "test"),
		                                                                                      m_parent.whoAmI().getEmail(),
		                                                                                      m_textCurrentPW.getValue(),
		                                                                                      m_textNewPW1.getValue(),
		                                                                                      m_textNewPW2.getValue()));

		m_tabsSubscriber.addSelectedTabChangeListener(event -> updateGridSub());
		m_tabsAdmin.addSelectedTabChangeListener(event -> updateGridAdmin());
		m_listTemplates.addValueChangeListener(event -> applyTemplate());
		m_gridProfilesForClippings.addItemClickListener(event -> m_parent.fetchClippings(
				this::showClippings,
				errorMessage -> {
					Notification.show(errorMessage);
					m_gridClippings.setItems(
							new ClippingDataRepresentation("",
							                               "keine Pressespiegel vorhanden"));
				}));
		m_gridClippings.addItemClickListener(event -> showClipping());
	}

	@Override
	public void show()
	{
		m_parent.setContent(this);
		if(m_parent.whoAmI().isOrganisationAdministrator())
		{
			showAdminLayout();
		}
		else
		{
			showAdminLayout();
		}
	}


	private MainUI m_parent;

	private final Label            m_title             = new Label("Willkommen bei METLAB-News");
	private final Button           m_buttonQuitAccount = new Button("Konto löschen");
	private final Button           m_buttonLogout      = new Button("Abmelden");
	private final HorizontalLayout m_layoutHeaderBar   = new HorizontalLayout();

	private final TabSheet m_tabLayout      = new TabSheet();
	private final TabSheet m_tabsSubscriber = new TabSheet();
	private final TabSheet m_tabsAdmin      = new TabSheet();
	private final TabSheet m_tabsSettings   = new TabSheet();

	private final VerticalLayout              m_displayQuitAccount = new VerticalLayout();
	private final VerticalLayout                    m_displayClippings                      = new VerticalLayout();
	private final HorizontalLayout                  m_layoutClippings                       = new HorizontalLayout();
	private final VerticalLayout                    m_layoutClippings1                      = new VerticalLayout();
	private final Grid<Profile_GridHelper>          m_gridProfilesForClippings              = new Grid<>("Profile");
	private final Button                            m_buttonShowProfilesForClippings        = new Button(
			"Profile aktualisieren");
	private final VerticalLayout                    m_layoutClippings2                      = new VerticalLayout();
	private final Grid<ClippingDataRepresentation>  m_gridClippings                         = new Grid<>(
			"Pressespiegel");
	private final Button                            m_buttonShowClippings                   = new Button(
			"Pressespiegel aktualisieren");
	private final Panel                             m_panelClipping                         = new Panel(
			"Pressespiegel");
	private final Label                             m_textClipping                          = new Label("");
	private final VerticalLayout                    m_displayProfiles                       = new VerticalLayout();
	private final Grid<Profile_GridHelper>          m_gridProfiles                          = new Grid<>("Profile");
	private final Button                            m_buttonShowProfiles                    = new Button(
			"Profile aktualisieren");
	private final Grid<Subscriber_GridHelper>       m_gridShare                             = new Grid<>();
	private final Button                            m_buttonShowShares                      = new Button(
			"Abonnenten aktualisieren");
	private final Button                            m_buttonShare                           = new Button(
			"Teilen");
	private final HorizontalLayout                  m_displayProfileCreation                = new HorizontalLayout();
	private final Panel                             m_panelProfileCreation1                 = new Panel("Allgemeines");
	private final VerticalLayout                    m_layoutProfileCreation1                = new VerticalLayout();
	private final TextField                         m_textProfileName                       = new TextField("Name:");
	private final TextField                         m_textProfileKeywords                   = new TextField(
			"Suchbegriffe: (mit Komma getennt)");
	private final Button                            m_buttonProfileCreate                   = new Button(
			"Profil erstellen");
	private final Panel                             m_panelProfileCreation2                 = new Panel("Quellen");
	private final VerticalLayout                    m_layoutProfileCreation2                = new VerticalLayout();
	private final TwinColSelect<String>             m_selectProfileSources                  = new TwinColSelect<>();
	private final Button                            m_buttonShowProfileSources              = new Button(
			"Quellen aktualisieren");
	private final Panel                             m_panelProfileCreation3                 = new Panel("Zeiten");
	private final VerticalLayout                    m_layoutProfileCreation3                = new VerticalLayout();
	private final InlineDateTimeField               m_dateTime                              = new InlineDateTimeField(
			"Zeitpunkt für erste Zustellung wählen");
	private final TextField                         m_textTime                              = new TextField(
			"Zustellungsntervall DD:HH:MM:SS");
	private final Panel                             m_panelProfileCreation4                 = new Panel("Vorlagen");
	private final VerticalLayout                    m_layoutProfileCreation4                = new VerticalLayout();
	private final ListSelect<Profile_GridHelper>    m_listTemplates                         = new ListSelect<>(
			"Vorlage aus Liste wählen:");
	private final Button                            m_buttonShowTemplatesForSubscribers     = new Button(
			"Vorlagen aktualisieren");
	private final VerticalLayout                    m_displayVerifications                  = new VerticalLayout();
	private final Grid<VerifySubscriber_GridHelper> m_gridSubscriberVerification            = new Grid<>(
			"Ausstehende Verifikationen");
	private final Button                            m_buttonShowPendingVerificationRequests = new Button(
			"Ausstehende Verifikationen aktualisieren");
	private final VerticalLayout                    m_displaySubscribers                    = new VerticalLayout();
	private final Grid<Subscriber_GridHelper>       m_gridSubscribers                       = new Grid<>();
	private final Button                            m_buttonShowSubscribers                 = new Button(
			"Abonnenten aktualisieren");
	private final VerticalLayout                    m_displayTemplates                      = new VerticalLayout();
	private final Grid<Template_GridHelper>         m_gridTemplates                         = new Grid<>("Vorlagen");
	private final Button                            m_buttonShowTemplatesForAdmins          = new Button(
			"Vorlagen aktualisieren");
	private final HorizontalLayout                  m_displayTemplateCreation               = new HorizontalLayout();
	private final Panel                             m_panelTemplateCreation1                = new Panel("Allgemeins");
	private final VerticalLayout                    m_layoutTemplateCreation1               = new VerticalLayout();
	private final TextField                         m_textTemplateName                      = new TextField("Name:");
	private final TextField                         m_textTemplateKeywords                  = new TextField(
			"Suchbegriffe: (mit Komma getennt)");
	private final Button                            m_buttonTemplateCreate                  = new Button(
			"Template erstellen");
	private final Panel                             m_panelTemplateCreation2                = new Panel("Quellen");
	private final VerticalLayout                    m_layoutTemplateCreation2               = new VerticalLayout();
	private final TwinColSelect<String>             m_selectTemplateSources                 = new TwinColSelect<>();
	private final Button                            m_buttonShowTemplateSources             = new Button(
			"Quellen aktualisieren");
	private final VerticalLayout                    m_displayPWReset                        = new VerticalLayout();
	private final TextField                         m_textCurrentPW                         = new TextField(
			"Aktuelles Passwort:");
	private final TextField                         m_textNewPW1                            = new TextField(
			"Neues Passwort:");
	private final TextField                         m_textNewPW2                            = new TextField(
			"Passwort wiederholen:");
	private final Button                            m_buttonPWReset                         = new Button(
			"Passwort zurücksetzen");

	private void showAdminLayout()
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

	private void showSubscriberLayout()
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

	private void createProfileAction()
	{
		String[] intervalString = m_textTime.getValue().split(":");

		if(m_textProfileName.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Profilnamen ein!");
		}
		else if(m_textProfileKeywords.isEmpty())
		{
			Notification.show("Bitte geben Sie Suchbegriffe ein!");
		}
		else if(m_selectProfileSources.isEmpty())
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
						m_textProfileKeywords.setValue("");
						m_selectProfileSources.deselectAll();
					},
					Notification::show,
					m_textProfileName.getValue(),
					(String[])m_selectProfileSources.getSelectedItems().toArray(),
					m_textProfileKeywords.getValue().split(" "),
					/*m_dateTime.getValue(),*/
					interval);
		}
	}

	private void createTemplateAction()
	{
		if(m_textTemplateName.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Vorlagenamen ein!");
		}
		else if(m_textTemplateKeywords.isEmpty())
		{
			Notification.show("Bitte geben Sie Suchbegriffe ein!");
		}
		else if(m_selectTemplateSources.isEmpty())
		{
			Notification.show("Bitte geben Sie Quellen ein!");
		}
		else
		{
			m_parent.addTemplate(
					() -> {
						m_textTemplateName.setValue("");
						m_textTemplateKeywords.setValue("");
						m_selectTemplateSources.deselectAll();
					},
					Notification::show,
					m_textTemplateName.getValue(),
					m_textTemplateKeywords.getValue().split(" "),
					(String[])m_selectTemplateSources.getSelectedItems().toArray());
		}
	}

	private void shareAction()
	{
		Object[] profiles    = m_gridProfiles.getSelectedItems().toArray();
		Object[] subscribers = m_gridSubscribers.getSelectedItems().toArray();
		if(profiles.length != 1)
		{
			Notification.show("Bitte wählen Sie genau ein Profil zum Teilen aus!");
			return;
		}
		if(subscribers.length != 1)
		{
			Notification.show("Bitte wählen Sie genau einen Abonnenten zum Teilen aus!");
		}
		Profile_GridHelper profile = (Profile_GridHelper)profiles[0];
		@SuppressWarnings("ConstantConditions")
		Subscriber_GridHelper subscriber = (Subscriber_GridHelper)profiles[0];

		String note = "Profil " +
				profile.getName() + " an " +
				subscriber.getFirstName() + " " +
				subscriber.getLastName() + " gesendet";
		m_parent.shareProfile(
				() -> Notification.show(note),
				Notification::show,
				profile.getID(),
				subscriber.getEmail());

		m_gridProfiles.deselectAll();
		m_gridShare.deselectAll();
	}

	private void setupGrids()
	{
		m_gridProfilesForClippings.addColumn(Profile_GridHelper::getName)
				.setCaption("Profilname");
		m_gridProfilesForClippings.addComponentColumn(Profile_GridHelper::getKeywordsSelect)
				.setCaption("Suchbegriffe");
		m_gridProfilesForClippings.addComponentColumn(Profile_GridHelper::getSourcesSelect)
				.setCaption("Quellen");
		m_gridProfilesForClippings.addColumn(Profile_GridHelper::getInterval)
				.setCaption("Zustellungsintervall");
		m_gridProfilesForClippings.setBodyRowHeight(42.0);
		m_gridProfilesForClippings.setSizeUndefined();

		m_gridClippings.addColumn(ClippingDataRepresentation::getDate)
				.setCaption("Erstellt am");
		m_gridClippings.setBodyRowHeight(42.0);
		m_gridClippings.setSizeUndefined();

		m_gridProfiles.addColumn(Profile_GridHelper::getName)
				.setCaption("Profilname");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getKeywordsSelect)
				.setCaption("Suchbegriffe");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getSourcesSelect)
				.setCaption("Quellen");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getIsActiveCheckBox)
				.setCaption("Aktiv");
		m_gridProfiles.addColumn(Profile_GridHelper::getNextTime)
				.setCaption("Nächste Zustellung");
		m_gridProfiles.addColumn(Profile_GridHelper::getIntervalString)
				.setCaption("Zustellungsintervall");
		m_gridProfiles.addComponentColumn(Profile_GridHelper::getDeleteButton)
				.setCaption("Löschen");
		m_gridProfiles.setBodyRowHeight(42.0);
		m_gridProfiles.setSizeFull();

		m_gridShare.addColumn(Subscriber_GridHelper::getFirstName)
				.setCaption("Vorname");
		m_gridShare.addColumn(Subscriber_GridHelper::getLastName)
				.setCaption("Nachname");
		m_gridShare.addColumn(Subscriber_GridHelper::getEmail)
				.setCaption("Email");
		m_gridShare.setBodyRowHeight(42.0);
		m_gridShare.setSizeFull();

		m_gridTemplates.addColumn(Template_GridHelper::getName)
				.setCaption("Vorlagenname");
		m_gridTemplates.addComponentColumn(Template_GridHelper::getKeywordsSelect)
				.setCaption("Suchbegriffe");
		m_gridTemplates.addComponentColumn(Template_GridHelper::getSourcesSelect)
				.setCaption("Quellen");
		m_gridTemplates.addComponentColumn(Template_GridHelper::getDeleteButton)
				.setCaption("Löschen");
		m_gridTemplates.setBodyRowHeight(42.0);
		m_gridTemplates.setSizeFull();

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
					this::showProfiles,
					Notification::show);
		}
		if(m_tabsSubscriber.getSelectedTab().equals(m_displayProfileCreation))
		{
			m_parent.fetchSources(
					this::showSources,
					Notification::show);
			m_parent.fetchTemplates(
					this::showTemplatesForSubscribers,
					errorMessage -> {
						Notification.show(errorMessage);
						showTemplatesForSubscribers(null);
					});
		}
	}

	private void updateGridAdmin()
	{
		if(m_tabsAdmin.getSelectedTab().equals(m_displayVerifications))
		{
			m_parent.fetchPendingSubscriberVerifications(
					this::showPendingVerificationRequests,
					Notification::show);
		}
		else if(m_tabsAdmin.getSelectedTab().equals(m_displaySubscribers))
		{
			m_parent.fetchSubscribers(
					this::showSubscribers,
					Notification::show);
		}
		else if(m_tabsAdmin.getSelectedTab().equals(m_displayTemplates))
		{
			m_parent.fetchTemplates(
					this::showTemplatesForAdmins,
					Notification::show);
		}
		else if(m_tabsAdmin.getSelectedTab().equals(m_displayTemplateCreation))
		{
			m_parent.fetchTemplates(
					this::showTemplatesForAdmins,
					Notification::show);
		}
	}

	private void showProfiles(ProfileDataRepresentation[] data)
	{
		List<Profile_GridHelper> profiles = new ArrayList<>();
		for(ProfileDataRepresentation profile : data)
		{
			profiles.add(new Profile_GridHelper(m_parent,
			                                    null,
			                                    profile.getEmail(),
			                                    profile.getName(),
			                                    profile.getKeywords(),
			                                    profile.getSources(),
			                                    profile.getIsActive(),
			                                    profile.getLastGenerationTime(),
			                                    profile.getInterval()));
		}
		m_gridProfiles.setItems(profiles);
		m_gridProfiles.recalculateColumnWidths();
		m_gridProfilesForClippings.setItems(profiles);
		m_gridProfilesForClippings.recalculateColumnWidths();
	}

	private void showClippings(ClippingDataRepresentation[] data)
	{
		List<ClippingDataRepresentation> clippings = new ArrayList<>();
		Collections.addAll(clippings, data);
		m_gridClippings.setItems(clippings);
		m_gridClippings.recalculateColumnWidths();
	}

	private void showClipping()
	{
		Object[] clippings = m_gridClippings.getSelectedItems().toArray();
		if(clippings.length != 1)
		{
			Notification.show("Wählen Sie genau einen Pressespiegel aus!");
			m_textClipping.setValue("");
			return;
		}
		m_textClipping.setValue(((ClippingDataRepresentation)clippings[0]).getContent());
	}

	private void showTemplatesForSubscribers(ProfileDataRepresentation[] data)
	{
		List<Profile_GridHelper> templates = new ArrayList<>();
		if(data != null)
		{
			for(ProfileDataRepresentation template : data)
			{
				templates.add(new Profile_GridHelper(m_parent,
				                                     null,
				                                     template.getEmail(),
				                                     template.getName(),
				                                     template.getKeywords(),
				                                     template.getSources(),
				                                     template.getIsActive(),
				                                     template.getLastGenerationTime(),
				                                     template.getInterval()));
			}
		}
		if(templates.isEmpty())
		{
			templates.add(new Profile_GridHelper(m_parent,
			                                     null,
			                                     "no-reply@metlabnews.me",
			                                     "keine Vorlage vorhanden",
			                                     new ArrayList<>(),
			                                     new ArrayList<>(),
			                                     Boolean.FALSE,
			                                     LocalDateTime.now(),
			                                     Duration.ofDays(1)));
		}
		m_listTemplates.setItemCaptionGenerator(Profile_GridHelper::getName);
		m_listTemplates.setItems(templates);
	}

	private void showTemplatesForAdmins(ProfileDataRepresentation[] data)
	{
		List<Template_GridHelper> templates = new ArrayList<>();
		for(ProfileDataRepresentation template : data)
		{
			templates.add(new Template_GridHelper(m_parent,
			                                      template.getEmail(),
			                                      template.getName(),
			                                      template.getKeywords(),
			                                      template.getSources()));
		}
		m_gridTemplates.setItems(templates);
		m_gridTemplates.recalculateColumnWidths();
	}

	private void applyTemplate()
	{
		Object[] templates = m_listTemplates.getSelectedItems().toArray();
		if(templates.length != 1)
		{
			Notification.show("Bitte wählen Sie genau eine Vorlage aus");
			return;
		}
		Profile_GridHelper template = (Profile_GridHelper)templates[0];

		StringBuilder keywords = new StringBuilder();
		for(String keyword : template.getKeywords())
		{
			if(keywords.length() != 0)
			{
				keywords.append(",");
			}
			keywords.append(keyword);
		}

		m_textProfileName.setValue(template.getName());
		m_textProfileKeywords.setValue(keywords.toString());

		m_selectProfileSources.deselectAll();
		for(String source : template.getSources())
		{
			m_selectProfileSources.select(source);
		}
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
		m_gridShare.setItems(subs);
		m_gridShare.recalculateColumnWidths();
	}

	private void showSources(SourceDataRepresentation[] data)
	{
		List<String> sources = new ArrayList<>();
		for(SourceDataRepresentation source : data)
		{
			sources.add(source.getName());
		}
		m_selectProfileSources.setItems(sources);
		m_selectTemplateSources.setItems(sources);
	}
}