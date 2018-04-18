package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberLoginView extends VerticalLayout implements IView
{
	public SubscriberLoginView(MainUI parent)
	{
		m_parent = parent;

		buttonLogin.addClickListener((Button.ClickEvent event) -> login());

		buttonRegister.addClickListener((Button.ClickEvent event)
				                                -> m_parent.openSubscriberRegisterView());

		buttonToSysAdminLogin.addClickListener((Button.ClickEvent event)
												-> m_parent.openSystemAdminLoginView());

		addComponents(title, textFieldEmail, textFieldPassword, buttonLogin,
		              buttonRegister, buttonToSysAdminLogin);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Anmelden");
	}


	public void clearFields()
	{
		textFieldEmail.clear();
		textFieldPassword.clear();
	}


	private void login()
	{
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();

		if(email.isEmpty())
		{
			Notification.show("Bitte geben Sie Ihre Email-Adresse ein!", Notification.Type.ERROR_MESSAGE);
		}
		else if(password.isEmpty())
		{
			Notification.show("Bitte geben Sie Ihr Passwort ein!", Notification.Type.ERROR_MESSAGE);
		}
		else
		{
			m_parent.loginSubscriber(email, password);
		}
	}



	private MainUI m_parent;

	private final Label         title                 = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldEmail        = new TextField("E-Mail:");
	private final PasswordField textFieldPassword     = new PasswordField("Passwort:");
	private final Button        buttonLogin           = new Button("Anmelden");
	private final Button        buttonToSysAdminLogin = new Button("Zur Anmeldeseite für Systemadministratoren");
	private final Button        buttonRegister        = new Button("Zur Registrierung");
}
