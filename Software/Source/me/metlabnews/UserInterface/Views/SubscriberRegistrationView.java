package me.metlabnews.UserInterface.Views;



import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Model.BusinessLogic.UserManager;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetOrganisation;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.MainUI;

import java.util.Arrays;
import java.util.List;



/**
 * The registration form for new subscribers
 * Contains text fields for information about the new subscriber
 */
public class SubscriberRegistrationView extends VerticalLayout implements IView
{
	private MainUI m_parent;

	private final Label            title               = new Label("Willkommen bei METLAB-News - Registrierung");
	private final TextField        textFieldFirstName  = new TextField("Vorname:");
	private final TextField        textFieldLastName   = new TextField("Nachname:");
	private final TextField        textFieldEmail      = new TextField("E-Mail:");
	private final PasswordField    textFieldPassword   = new PasswordField("Passwort:");
	private final CheckBox         checkBoxClientAdmin = new CheckBox("Administrator Status beantragen");
	private final Button           buttonRegister      = new Button("Registrieren");
	private final Button           buttonLogin         = new Button("Zurück zur Anmeldung");
	private final HorizontalLayout buttonBar           = new HorizontalLayout();

	/**
	 * Initializes the view and sets all of its components to their default values
	 *
	 * @param parent the parent object of this view
	 */
	public SubscriberRegistrationView(MainUI parent)
	{
		m_parent = parent;

		QueryGetOrganisation qgo = new QueryGetOrganisation();
		if(!qgo.execute())
		{
			return;
		}
		List<String> data             = Arrays.asList(qgo.organisations);
		NativeSelect textFieldCompany = new NativeSelect<>("Organisation:", data);
		textFieldCompany.setEmptySelectionAllowed(false);

		textFieldCompany.addValueChangeListener(event -> getDropDownvalue(event.getValue().toString()));

		buttonRegister.addClickListener((Button.ClickEvent event) -> register());

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             m_parent.openSubscriberLoginView());

		buttonBar.addComponents(buttonRegister, buttonLogin);
		this.addComponents(title, textFieldFirstName, textFieldLastName, textFieldCompany,
		                   textFieldEmail, textFieldPassword, checkBoxClientAdmin, buttonBar);
	}

	private void getDropDownvalue(String temp)
	{
		company = temp;
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Registrieren");
	}

	private String company = "";

	private void register()
	{
		String  firstName = textFieldFirstName.getValue();
		String  lastName  = textFieldLastName.getValue();
		String  email     = textFieldEmail.getValue();
		String  password  = textFieldPassword.getValue();
		boolean admin     = checkBoxClientAdmin.getValue();

		if(company.isEmpty())
		{
			Notification popup = new Notification("Bitte wählen sie eine Organisation aus!",
			                                      Notification.Type.WARNING_MESSAGE);
			popup.setDelayMsec(3000);
			popup.show(Page.getCurrent());
		}
		else if(email.isEmpty())
		{
			Notification popup = new Notification("Bitte geben Sie eine Email Adresse ein!",
			                                      Notification.Type.WARNING_MESSAGE);
			popup.setDelayMsec(3000);
			popup.show(Page.getCurrent());
		}
		else if(password.isEmpty())
		{
			Notification popup = new Notification("Bitte geben Sie ein Passwort ein!",
			                                      Notification.Type.WARNING_MESSAGE);
			popup.setDelayMsec(3000);
			popup.show(Page.getCurrent());
		}
		else
		{
			m_parent.registerSubscriber(firstName, lastName, company, email, password, admin);
		}
	}

}
