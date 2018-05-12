package me.metlabnews.UserInterface.Views;



import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetOrganisation;
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

	private final Label          title               = new Label("Willkommen bei METLAB-News");
	private final Panel          panelLayout         = new Panel("Registrierung");
	private final VerticalLayout layout              = new VerticalLayout();
	private final TextField      textFieldFirstName  = new TextField("Vorname:");
	private final TextField      textFieldLastName   = new TextField("Nachname:");
	private final TextField      textFieldEmail      = new TextField("E-Mail:");
	private final PasswordField  textFieldPassword   = new PasswordField("Passwort:");
	private final CheckBox       checkBoxClientAdmin = new CheckBox("Administrator Status beantragen");
	private final Button         buttonRegister      = new Button("Registrieren");
	private final Button         buttonLogin         = new Button("Zurück zur Anmeldung");
	private NativeSelect textFieldCompany;
	private final HorizontalLayout buttonBar = new HorizontalLayout();

	/**
	 * Initializes the view and sets all of its components to their default values
	 *
	 * @param parent the parent object of this view
	 */
	public SubscriberRegistrationView(MainUI parent)
	{
		m_parent = parent;

		textFieldPassword.setWidth("310");
		textFieldEmail.setWidth("310");
		textFieldFirstName.setWidth("310");
		textFieldLastName.setWidth("310");

		textFieldEmail.setIcon(VaadinIcons.ENVELOPE);
		textFieldPassword.setIcon(VaadinIcons.KEY);

		QueryGetOrganisation qgo = new QueryGetOrganisation();
		if(!qgo.execute())
		{
			return;
		}
		List<String> data = Arrays.asList(qgo.organisations);
		textFieldCompany = new NativeSelect<>("Organisation:", data);
		textFieldCompany.setEmptySelectionAllowed(false);

		textFieldCompany.addValueChangeListener(event -> getDropDownvalue(event.getValue().toString()));

		buttonRegister.addClickListener((Button.ClickEvent event) -> register());

		buttonLogin.addClickListener((Button.ClickEvent event) ->
				                             m_parent.openSubscriberLoginView());

		buttonBar.addComponents(buttonRegister, buttonLogin);
		this.addComponents(title, panelLayout);
		panelLayout.setContent(layout);
		panelLayout.setWidthUndefined();
		layout.addComponents(textFieldFirstName, textFieldLastName, textFieldCompany,
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
		String  firstName  = textFieldFirstName.getValue();
		String  lastName   = textFieldLastName.getValue();
		String  email      = textFieldEmail.getValue();
		String  password   = textFieldPassword.getValue();
		boolean admin      = checkBoxClientAdmin.getValue();
		boolean doRegister = true;

		if(company.isEmpty())
		{
			doRegister = false;
			textFieldCompany.setComponentError(new UserError("Bitte wählen sie eine Organisation aus!"));
			//Notification popup = new Notification("Bitte wählen sie eine Organisation aus!", Notification.Type.WARNING_MESSAGE);
			//popup.setDelayMsec(3000);
			//popup.show(Page.getCurrent());
		}
		if(email.isEmpty())
		{
			doRegister = false;
			textFieldEmail.setPlaceholder("Bitte geben Sie eine Email Adresse ein!");
			textFieldEmail.setComponentError(new UserError("Bitte geben Sie eine Email Adresse ein!"));
			//Notification popup = new Notification("Bitte geben Sie eine Email Adresse ein!", Notification.Type.WARNING_MESSAGE);
			//popup.setDelayMsec(3000);
			//popup.show(Page.getCurrent());
		}
		if(password.isEmpty())
		{
			doRegister = false;
			textFieldPassword.setPlaceholder("Bitte geben Sie ein Passwort ein!");
			textFieldPassword.setComponentError(new UserError("Bitte geben Sie ein Passwort ein!"));
			//Notification popup = new Notification("Bitte geben Sie ein Passwort ein!", Notification.Type.WARNING_MESSAGE);
			//popup.setDelayMsec(3000);
			//popup.show(Page.getCurrent());
		}
		else if(doRegister)
		{
			m_parent.registerSubscriber(firstName, lastName, company, email, password, admin);
		}
	}
}
