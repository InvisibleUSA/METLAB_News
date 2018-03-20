package com.metlab.frontend.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;

import java.io.File;



/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class UserLoginUI extends UI
{

	final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	final FileResource loginButtonIco = new FileResource(new File(basepath + "/../../Resources/Icons/loginButton.gif"));
	final FileResource registerButtonIco = new FileResource(new File(basepath + "/../../Resources/Icons/registerButton.gif"));

	@Override
	protected void init(VaadinRequest vaadinRequest)
	{

		//Variable definition
		final Panel panel = new Panel();

		final HorizontalLayout titleBar = new HorizontalLayout();

		final VerticalLayout verticalLayout = new VerticalLayout();

		final FormLayout formLayout = new FormLayout();

		final HorizontalSplitPanel horizontalLayout = new HorizontalSplitPanel();

		final TextField TextFieldUserName = new TextField("Benutzername:");

		final PasswordField TextFieldPassword = new PasswordField("Passwort:");

		Button buttonLogin = new Button(loginButtonIco);
		Button buttonRegister = new Button(registerButtonIco);

		buttonLogin.addClickListener(e ->
			{
				verticalLayout.addComponent(new Label("Benutzer " + TextFieldUserName.getValue()
					+ " hat sich mit Passwort " + TextFieldPassword.getValue() + " angemeldet."));
			});

		buttonRegister.addClickListener(e ->
		    {
		    	UserRegisterUI userReg = new UserRegisterUI();
		    	userReg.setFields(TextFieldUserName.getValue(), TextFieldPassword.getValue());
		    	setContent(userReg.registerForm);

		        verticalLayout.addComponent(new Label("Benutzer " + TextFieldUserName.getValue()
		            + " hat sich mit Passwort " + TextFieldPassword.getValue() + " registriert."));
		    });





		Label title = new Label("Willkommen bei METLAB-News");
		titleBar.addComponent(title);
		titleBar.setExpandRatio(title,1.0f);

		panel.setContent(horizontalLayout);

		horizontalLayout.setFirstComponent(buttonLogin);
		horizontalLayout.setSecondComponent(buttonRegister);

		formLayout.addComponents(TextFieldUserName, TextFieldPassword);
		verticalLayout.addComponents(titleBar, formLayout,horizontalLayout);

		setContent(verticalLayout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = UserLoginUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet
	{
	}
}
