package com.metlab.frontend.view;

import com.metlab.frontend.controller.SQLController;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import javax.xml.soap.Text;



public class UserRegisterUI extends UI
{

	//Layouts
	final FormLayout registerForm = new FormLayout();
	final HorizontalLayout titleBar = new HorizontalLayout();

	//Form components
	TextField usernameTextfield = new TextField("Benutzername:");
	TextField name = new TextField("Name:");
	TextField gName = new TextField("Vorname:");
	TextField companyTextfield = new TextField("Firma:");
	PasswordField passwordTextfield = new PasswordField("Passwort:");
	PasswordField repeatPassword = new PasswordField("Passwort wiederholen:");
	TextField emailTextfield = new TextField("Email:");
	TextField repeatEmail = new TextField("Email wiederholen:");
	TextField sex = new TextField("Geschlecht:");

	Button submit = new Button("Abschicken");

	//Panel components
	Label title = new Label("Registrierung");

	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		setContent(registerForm);

	}


	protected void setFields(String aUsername, String aPassword)
	{
		usernameTextfield.setValue(aUsername);
		passwordTextfield.setValue(aPassword);
		repeatPassword.setValue(aPassword);

		submit.addClickListener(e ->
		     {
		     	SQLController submitReg = new SQLController();
			     submitReg.test();
		     });

		titleBar.addComponent(title);
		registerForm.addComponents(titleBar,usernameTextfield, name, gName, companyTextfield,passwordTextfield,repeatPassword,emailTextfield,repeatEmail, submit, sex);
		setContent(registerForm);
	}

}
