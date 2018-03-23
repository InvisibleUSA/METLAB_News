package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class DashboardForm extends VerticalLayout
{
	private final Label  title        = new Label();
	private final Button buttonLogout = new Button("Abmelden");

	public DashboardForm(String userName,
	                     ICallbackFunction userLogoutCallback)
	{
		Page.getCurrent().setTitle("METLAB News");

		title.setCaption("Nutzeranmeldung erfolgreich - Hallo " + userName);

		buttonLogout.addClickListener((Button.ClickEvent event) ->
				                              userLogoutCallback.execute(new String[] {userName}));

		this.addComponents(title,
		                   buttonLogout);
	}
}
