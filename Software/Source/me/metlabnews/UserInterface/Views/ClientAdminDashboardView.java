package me.metlabnews.UserInterface.Views;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import me.metlabnews.Presentation.UserDataRepresentation;
import me.metlabnews.UserInterface.MainUI;



public class ClientAdminDashboardView extends VerticalLayout implements IView
{
	public ClientAdminDashboardView(MainUI parent)
	{
		m_parent = parent;

		buttonLogout.addClickListener((Button.ClickEvent event)
				                              -> m_parent.logout());

		buttonQuitAccount.addClickListener(
				event -> m_parent.removeSubscriber(m_parent::openLogoutView,
				                                   errorMessage -> {
					                                   Notification popup = new Notification(errorMessage,
					                                                                         Notification.Type.WARNING_MESSAGE);
					                                   popup.setDelayMsec(3000);
					                                   popup.show(Page.getCurrent());
				                                   },
				                                   m_parent.whoAmI().getEmail()));

		buttonShowPendingVerificationRequests.addClickListener(
				event ->
						m_parent.fetchPendingSubscriberVerifications(
								data -> showPendingVerificationRequests(data),
								errorMessage -> {
									Notification popup = new Notification(errorMessage,
									                                      Notification.Type.WARNING_MESSAGE);
									popup.setDelayMsec(3000);
									popup.show(Page.getCurrent());
								}));

		this.addComponents(title, buttonShowPendingVerificationRequests,
		                   panelSubscriberVerification, buttonLogout);
	}


	@Override
	public void show()
	{
		m_parent.setContent(this);
		Page.getCurrent().setTitle("Dashboard");
	}


	private void showPendingVerificationRequests(UserDataRepresentation[] data)
	{
		VerticalLayout table = new VerticalLayout();

		for(UserDataRepresentation subscriber : data)
		{
			HorizontalLayout row = new HorizontalLayout();
			row.addComponent(new Label(subscriber.getFirstName()));
			row.addComponent(new Label(subscriber.getLastName()));
			row.addComponent(new Label(subscriber.getEmail()));
			CheckBox grantAdminStatus = new CheckBox("Admin:");
			row.addComponent(grantAdminStatus);
			grantAdminStatus.setEnabled(subscriber.isOrganisationAdministrator());
			Button verify = new Button("Verifizieren");
			row.addComponent(verify);
			Button deny = new Button("Ablehnen");
			row.addComponent(deny);
			verify.addClickListener(
					event -> m_parent.verifySubscriber(() ->
					                                   {
						                                   verify.setEnabled(false);
						                                   deny.setEnabled(false);
					                                   },
					                                   errorMessage ->
					                                   {
						                                   Notification popup = new Notification(errorMessage,
						                                                                         Notification.Type.WARNING_MESSAGE);
						                                   popup.setDelayMsec(3000);
						                                   popup.show(Page.getCurrent());
					                                   },
					                                   subscriber.getEmail(),
					                                   grantAdminStatus.getValue()));
			deny.addClickListener(
					event -> m_parent.denySubscriber(() ->
					                                 {
						                                 verify.setEnabled(false);
						                                 deny.setEnabled(false);
					                                 },
					                                 errorMessage -> {

						                                 Notification popup = new Notification(errorMessage,
						                                                                       Notification.Type.WARNING_MESSAGE);
						                                 popup.setDelayMsec(3000);
						                                 popup.show(Page.getCurrent());
					                                 },
					                                 subscriber.getEmail()));
			table.addComponent(row);
		}
		panelSubscriberVerification.setContent(table);
	}


	private MainUI m_parent;

	private final Label  title                                 = new Label("Willkommen bei METLAB-News - Dashboard!");
	private final Button buttonShowPendingVerificationRequests = new Button("Offene Anfragen");
	private final Button buttonQuitAccount                     = new Button("Konto löschen");
	private final Button buttonLogout                          = new Button("Abmelden");
	private final Panel  panelSubscriberVerification           = new Panel("Ausstehende Verifikationen");
}
