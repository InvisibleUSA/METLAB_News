package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberLoginView extends VerticalLayout
{
	public SubscriberLoginView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Anmelden");

		buttonLogin.addClickListener((Button.ClickEvent event) -> loginAction());

		buttonRegister.addClickListener((Button.ClickEvent event) -> m_parent.openUserRegisterView());

		this.addComponents(title, textFieldEmail, textFieldPassword, buttonLogin, buttonRegister);
	}

	private void loginAction()
	{
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();

		if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie eine Email Adresse ein!");
		}
		else if(password.isEmpty())
		{
			Notification.show("Bitte geben Sie ein Passwort ein!");
		}
		else
		{
			m_parent.userLoginAction(email, password);
		}
	}



	private MainUI m_parent;

	private final Label            title             = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField        textFieldEmail    = new TextField("E-Mail:");
	private final PasswordField    textFieldPassword = new PasswordField("Passwort:");
	private final Button           buttonLogin       = new Button("Anmelden");
	private final Button           buttonRegister    = new Button("Zur Registrierung");
}
