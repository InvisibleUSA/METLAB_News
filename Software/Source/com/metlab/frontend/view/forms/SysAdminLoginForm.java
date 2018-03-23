package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class SysAdminLoginForm extends VerticalLayout
{
	public SysAdminLoginForm(ICallbackFunction sysLoginCallback,
	                         ICallbackFunction enterUserLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Anmeldung Systemadministrator");

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             sysLoginCallback.execute(new String[] {textFieldEmail.getValue(),
						                             textFieldPassword.getValue()}));

		buttonUser.addClickListener((Button.ClickEvent event) ->
				                            enterUserLoginFormCallback.execute(null));

		this.addComponents(title,
		                   textFieldEmail, textFieldPassword,
		                   buttonLogin, buttonUser);
	}

	private final Label         title             = new Label(
			"Willkommen bei METLAB-News - Anmeldung Systemadministrator");
	private final TextField     textFieldEmail    = new TextField("E-Mail:");
	private final PasswordField textFieldPassword = new PasswordField("Passwort:");
	private final Button        buttonLogin       = new Button("Anmelden");
	private final Button        buttonUser        = new Button("Nutzer-Login");
}
