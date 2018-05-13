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
 */
@SuppressWarnings("FieldCanBeLocal")
public class SubscriberRegistrationView extends VerticalLayout implements IView
{
	/**
	 * Constructs the registration form for new subscribers
	 *
	 * @param parent the object owning this view
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

	@Override
	public void show()
	{
		m_parent.setContent(this);
	}

	private MainUI m_parent;

	private final Label            title               = new Label("Willkommen bei METLAB-News");
	private final Panel            panelLayout         = new Panel("Registrierung");
	private final VerticalLayout   layout              = new VerticalLayout();
	private final TextField        textFieldFirstName  = new TextField("Vorname:");
	private final TextField        textFieldLastName   = new TextField("Nachname:");
	private final TextField        textFieldEmail      = new TextField("E-Mail:");
	private final PasswordField    textFieldPassword   = new PasswordField("Passwort:");
	private final CheckBox         checkBoxClientAdmin = new CheckBox("Administrator Status beantragen");
	private final Button           buttonRegister      = new Button("Registrieren");
	private final Button           buttonLogin         = new Button("Zurück zur Anmeldung");
	private final TextField        textFieldCompany    = new TextField("Organisation:");
	private final HorizontalLayout buttonBar           = new HorizontalLayout();


	private void register()
	{
		String  firstName  = textFieldFirstName.getValue();
		String  lastName   = textFieldLastName.getValue();
		String  email      = textFieldEmail.getValue();
		String  password   = textFieldPassword.getValue();
		boolean admin      = checkBoxClientAdmin.getValue();
		boolean doRegister = true;
		String company = textFieldCompany.getValue();
		if(company.isEmpty())
		{
			doRegister = false;
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
