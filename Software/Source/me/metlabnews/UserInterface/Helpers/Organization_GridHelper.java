package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class Organization_GridHelper
{
	public Organization_GridHelper(MainUI parent, String name)
	{
		m_parent = parent;
		m_name = name;

		m_textAdminFirstName.setPlaceholder("Vorname");
		m_textAdminLastName.setPlaceholder("Nachname");
		m_textAdminEmail.setPlaceholder("E-Mail");
		m_textAdminPassword.setPlaceholder("Passwort");

		m_layoutAddUser.addComponents(m_textAdminFirstName, m_textAdminLastName,
		                              m_textAdminEmail, m_textAdminPassword, m_buttonAddUser);

		m_buttonAddUser.addClickListener(
				event -> m_parent.registerVerifiedSubscriber(
						() -> {
							m_parent.access(() -> Notification.show("Administrator hinzugefügt"));
							m_textAdminFirstName.setValue("");
							m_textAdminLastName.setValue("");
							m_textAdminEmail.setValue("");
							m_textAdminPassword.setValue("");
						},
						m_textAdminFirstName.getValue(), m_textAdminLastName.getValue(), m_name,
						m_textAdminEmail.getValue(), m_textAdminPassword.getValue(), true));

		m_buttonRemoveOrganization.addClickListener(event -> m_parent.removeOrganisation(
				() -> {
					m_buttonRemoveOrganization.setEnabled(false);
					m_parent.access(() -> Notification.show("Organisation entfernt"));
				},
				errorMessage -> m_parent.access(() -> Notification.show("Organisation konnte nicht entfernt werden!\n" + errorMessage)),
				m_name));
	}

	public String getName()
	{
		return m_name;
	}

	public HorizontalLayout getLayoutAddUser()
	{
		return m_layoutAddUser;
	}

	public Button getButtonRemoveOrganization()
	{
		return m_buttonRemoveOrganization;
	}

	private MainUI m_parent;
	private String m_name;
	private final HorizontalLayout m_layoutAddUser            = new HorizontalLayout();
	private final TextField        m_textAdminFirstName       = new TextField();
	private final TextField        m_textAdminLastName        = new TextField();
	private final TextField        m_textAdminEmail           = new TextField();
	private final PasswordField    m_textAdminPassword        = new PasswordField();
	private final Button           m_buttonAddUser            = new Button("Administrator hizufügen");
	private final Button           m_buttonRemoveOrganization = new Button("Organisation entfernen");
}
