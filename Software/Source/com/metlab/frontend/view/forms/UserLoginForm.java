package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class UserLoginForm extends VerticalLayout
{
	private final Label         title             = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldEmail    = new TextField("E-Mail:");
	private final PasswordField textFieldPassword = new PasswordField("Passwort:");
	private final Button        buttonLogin       = new Button("Anmelden");
	private final Button        buttonRegister    = new Button("Registrierung");
	private final Button        buttonSysAdmin    = new Button("Systemadministrator-Anmeldung");

	private final HorizontalLayout buttonBar = new HorizontalLayout();

	public UserLoginForm(ICallbackFunction loginCallback,
	                     ICallbackFunction enterRegisterFormCallback,
	                     ICallbackFunction enterSysAdminLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Anmeldung");

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             loginAction(loginCallback));

		buttonRegister.addClickListener((Button.ClickEvent event) ->
				                                enterRegisterFormCallback.execute(null));

		buttonSysAdmin.addClickListener((Button.ClickEvent event) ->
				                                enterSysAdminLoginFormCallback.execute(null));

		buttonBar.addComponents(buttonLogin, buttonRegister, buttonSysAdmin);
		this.addComponents(title,
		                   textFieldEmail, textFieldPassword,
		                   buttonBar);
	}

	public void displayInfo_NotRegistered()
	{
		Notification.show("Email Adresse nicht registriert!");
	}

	public void displayInfo_PasswordIncorrect()
	{
		Notification.show("Passwort nicht korrekt!");
	}

	public void displayInfo_UnknownError()
	{
		Notification.show("Ein unbekannter Fehler ist aufgetreten.");
	}

	private void loginAction(ICallbackFunction loginCallback)
	{
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();

		if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie eine Email Adresse ein!");
			return;
		}
		else if(password.isEmpty())
		{
			Notification.show("Bitte geben Sie ein Passwort ein!");
			return;
		}

		loginCallback.execute(new String[] {email, password});
	}
}
