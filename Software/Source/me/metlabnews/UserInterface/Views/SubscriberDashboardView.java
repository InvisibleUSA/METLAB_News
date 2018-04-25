package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.MainUI;



/**
 * The dashboard for subscribers
 * Contains clippings and profiles options
 */
public class SubscriberDashboardView extends VerticalLayout implements IView
{
	private MainUI m_parent;

	private final Label  title               = new Label("Willkommen bei METLAB-News - Dashboard");
	private final Button buttonShowClippings = new Button("Zeige Pressespiegel");
	private final Button buttonQuitAccount   = new Button("Konto löschen");
	private final Button buttonLogout        = new Button("Abmelden");
	private final Panel  panelClippings      = new Panel("Bisherige Pressespiegel");

	/**
	 * Initializes the view and sets all of its components to their default values
	 *
	 * @param parent the parent object of this view
	 */
	public SubscriberDashboardView(MainUI parent)
	{
		m_parent = parent;

		buttonLogout.addClickListener((Button.ClickEvent event) -> m_parent.logout());

		buttonQuitAccount.addClickListener((Button.ClickEvent event) ->
				                                   m_parent.removeSubscriber(m_parent::openLogoutView,
				                                                             errorMessage -> {
					                                                             Notification popup = new Notification(
							                                                             errorMessage,
							                                                             Notification.Type.WARNING_MESSAGE);
					                                                             popup.setDelayMsec(3000);
					                                                             popup.show(Page.getCurrent());
				                                                             },
				                                                             m_parent.whoAmI().getEmail()));

		buttonShowClippings.addClickListener((Button.ClickEvent event) ->
		                                     {
			                                     Notification popup = new Notification(/*TODO*/"Not implemented jet",
			                                                                                   Notification.Type.WARNING_MESSAGE);
			                                     popup.setDelayMsec(3000);
			                                     popup.show(Page.getCurrent());
		                                     });

		this.addComponents(title, buttonQuitAccount, buttonShowClippings, panelClippings, buttonLogout);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Dashboard");
	}
}
