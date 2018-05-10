package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetOrganisation;
import me.metlabnews.UserInterface.Helpers.Organization_GridHelper;
import me.metlabnews.UserInterface.MainUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;



public class SystemAdminDashboardView extends VerticalLayout
{
	public SystemAdminDashboardView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Dashboard");

		setupGrids();
		showOrganizations();

		m_tabLayout.addSelectedTabChangeListener(event -> showOrganizations());

		m_buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		m_buttonShowOrganizations.addClickListener((Button.ClickEvent event) -> showOrganizations());

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


		this.addComponents(m_layoutHeaderBar, m_tabLayout);
		m_layoutHeaderBar.addComponents(m_title, m_buttonLogout);
		m_tabLayout.addTab(m_displayOrganizations, "Organisationen");
		m_displayOrganizations.addComponents(m_gridOrganizations, m_buttonShowOrganizations);
		m_tabLayout.addTab(m_layoutAddOrganization, "Organisation hinzufügen");
		m_layoutAddOrganization.addComponents(m_panelInitialUser, m_panelOrganization);
		m_panelInitialUser.setContent(m_layoutInitialUser);
		m_layoutInitialUser.addComponents(m_textAdminFirstName, m_textAdminLastName,
		                                  m_textAdminEmail, m_textAdminPassword);
		m_panelOrganization.setContent(m_layoutOrganization);
		m_layoutOrganization.addComponents(m_textOrganization, m_buttonAddOrganisation);
		m_tabLayout.addTab(m_layoutDisplaySources, "Quellen");
		m_tabLayout.addTab(m_layoutAddSource, "Quelle hinzufügen");
	}



	private MainUI m_parent;

	private final Label            m_title           = new Label("Willkommen bei METLAB-News - Dashboard");
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

	private final VerticalLayout m_layoutDisplaySources = new VerticalLayout();

	private final HorizontalLayout m_layoutAddSource = new HorizontalLayout();

	private void setupGrids()
	{
		m_gridOrganizations.addColumn(Organization_GridHelper::getName).setCaption("Organisation");
		m_gridOrganizations.addComponentColumn(Organization_GridHelper::getButtonAddUser).setCaption(
				"Administrator hinzufügen");
		m_gridOrganizations.addComponentColumn(Organization_GridHelper::getButtonRemoveOrganization).setCaption(
				"Organisation entfernen");
		m_gridOrganizations.setBodyRowHeight(42.0);
		m_gridOrganizations.setSizeFull();
	}

	private void showOrganizations()
	{
		QueryGetOrganisation qgo = new QueryGetOrganisation();
		if(!qgo.execute())
		{
			return;
		}
		List<String> data = Arrays.asList(qgo.organisations);

		List<Organization_GridHelper> organizations = new ArrayList<>();
		for(String organization : data)
		{
			organizations.add(new Organization_GridHelper(m_parent, organization));
		}
		m_gridOrganizations.setItems(organizations);
		m_gridOrganizations.recalculateColumnWidths();
	}
}
