package me.metlabnews.UserInterface.Views;



import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberRegisterView extends VerticalLayout
{
	public SubscriberRegisterView(MainUI parent)
	{
		m_parent = parent;

		Page.getCurrent().setTitle("Registrieren");

		buttonRegister.addClickListener((Button.ClickEvent event) -> registerAction());

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             m_parent.openUserLoginView());

		buttonBar.addComponents(buttonRegister, buttonLogin);
		this.addComponents(title, textFieldFirstName, textFieldLastName, textFieldCompany,
		                   textFieldEmail, textFieldPassword, buttonBar);
	}

	private void registerAction()
	{
		String firstName = textFieldFirstName.getValue();
		String lastName = textFieldLastName.getValue();
		String company = textFieldCompany.getValue();
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
			m_parent.userRegisterAction(firstName, lastName, company, email, password);
		}
	}



	private MainUI m_parent;

	private final Label         title              = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldFirstName = new TextField("Vorname:");
	private final TextField     textFieldLastName  = new TextField("Nachname:");
	private final TextField     textFieldCompany   = new TextField("Organisation:");
	private final TextField     textFieldEmail     = new TextField("E-Mail:");
	private final PasswordField textFieldPassword  = new PasswordField("Passwort:");
	private final Button        buttonRegister     = new Button("Registrieren");
	private final Button        buttonLogin        = new Button("Zurück zur Anmeldung");
	private final HorizontalLayout buttonBar = new HorizontalLayout();
}
