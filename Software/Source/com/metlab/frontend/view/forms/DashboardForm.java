package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;



public class DashboardForm extends VerticalLayout
{
	private final Label            title            = new Label();
	private final Button           buttonLogout     = new Button("Abmelden");
	private final HorizontalLayout layoutFirst      = new HorizontalLayout();
	private final TabSheet         layoutOptional   = new TabSheet();
	private final HorizontalLayout layoutUser       = new HorizontalLayout();
	private final Accordion        articleContainer = new Accordion();
	private final VerticalLayout   profileSidebar   = new VerticalLayout();
	private final DateField        searchDate       = new DateField("Datum");
	private final TextField        searchInput      = new TextField("Suchbegriff");
	private final Button           searchButton     = new Button("Filtern");
	private final VerticalLayout   layoutAdmin      = new VerticalLayout();
	private final CheckBoxGroup    userList         = new CheckBoxGroup();
	private final HorizontalLayout buttonBar        = new HorizontalLayout();
	private final Button           buttonAddUser    = new Button("Nutzer hinzufügen");
	private final Button           buttonRemoveUser = new Button("Ausgewählte Nutzer entfernen");

	public DashboardForm(String userName,
	                     Boolean isAdmin,
	                     ICallbackFunction userLogoutCallback)
	{
		Page.getCurrent().setTitle("METLAB News");
		title.setCaption("Nutzeranmeldung erfolgreich - Hallo " + userName);

		createDummyData();
		title.setWidth("400px");
		articleContainer.setWidth("400px");

		buttonLogout.addClickListener((Button.ClickEvent event) ->
				                              userLogoutCallback.execute(new String[] {userName}));

		profileSidebar.addComponents(searchDate, searchInput, searchButton);
		layoutUser.addComponents(articleContainer, profileSidebar);
		layoutFirst.addComponents(title, buttonLogout);
		if(isAdmin)
		{
			layoutOptional.addTab(layoutUser, "Nutzer");
			layoutOptional.addTab(layoutAdmin, "Admin");
			layoutAdmin.addComponents(userList, buttonBar);
			buttonBar.addComponents(buttonAddUser, buttonRemoveUser);
			this.addComponents(layoutFirst, layoutOptional);
		}
		else
		{
			this.addComponents(layoutFirst, layoutUser);
		}
	}

	private void createDummyData()
	{
		articleContainer.addTab(new Label("Lorem ipsum"), "Artikel 1");
		articleContainer.addTab(new Label("dolor sit amet"), "Artikel 2");
		articleContainer.addTab(new Label("consectetur adipiscing elit"), "Artikel 3");
		articleContainer.addTab(new Label("Funktion wurde klar, oder?"), "Artikel 4");
		articleContainer.addTab(new Label(
				                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut ut massa eget erat dapibus sollicitudin. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Pellentesque a augue. Praesent non elit. Duis sapien dolor, cursus eget, pulvinar eget, eleifend a, est. Integer in nunc. Vivamus consequat ipsum id sapien. Duis eu elit vel libero posuere luctus. Aliquam ac turpis. Aenean vitae justo in sem iaculis pulvinar. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam sit amet mi. Aenean auctor, mi sit amet ultricies pulvinar, dui urna adipiscing odio, ut faucibus odio mauris eget justo. Mauris quis magna quis augue interdum porttitor. Sed interdum, tortor laoreet tincidunt ullamcorper, metus velit hendrerit nunc, id laoreet mauris arcu vitae est. Nulla nec nisl. Mauris orci nibh, tempor nec, sollicitudin ac, venenatis sed, lorem. Quisque dignissim tempus erat. Maecenas molestie, pede ac ultrices interdum, felis neque vulputate quam, in sodales felis odio quis mi. Aliquam massa pede, pharetra quis, tincidunt quis, fringilla at, mauris. Vestibulum a massa. Vestibulum luctus odio ut quam. Maecenas congue convallis diam. Cras urna arcu, vestibulum vitae, blandit ut, laoreet id, risus. Ut condimentum, arcu sit amet placerat blandit, augue nibh pretium nunc, in tempus sem dolor non leo. Etiam fringilla mauris a odio. Nunc lorem diam, interdum eget, lacinia in, scelerisque sit amet, purus. Nam ornare. Donec placerat dui ut orci. Phasellus quis lacus at nisl elementum cursus. Cras bibendum egestas nulla. Phasellus pulvinar ullamcorper odio. Etiam ipsum. Proin tincidunt. Aliquam aliquet. Etiam purus odio, commodo sed, feugiat volutpat, scelerisque molestie, velit. Aenean sed sem sit amet libero sodales ultrices. Donec dictum, arcu sed iaculis porttitor, est mauris pulvinar purus, sit amet porta purus neque in risus. Mauris libero. Maecenas rhoncus. Morbi quis nisl. Vestibulum laoreet tortor eu elit. Cras euismod nulla eu sapien. Sed imperdiet. Maecenas vel sapien. Nulla at purus eu diam auctor lobortis. Donec pede eros, lacinia tincidunt, tempus eu, molestie nec, velit. Nullam ipsum odio, euismod non, aliquet nec, consequat ac, felis. Duis fermentum mauris sed justo. Suspendisse potenti. Praesent at libero sit amet ipsum imperdiet fermentum. Aliquam enim nisl, dictum id, lacinia sit amet, elementum posuere, ipsum. Integer luctus dictum libero. Pellentesque sed pede sed nisl bibendum porttitor. Phasellus tempor interdum nisi. Mauris nec magna. Phasellus massa pede, vehicula sed, ornare at, ullamcorper ut, nisl. Sed turpis nisl, hendrerit sit amet, consequat id, auctor nec, arcu. Quisque fringilla tincidunt massa. In eleifend, nulla sed mollis vestibulum, mauris orci facilisis ante, id pharetra dolor ipsum vitae sem. Integer dictum. Nunc ut odio."),
		                        "Artikel 5");
		userList.setItems(new String[] {"Marco", "Erik", "Tobi", "Lukas", "Achim", "Benni", "METLAB"});
	}
}