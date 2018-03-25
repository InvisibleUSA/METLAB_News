package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class SysAdminForm extends VerticalLayout
{
	private final Label            title        = new Label();
	private final Button           buttonLogout = new Button("Abmelden");
	private final HorizontalLayout headerBar    = new HorizontalLayout();
	private final Panel            placeholder  = new Panel();

	public SysAdminForm(String userName,
	                    ICallbackFunction sysAdminLogoutCallback)
	{
		Page.getCurrent().setTitle("METLAB Administration");

		title.setCaption("Systemadministratoranmeldung erfolgreich - Hallo " + userName);
		placeholder.setCaption("Platzhalter");

		buttonLogout.addClickListener((Button.ClickEvent event) ->
				                              sysAdminLogoutCallback.execute(new String[] {userName}));

		headerBar.addComponents(title, buttonLogout);
		this.addComponents(headerBar, placeholder);
	}
}
