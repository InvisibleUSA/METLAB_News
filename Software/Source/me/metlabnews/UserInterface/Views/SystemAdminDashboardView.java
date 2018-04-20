package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SystemAdminDashboardView
		extends VerticalLayout implements IView
{
	public SystemAdminDashboardView(MainUI parent)
	{
		m_parent = parent;

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


		buttonAddOrganisation.addClickListener(
				event -> m_parent.addOrganisation(() -> m_parent.access(() ->
				                                                        {
					                                                        Notification popup = new Notification(
							                                                        "Organisation hinzugefügt",
							                                                        Notification.Type.WARNING_MESSAGE);
					                                                        popup.setDelayMsec(3000);
					                                                        popup.show(Page.getCurrent());
					                                                        textFieldOrganisationName.clear();
					                                                        textFieldAdminFirstName.clear();
					                                                        textFieldAdminLastName.clear();
					                                                        textFieldAdminEmail.clear();
					                                                        textFieldAdminPassword.clear();
				                                                        }),
				                                  errorMessage -> m_parent.access(() ->
				                                                                  {
					                                                                  Notification popup = new Notification(
							                                                                  "Organisation konnte nicht hinzugefügt werden\n" + errorMessage,
							                                                                  Notification.Type.WARNING_MESSAGE);
					                                                                  popup.setDelayMsec(3000);
					                                                                  popup.show(Page.getCurrent());
				                                                                  }),

				                                  textFieldOrganisationName.getValue(),
				                                  textFieldAdminFirstName.getValue(),
				                                  textFieldAdminLastName.getValue(),
				                                  textFieldAdminEmail.getValue(),
				                                  textFieldAdminPassword.getValue()));


		buttonLogout.addClickListener((Button.ClickEvent event)
				                              -> m_parent.logout());
		this.addComponents(title, panelNewOrganisation, buttonLogout);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Dashboard");
	}



	private MainUI m_parent;

	private final Label         title                     = new Label("Willkommen bei METLAB-News - Dashboard!");
	private final Button        buttonLogout              = new Button("Abmelden");
	private final Panel         panelNewOrganisation      = new Panel("Neue Organisation hinzufügen");
	private final TextField     textFieldOrganisationName = new TextField("Name der Organisation:");
	private final Button        buttonAddOrganisation     = new Button("Organisation hinzufügen");
	private final Panel         panelInitialUser          = new Panel("Administrator hinzufügen");
	private final TextField     textFieldAdminFirstName   = new TextField("Vorname:");
	private final TextField     textFieldAdminLastName    = new TextField("Nachname:");
	private final TextField     textFieldAdminEmail       = new TextField("E-Mail:");
	private final PasswordField textFieldAdminPassword    = new PasswordField("Passwort:");
}
