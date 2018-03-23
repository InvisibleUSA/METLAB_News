package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;



public class UserLoginForm extends VerticalLayout
{
	public UserLoginForm(ICallbackFunction loginCallback,
	                     ICallbackFunction enterRegisterFormCallback,
	                     ICallbackFunction enterSysAdminLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Anmeldung");

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             loginCallback.execute(new String[] {
						                             textFieldEmail.getValue(),
						                             textFieldPassword.getValue()}));

		buttonRegister.addClickListener((Button.ClickEvent event) ->
				                                enterRegisterFormCallback.execute(null));

		buttonSysAdmin.addClickListener((Button.ClickEvent event) ->
				                                enterSysAdminLoginFormCallback.execute(null));

		this.addComponents(title,
		                   textFieldEmail, textFieldPassword,
		                   buttonLogin, buttonRegister, buttonSysAdmin);
	}

	private final Label         title             = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldEmail    = new TextField("E-Mail:");
	private final PasswordField textFieldPassword = new PasswordField("Passwort:");
	private final Button        buttonLogin       = new Button("Anmelden");
	private final Button        buttonRegister    = new Button("Registrieren");
	private final Button        buttonSysAdmin    = new Button("Systemadministrator-Login");
}
