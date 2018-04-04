package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import me.metlabnews.UserInterface.MainUI;



public class SubscriberDashboardView extends VerticalLayout
{
	public SubscriberDashboardView(MainUI parent)
	{
		m_parent = parent;
		Page.getCurrent().setTitle("Dashboard");
		this.addComponents(title);
	}



	private MainUI m_parent;

	private final Label title = new Label("Willkommen bei METLAB-News - Dashboard");
}
