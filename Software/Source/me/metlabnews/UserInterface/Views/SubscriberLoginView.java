package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Model.Common.Mail.MailDeliverer;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryLoginUser;
import me.metlabnews.Presentation.Messages;
import me.metlabnews.UserInterface.MainUI;



/**
 * The login form for subscribers and client administrators
 */
@SuppressWarnings("FieldCanBeLocal")
public class SubscriberLoginView extends VerticalLayout implements IView
{
	/**
	 * Constructs the login form for subscribers and client administrators
	 * @param parent the object owning this view
	 */
	public SubscriberLoginView(MainUI parent)
	{
		m_parent = parent;

		buttonLogin.addClickListener((Button.ClickEvent event) -> loginAction());

		buttonRegister.addClickListener((Button.ClickEvent event)
				                                -> m_parent.openSubscriberRegisterView());

		buttonToSysAdminLogin.addClickListener((Button.ClickEvent event)
				                                       -> m_parent.openSystemAdminLoginView());
		buttonForgotPassword.addClickListener((Button.ClickEvent event) -> passwordForgotAction());

		this.addComponents(title, layout);
		layout.addComponents(panelLogin, panelOptions);
		panelLogin.setContent(layoutLogin);
		layoutLogin.addComponents(textFieldEmail, textFieldPassword, buttonLogin);
		panelOptions.setContent(layoutOptions);
		layoutOptions.addComponents(buttonRegister, buttonToSysAdminLogin, buttonForgotPassword);
	}

	/**
	 * Executes the action of changing a password
	 */
	private void passwordForgotAction()
	{
		if(textFieldEmail.getValue().isEmpty())
		{
			Notification.show("Bitte geben sie Ihre Email-Adresse ein!");
			return;
		}
		QueryLoginUser qlu = new QueryLoginUser();
		qlu.email = textFieldEmail.getValue();
		qlu.checkPassword = false;
		if(!qlu.execute())
		{
			Notification.show(Messages.UnknownError);
			return;
		}
		if(qlu.subscriber == null)
		{
			Notification.show(Messages.InvalidEmailAddress);
			return;
		}
		MailDeliverer md = new MailDeliverer();
		md.send(textFieldEmail.getValue(), "Passwort wiederherstellen",
		        "Ihr Passwort: " + qlu.subscriber.getPassword());
		Notification.show("Email zur Passwortwiederherstellung wurde versendet");
	}

	/**
	 * resets the text fields in the view
	 */
	public void clearFields()
	{
		textFieldEmail.setValue("");
		textFieldPassword.setValue("");
	}

	@Override
	public void show()
	{
		m_parent.setContent(this);
	}

	private MainUI m_parent;

	private final Label            title                 = new Label("Willkommen bei METLAB-News");
	private final HorizontalLayout layout                = new HorizontalLayout();
	private final Panel            panelLogin            = new Panel("Anmeldung");
	private final VerticalLayout   layoutLogin           = new VerticalLayout();
	private final TextField        textFieldEmail        = new TextField("E-Mail:");
	private final PasswordField    textFieldPassword     = new PasswordField("Passwort:");
	private final Button           buttonLogin           = new Button("Anmelden");
	private final Panel            panelOptions          = new Panel("Weitere Optionen");
	private final VerticalLayout   layoutOptions         = new VerticalLayout();
	private final Button           buttonToSysAdminLogin = new Button("Zur Anmeldeseite für Systemadministratoren");
	private final Button           buttonRegister        = new Button("Zur Registrierung");
	private final Button           buttonForgotPassword  = new Button("Passwort vergessen");

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
			m_parent.loginSubscriber(email, password);
		}
	}
}
