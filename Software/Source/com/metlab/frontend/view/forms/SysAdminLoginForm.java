package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class SysAdminLoginForm extends VerticalLayout
{
	private final Label         title             = new Label(
			"Willkommen bei METLAB-News - Anmeldung Systemadministrator");
	private final TextField     textFieldEmail    = new TextField("E-Mail:");
	private final PasswordField textFieldPassword = new PasswordField("Passwort:");
	private final Button        buttonLogin       = new Button("Anmelden");
	private final Button        buttonUser        = new Button("Nutzer-Anmeldung");

	private final HorizontalLayout buttonBar = new HorizontalLayout();

	public SysAdminLoginForm(ICallbackFunction sysLoginCallback,
	                         ICallbackFunction enterUserLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Anmeldung Systemadministrator");

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             sysLoginAction(sysLoginCallback));

		buttonUser.addClickListener((Button.ClickEvent event) ->
				                            enterUserLoginFormCallback.execute(null));

		buttonBar.addComponents(buttonLogin, buttonUser);
		this.addComponents(title,
		                   textFieldEmail, textFieldPassword,
		                   buttonBar);
	}

	private void sysLoginAction(ICallbackFunction sysLoginCallback)
	{
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();

		if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie eine Email Adresse ein!");
		}
		if(password.isEmpty())
		{
			Notification.show("Bitte geben Sie ein Passwort ein!");
		}
		else
		{
			sysLoginCallback.execute(new String[] {email, password});
		}
	}
}
