package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import me.metlabnews.UserInterface.MainUI;

import java.time.LocalDate;


public class VerifySubscriber_GridHelper
{
	public VerifySubscriber_GridHelper(MainUI parent, String firstName, String lastName, String email, Boolean isAdmin)
	{
		m_parent = parent;
		m_firstName = firstName;
		m_lastName = lastName;
		m_email = email;
		m_grantAdminStatus.setValue(false);
		m_grantAdminStatus.setEnabled(isAdmin);
		m_buttonVerify.addClickListener(
				event -> m_parent.verifySubscriber(() ->
				                                   {
					                                   m_buttonVerify.setEnabled(false);
					                                   m_buttonDeny.setEnabled(false);
				                                   },
				                                   errorMessage ->
						                                   Notification.show(errorMessage),
				                                   m_email,
				                                   isAdmin));
		m_buttonDeny.addClickListener(
				event -> m_parent.denySubscriber(() ->
				                                 {
					                                 m_buttonVerify.setEnabled(false);
					                                 m_buttonDeny.setEnabled(false);
				                                 },
				                                 errorMessage ->
						                                 Notification.show(errorMessage),
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

	public Button getVerifyButton()
	{
		return m_buttonVerify;
	}

	public Button getDenyButton()
	{
		return m_buttonDeny;
	}

	private MainUI m_parent;
	private String m_firstName;
	private String m_lastName;
	private String m_email;
	private final CheckBox m_grantAdminStatus = new CheckBox("Adminrechte");
	private final Button   m_buttonVerify     = new Button("Verifizieren");
	private final Button   m_buttonDeny       = new Button("Ablehnen");
}
