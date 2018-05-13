package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetOrganisation;
import me.metlabnews.Presentation.SourceDataRepresentation;
import me.metlabnews.UserInterface.Helpers.Organization_GridHelper;
import me.metlabnews.UserInterface.Helpers.Source_GridHelper;
import me.metlabnews.UserInterface.MainUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class SystemAdminDashboardView extends VerticalLayout implements IView
{
	public SystemAdminDashboardView(MainUI parent)
	{
		m_parent = parent;

		setupGrids();

		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonShowOrganizations.addClickListener(
				(Button.ClickEvent event) -> m_parent.getAllOrganisations(
						this::showOrganizations,
						Notification::show));

		m_buttonShowSources.addClickListener(
				(Button.ClickEvent event) -> updateGrid());

		m_buttonAddOrganisation.addClickListener(event -> m_parent.addOrganisation(
				() -> m_parent.access(
						() -> {
							Notification.show("Organisation hinzugefügt");
							m_textOrganization.clear();
							m_textAdminFirstName.clear();
							m_textAdminLastName.clear();
							m_textAdminEmail.clear();
							m_textAdminPassword.clear();
						}),
				errorMessage -> m_parent.access(
						() -> Notification.show("Organisation konnte nicht hinzugefügt werden\n" + errorMessage)),
				m_textOrganization.getValue(),
				m_textAdminFirstName.getValue(),
				m_textAdminLastName.getValue(),
				m_textAdminEmail.getValue(),
				m_textAdminPassword.getValue()));

		m_buttonAddSource.addClickListener(event -> m_parent.addSource(
				() -> m_parent.access(
						() -> {
							Notification.show("Quelle hinzugefügt");
							m_textSourceName.clear();
							m_textSourceLink.clear();
							m_textSourceRssLink.clear();
						}),
				errorMessage -> m_parent.access(
						() -> Notification.show("Quelle konnte nicht hinzugefügt werden\n" + errorMessage)),
				m_textSourceName.getValue(),
				m_textSourceLink.getValue(),
				m_textSourceRssLink.getValue()));


		this.addComponents(m_title, m_layoutHeaderBar, m_tabLayout);
		m_layoutHeaderBar.addComponents(m_buttonLogout);
		m_tabLayout.addTab(m_displayOrganizations, "Organisationen");
		m_displayOrganizations.addComponents(m_gridOrganizations, m_buttonShowOrganizations);
		m_tabLayout.addTab(m_layoutAddOrganization, "Organisation hinzufügen");
		m_layoutAddOrganization.addComponents(m_panelInitialUser, m_panelOrganization);
		m_panelInitialUser.setContent(m_layoutInitialUser);
		m_layoutInitialUser.addComponents(m_textAdminFirstName, m_textAdminLastName,
		                                  m_textAdminEmail, m_textAdminPassword);
		m_panelOrganization.setContent(m_layoutOrganization);
		m_layoutOrganization.addComponents(m_textOrganization, m_buttonAddOrganisation);
		m_tabLayout.addTab(m_displaySources, "Quellen");
		m_displaySources.addComponents(m_gridSources, m_buttonShowSources);
		m_tabLayout.addTab(m_layoutAddSource, "Quelle hinzufügen");
		m_layoutAddSource.addComponents(m_panelSource);
		m_panelSource.setContent(m_layoutSource);
		m_layoutSource.addComponents(m_textSourceName, m_textSourceLink, m_textSourceRssLink, m_buttonAddSource);


		m_tabLayout.addSelectedTabChangeListener(event -> updateGrid());
	}

	@Override
	public void show()
	{
		m_parent.setContent(this);
	}

	private MainUI m_parent;

	private final Label            m_title           = new Label("Willkommen bei METLAB-News");
	private final Button           m_buttonLogout    = new Button("Abmelden");
	private final HorizontalLayout m_layoutHeaderBar = new HorizontalLayout();

	private final TabSheet m_tabLayout = new TabSheet();

	private final VerticalLayout                m_displayOrganizations    = new VerticalLayout();
	private final Grid<Organization_GridHelper> m_gridOrganizations       = new Grid<>();
	private final Button                        m_buttonShowOrganizations = new Button("Organisationen aktualisieren");

	private final HorizontalLayout m_layoutAddOrganization = new HorizontalLayout();
	private final Panel            m_panelInitialUser      = new Panel("Administrator");
	private final VerticalLayout   m_layoutInitialUser     = new VerticalLayout();
	private final TextField        m_textAdminFirstName    = new TextField("Vorname:");
	private final TextField        m_textAdminLastName     = new TextField("Nachname:");
	private final TextField        m_textAdminEmail        = new TextField("E-Mail:");
	private final PasswordField    m_textAdminPassword     = new PasswordField("Passwort:");
	private final Panel            m_panelOrganization     = new Panel("Organisation");
	private final VerticalLayout   m_layoutOrganization    = new VerticalLayout();
	private final TextField        m_textOrganization      = new TextField("Name der Organisation:");
	private final Button           m_buttonAddOrganisation = new Button("Organisation hinzufügen");

	private final VerticalLayout          m_displaySources    = new VerticalLayout();
	private final Grid<Source_GridHelper> m_gridSources       = new Grid<>();
	private final Button                  m_buttonShowSources = new Button("Quellen aktualisieren");

	private final HorizontalLayout m_layoutAddSource   = new HorizontalLayout();
	private final Panel            m_panelSource       = new Panel("Quelle");
	private final VerticalLayout   m_layoutSource      = new VerticalLayout();
	private final TextField        m_textSourceName    = new TextField("Bezeichnung:");
	private final TextField        m_textSourceLink    = new TextField("Link:");
	private final TextField        m_textSourceRssLink = new TextField("RSS-Feed:");
	private final Button           m_buttonAddSource   = new Button("Quelle hinzufügen");

	private void setupGrids()
	{
		m_gridOrganizations.addColumn(Organization_GridHelper::getName)
				.setCaption("Organisation");
		m_gridOrganizations.addComponentColumn(Organization_GridHelper::getLayoutAddUser)
				.setCaption("Administrator hinzufügen");
		m_gridOrganizations.addComponentColumn(Organization_GridHelper::getButtonRemoveOrganization)
				.setCaption("Organisation entfernen");
		m_gridOrganizations.setBodyRowHeight(42.0);
		m_gridOrganizations.setSizeFull();

		m_gridSources.addColumn(Source_GridHelper::getName)
				.setCaption("Bezeichnung");
		m_gridSources.addColumn(Source_GridHelper::getLink)
				.setCaption("Link");
		m_gridSources.addColumn(Source_GridHelper::getRssLink)
				.setCaption("RSS-Link");
		m_gridSources.addColumn(Source_GridHelper::getRemoveButton)
				.setCaption("Quelle entfernen");
		m_gridSources.setBodyRowHeight(42.0);
		m_gridSources.setSizeFull();
	}

	private void updateGrid()
	{
		if(m_tabLayout.getSelectedTab().equals(m_displayOrganizations))
		{
			m_parent.getAllOrganisations(this::showOrganizations,
			                             Notification::show);
		}
		else if(m_tabLayout.getSelectedTab().equals(m_displaySources))
		{
			m_parent.fetchSources(this::showSources, Notification::show);
		}
	}

	private void showOrganizations(String[] data)
	{
		List<Organization_GridHelper> organizations = new ArrayList<>();
		for(String organization : data)
		{
			organizations.add(new Organization_GridHelper(m_parent, organization));
		}
		m_gridOrganizations.setItems(organizations);
		m_gridOrganizations.recalculateColumnWidths();
	}

	private void showSources(SourceDataRepresentation[] data)
	{
		List<Source_GridHelper> sources = new ArrayList<>();
		for(SourceDataRepresentation source : data)
		{
			sources.add(new Source_GridHelper(m_parent,
			                                  source.getName(),
			                                  source.getLink(),
			                                  source.getRssLink()));
		}
		m_gridSources.setItems(sources);
		m_gridSources.recalculateColumnWidths();
	}
}
