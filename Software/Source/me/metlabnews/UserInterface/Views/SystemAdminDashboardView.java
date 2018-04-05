package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SystemAdminDashboardView extends VerticalLayout
{
	public SystemAdminDashboardView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Dashboard");

		final FormLayout outerContent = new FormLayout();
		final FormLayout innerContent = new FormLayout();
		innerContent.addComponent(textFieldAdminFirstName);
		innerContent.addComponent(textFieldAdminLastName);
		innerContent.addComponent(textFieldAdminEmail);
		innerContent.addComponent(textFieldAdminPassword);
		innerContent.setSizeUndefined();
		innerContent.setMargin(true);
		panelInitialUser.setContent(innerContent);

		outerContent.addComponent(textFieldOrganisationName);
		outerContent.addComponent(panelInitialUser);
		outerContent.addComponent(buttonAddOrganisation);
		outerContent.setSizeUndefined();
		outerContent.setMargin(true);
		panelNewOrganisation.setContent(outerContent);


		buttonLogout.addClickListener((Button.ClickEvent event)
				                              -> m_parent.userLogoutAction());
		this.addComponents(title, panelNewOrganisation, buttonLogout);
	}



	private MainUI m_parent;

	private final Label  title        = new Label("Willkommen bei METLAB-News - Dashboard!");
	private final Button buttonLogout = new Button("Abmelden");
	private final Panel panelNewOrganisation = new Panel("Neue Organisation hinzufügen");
	private final TextField textFieldOrganisationName = new TextField("Name der Organisation:");
	private final Button buttonAddOrganisation = new Button("Organisation hinzufügen");
	private final Panel panelInitialUser = new Panel("Administrator hinzufügen");
	private final TextField     textFieldAdminFirstName = new TextField("Vorname:");
	private final TextField     textFieldAdminLastName  = new TextField("Nachname:");
	private final TextField     textFieldAdminEmail     = new TextField("E-Mail:");
	private final PasswordField textFieldAdminPassword  = new PasswordField("Passwort:");
}
