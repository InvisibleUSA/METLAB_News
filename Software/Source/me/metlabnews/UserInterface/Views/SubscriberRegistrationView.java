package me.metlabnews.UserInterface.Views;



import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberRegistrationView extends VerticalLayout implements IView
{
	public SubscriberRegistrationView(MainUI parent)
	{
		m_parent = parent;

		buttonRegister.addClickListener((Button.ClickEvent event) -> register());

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             m_parent.openSubscriberLoginView());

		buttonBar.addComponents(buttonRegister, buttonLogin);
		this.addComponents(title, textFieldFirstName, textFieldLastName, textFieldCompany,
		                   textFieldEmail, textFieldPassword, checkBoxClientAdmin, buttonBar);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Registrieren");
	}


	private void register()
	{
		String firstName = textFieldFirstName.getValue();
		String lastName = textFieldLastName.getValue();
		String company = textFieldCompany.getValue();
		String email    = textFieldEmail.getValue();
		String password = textFieldPassword.getValue();
		boolean admin = checkBoxClientAdmin.getValue();

		if(email.isEmpty())
		{
			Notification popup = new Notification("Bitte geben Sie eine Email Adresse ein!",Notification.Type.WARNING_MESSAGE);
			popup.setDelayMsec(3000);
			popup.show(Page.getCurrent());
		}
		else if(password.isEmpty())
		{
			Notification popup = new Notification("Bitte geben Sie ein Passwort ein!", Notification.Type.WARNING_MESSAGE);
			popup.setDelayMsec(3000);
			popup.show(Page.getCurrent());
		}
		else
		{
			m_parent.registerSubscriber(firstName, lastName, company, email, password, admin);
		}
	}



	private MainUI m_parent;

	private final Label         title              = new Label("Willkommen bei METLAB-News - Registrierung");
	private final TextField     textFieldFirstName = new TextField("Vorname:");
	private final TextField     textFieldLastName  = new TextField("Nachname:");
	private final TextField     textFieldCompany   = new TextField("Organisation:");
	private final TextField     textFieldEmail     = new TextField("E-Mail:");
	private final PasswordField textFieldPassword  = new PasswordField("Passwort:");
	private final CheckBox      checkBoxClientAdmin = new CheckBox("Administrator Status beantragen");
	private final Button        buttonRegister     = new Button("Registrieren");
	private final Button        buttonLogin        = new Button("Zurück zur Anmeldung");
	private final HorizontalLayout buttonBar = new HorizontalLayout();
}
