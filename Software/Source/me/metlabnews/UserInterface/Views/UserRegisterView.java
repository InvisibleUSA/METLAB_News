package me.metlabnews.UserInterface.Views;



import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;



public class UserRegisterView extends VerticalLayout
{
	public UserRegisterView(MainUI parent)
	{
		m_parent = parent;

		Page.getCurrent().setTitle("Registrieren");

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             m_parent.openUserLoginView());

		buttonBar.addComponents(buttonRegister, buttonLogin);
		this.addComponents(title, textFieldFirstName, textFieldLastName, textFieldEmail,
			buttonBar);
	}



	private MainUI m_parent;

	private final Label         title              = new Label("Willkommen bei METLAB-News - Anmeldung");
	private final TextField     textFieldFirstName = new TextField("Vorname:");
	private final TextField     textFieldLastName  = new TextField("Nachname:");
	private final TextField     textFieldEmail     = new TextField("E-Mail:");
	private final PasswordField textFieldPassword  = new PasswordField("Passwort:");
	private final Button        buttonRegister     = new Button("Registrieren");
	private final Button        buttonLogin        = new Button("Zurück zur Anmeldung");
	private final HorizontalLayout buttonBar = new HorizontalLayout();
}
