package com.metlab.view.forms;

import com.metlab.controller.ICallbackFunction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;



public class DashboardForm extends VerticalLayout
{
	public DashboardForm(String userName, ICallbackFunction userLogoutCallback)
	{
		final Label topLabel = new Label("Hallo " + userName);
		final Button logoutButton = new Button("Abmelden");
		logoutButton.addClickListener((Button.ClickEvent event) ->
			{
				userLogoutCallback.execute(null);
		    });
		this.addComponents(topLabel, logoutButton);
	}
}
