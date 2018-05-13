package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import me.metlabnews.UserInterface.MainUI;



/**
 * The form displayed after logout
 */
public class LogoutView extends VerticalLayout implements IView
{
	/**
	 * Constructs the form displayed after logout
	 * @param parent the object owning this view
	 */
	public LogoutView(MainUI parent)
	{
		m_parent = parent;

		buttonLogin.addClickListener((Button.ClickEvent event)
				                             -> m_parent.openSubscriberLoginView());

		this.addComponents(title, buttonLogin);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
	}

	private MainUI m_parent;

	private final Label  title       = new Label("Auf Wiedersehen!");
	private final Button buttonLogin = new Button("Erneut anmelden");
}
