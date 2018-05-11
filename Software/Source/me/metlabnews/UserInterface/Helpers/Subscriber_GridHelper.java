package me.metlabnews.UserInterface.Helpers;

import me.metlabnews.UserInterface.MainUI;
import com.vaadin.ui.*;

import java.time.LocalDate;



public class Subscriber_GridHelper
{
	public Subscriber_GridHelper(MainUI parent, String firstName, String lastName, String email, Boolean isAdmin)
	{
		m_parent = parent;
		m_firstName = firstName;
		m_lastName = lastName;
		m_email = email;
		m_grantAdminStatus.setValue(isAdmin);
		m_grantAdminStatus.setEnabled(false);

		m_buttonRemove.addClickListener((Button.ClickEvent event) -> m_parent.removeSubscriber(
				() -> {
						m_buttonRemove.setEnabled(false);
						Notification.show("Abonnent wurde entfernt");
				},
				errorMessage -> Notification.show(errorMessage),
				m_email,
				java.sql.Date.valueOf(LocalDate.now())));
	}

	public String getFirstName()
	{
		return m_firstName;
	}

	public String getLastName()
	{
		return m_lastName;
	}

	public String getEmail()
	{
		return m_email;
	}

	public CheckBox getAdminCheckBox()
	{
		return m_grantAdminStatus;
	}

	public Button getRemoveButton()
	{
		return m_buttonRemove;
	}

	private MainUI m_parent;
	private String m_firstName;
	private String m_lastName;
	private String m_email;
	private final CheckBox m_grantAdminStatus = new CheckBox("Adminrechte");
	private final Button   m_buttonRemove     = new Button("Entfernen");
}
