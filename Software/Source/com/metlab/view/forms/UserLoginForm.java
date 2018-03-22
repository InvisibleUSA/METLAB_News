package com.metlab.view.forms;

import com.metlab.controller.ICallbackFunction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;



public class UserLoginForm extends VerticalLayout
{
	public UserLoginForm(ICallbackFunction loginCallback)
	{
		textFieldEmail.setCaption("E-Mail:");
		textFieldPassword.setCaption("Passwort:");
		buttonLogin.addClickListener((Button.ClickEvent event) ->
		{
			loginCallback.execute(new String[]{ textFieldEmail.getValue(),
					textFieldPassword.getValue() });
		});

		this.addComponents(textFieldEmail, textFieldPassword, buttonLogin);
	}



	private final TextField textFieldEmail = new TextField();;
	private final PasswordField textFieldPassword = new PasswordField();
	private final Button buttonLogin =  new Button("Anmelden");
}
