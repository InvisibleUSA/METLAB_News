package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SystemAdminLoginView
		extends VerticalLayout implements IView
{
	public SystemAdminLoginView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Anmelden");

		buttonLogin.addClickListener((Button.ClickEvent event) -> loginAction());

		buttonToSubscriberLogin.addClickListener((Button.ClickEvent event)
				                                -> m_parent.openSubscriberLoginView());

		this.addComponents(title, textFieldEmail, textFieldPassword, buttonLogin,
		                   buttonToSubscriberLogin);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Anmelden");
	}


	private void loginAction()
	{
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();

		if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie Ihre Email-Adresse ein!");
		}
		else if(password.isEmpty())
		{
			Notification.show("Bitte geben Sie Ihr Passwort ein!");
		}
		else
		{
			m_parent.loginSysAdmin(email, password);
		}
	}



	private MainUI m_parent;

	private final Label         title                   = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldEmail          = new TextField("E-Mail:");
	private final PasswordField textFieldPassword       = new PasswordField("Passwort:");
	private final Button        buttonLogin             = new Button("Anmelden");
	private final Button        buttonToSubscriberLogin = new Button("Zurück zur Anmeldeseite für Abonnenten");
}
