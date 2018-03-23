package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class UserRegisterForm extends VerticalLayout
{
	public UserRegisterForm(ICallbackFunction registerCallback,
	                        ICallbackFunction enterUserLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Registrierung");

		buttonRegister.addClickListener((Button.ClickEvent event) ->
				                                registerCallback.execute(new String[] {
						                                textFieldEmail.getValue(),
						                                textFieldPassword_1.getValue(),
						                                textFieldFirstName.getValue(),
						                                textFieldLastName.getValue(),
						                                textFieldCompany.getValue()
				                                }));

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             enterUserLoginFormCallback.execute(null));

		this.addComponents(title,
		                   textFieldFirstName, textFieldLastName, textFieldEmail, textFieldPassword_1,
		                   textFieldPassword_2, textFieldCompany,
		                   buttonLogin, buttonRegister);
	}

	private final Label         title               = new Label("Willkommen bei METLAB-News - Registrierung");
	private final TextField     textFieldFirstName  = new TextField("Vorname:");
	private final TextField     textFieldLastName   = new TextField("Nachname:");
	private final TextField     textFieldEmail      = new TextField("E-Mail:");
	private final PasswordField textFieldPassword_1 = new PasswordField("Passwort:");
	private final PasswordField textFieldPassword_2 = new PasswordField("Passwort wiederholen:");
	private final TextField     textFieldCompany    = new TextField("Firmenbezeichner:");
	private final Button        buttonLogin         = new Button("Anmelden");
	private final Button        buttonRegister      = new Button("Registrieren");
}