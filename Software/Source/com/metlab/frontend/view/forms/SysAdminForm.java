package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class SysAdminForm extends VerticalLayout
{
	public SysAdminForm(String userName,
	                    ICallbackFunction sysAdminLogoutCallback)
	{
		Page.getCurrent().setTitle("METLAB Administration");

		title.setCaption("Systemadministratoranmeldung erfolgreich - Hallo " + userName);

		buttonLogout.addClickListener((Button.ClickEvent event) ->
				                              sysAdminLogoutCallback.execute(new String[] {userName}));

		this.addComponents(title,
		                   buttonLogout);
	}

	private final Label  title        = new Label();
	private final Button buttonLogout = new Button("Abmelden");
}
