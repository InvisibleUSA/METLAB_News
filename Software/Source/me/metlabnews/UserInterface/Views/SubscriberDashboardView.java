package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberDashboardView extends VerticalLayout
{
	public SubscriberDashboardView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Dashboard");

		buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		buttonQuitAccount.addClickListener((Button.ClickEvent event) ->
			m_parent.removeSubscriber(m_parent::openLogoutView,
			                          errorMessage -> Notification.show(errorMessage),
			                          m_parent.whoAmI().getEmail()));

		this.addComponents(title, buttonQuitAccount, buttonLogout);
	}



	private MainUI m_parent;

	private final Label title = new Label("Willkommen bei METLAB-News - Dashboard");
	private final Button buttonQuitAccount = new Button("Konto löschen");
	private final Button buttonLogout = new Button("Abmelden");
}
