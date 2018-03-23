package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;



public class UserLoginForm extends VerticalLayout
{
	public UserLoginForm(ICallbackFunction loginCallback, ICallbackFunction enterRegisterFormCallback)
	{
		Page.getCurrent().setTitle("Anmelden");
		textFieldEmail.setCaption("E-Mail:");
		textFieldPassword.setCaption("Passwort:");
		buttonLogin.addClickListener((Button.ClickEvent event) ->
		{
			loginCallback.execute(new String[]{ textFieldEmail.getValue(),
					textFieldPassword.getValue() });
		});
		buttonRegister.addClickListener((Button.ClickEvent event) ->
		{
			enterRegisterFormCallback.execute(null);
		});
		this.addComponents(textFieldEmail, textFieldPassword, buttonLogin, buttonRegister, buttonSysAdmin);
	}



	private final TextField textFieldEmail = new TextField();
	private final PasswordField textFieldPassword = new PasswordField();
	private final Button buttonLogin =  new Button("Anmelden");
	private final Button buttonRegister = new Button("Registrieren");
	private final Button buttonSysAdmin = new Button("Systemadministrator");
}
