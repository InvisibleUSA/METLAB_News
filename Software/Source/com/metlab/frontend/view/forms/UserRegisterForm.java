package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class UserRegisterForm extends VerticalLayout
{
	private final Label         title               = new Label("Willkommen bei METLAB-News - Registrierung");
	private final TextField     textFieldFirstName  = new TextField("Vorname:");
	private final TextField     textFieldLastName   = new TextField("Nachname:");
	private final TextField     textFieldEmail      = new TextField("E-Mail:");
	private final PasswordField textFieldPassword_1 = new PasswordField("Passwort:");
	private final PasswordField textFieldPassword_2 = new PasswordField("Passwort wiederholen:");
	private final TextField     textFieldCompany    = new TextField("Firmenbezeichner:");
	private final Button        buttonRegister      = new Button("Registrieren");
	private final Button        buttonLogin         = new Button("Anmeldung");

	public UserRegisterForm(ICallbackFunction registerCallback,
	                        ICallbackFunction enterUserLoginFormCallback)
	{
		Page.getCurrent().setTitle("METLAB Registrierung");

		buttonRegister.addClickListener((Button.ClickEvent event) ->
				                                registerAction(registerCallback));

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             enterUserLoginFormCallback.execute(null));

		this.addComponents(title,
		                   textFieldFirstName, textFieldLastName, textFieldEmail, textFieldPassword_1,
		                   textFieldPassword_2, textFieldCompany,
		                   buttonRegister, buttonLogin);
	}

	private void registerAction(ICallbackFunction registerCallback)
	{
		String email      = textFieldEmail.getValue();
		String password_1 = textFieldPassword_1.getValue();
		String password_2 = textFieldPassword_2.getValue();
		String firstName  = textFieldFirstName.getValue();
		String lastName   = textFieldLastName.getValue();
		String company    = textFieldCompany.getValue();

		if(firstName.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Vornamen ein!");
		}
		else if(lastName.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Nachnamen ein!");
		}
		else if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie eine Email Adresse ein!");
		}
		else if(password_1.isEmpty() || password_2.isEmpty())
		{
			Notification.show("Bitte geben Sie ein Passwort ein!");
		}
		else if(company.isEmpty())
		{
			Notification.show("Bitte geben Sie einen Firmencode ein!");
		}
		else if(!password_1.contentEquals(password_2))
		{
			Notification.show("Bitte geben Sie übereinstimmende Passwörter ein!");
		}
		else
		{
			registerCallback.execute(new String[] {email, password_1, firstName, lastName, company});
		}
	}
}