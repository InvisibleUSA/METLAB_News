package com.metlab.frontend.view;

import com.metlab.frontend.controller.SQLController;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;



public class UserRegisterUI extends UI
{

	//Layouts
	final FormLayout registerForm = new FormLayout();
	final HorizontalLayout titleBar = new HorizontalLayout();

	//Form components
	TextField     userNameTextField       = new TextField("Benutzername:");
	TextField     nameTextField           = new TextField("Name:");
	TextField     preNameTextField        = new TextField("Vorname:");
	TextField     companyTextField        = new TextField("Firma:");
	PasswordField passwordTextField       = new PasswordField("Passwort:");
	PasswordField repeatPasswordTextField = new PasswordField("Passwort wiederholen:");
	TextField     emailTextField          = new TextField("Email:");
	TextField     repeatEmailTextField    = new TextField("Email wiederholen:");
	TextField     sexTextField            = new TextField("Geschlecht:");

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
		userNameTextField.setValue(aUsername);
		passwordTextField.setValue(aPassword);
		repeatPasswordTextField.setValue(aPassword);

		submit.addClickListener(e ->
		     {
		     	SQLController submitReg = new SQLController();
			     submitReg.test(emailTextField.getValue(),
			                    companyTextField.getValue(),
			                    sexTextField.getValue(),
			                    nameTextField.getValue(),
			                    passwordTextField.getValue(),
			                    preNameTextField.getValue());
		     });

		titleBar.addComponent(title);
		registerForm.addComponents(titleBar, userNameTextField, nameTextField, preNameTextField, companyTextField,
		                           passwordTextField, repeatPasswordTextField, emailTextField, repeatEmailTextField,
		                           submit,
		                           sexTextField);
		setContent(registerForm);
	}

}
