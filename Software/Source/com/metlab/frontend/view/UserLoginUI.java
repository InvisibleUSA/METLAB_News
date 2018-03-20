package com.metlab.frontend.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;



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
	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		final VerticalLayout mainLayout = new VerticalLayout();

		final TextField TextFieldUserName = new TextField();
		TextFieldUserName.setCaption("Benutzername:");

		final TextField TextFieldPassword = new TextField();
		TextFieldPassword.setCaption("Passwort:");

		Button buttonLogin = new Button("Anmelden");
		buttonLogin.addClickListener(e ->
			{
				mainLayout.addComponent(new Label("Benutzer " + TextFieldUserName.getValue()
					+ " hat sich mit Passwort " + TextFieldPassword.getValue() + " angemeldet."));
			});

		mainLayout.addComponents(TextFieldUserName, TextFieldPassword, buttonLogin);

		setContent(mainLayout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = UserLoginUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet
	{
	}
}
