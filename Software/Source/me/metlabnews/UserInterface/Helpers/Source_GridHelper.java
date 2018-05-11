package me.metlabnews.UserInterface.Helpers;

import me.metlabnews.UserInterface.MainUI;
import com.vaadin.ui.*;



public class Source_GridHelper
{
	public Source_GridHelper(MainUI parent, String name, String link, String rssLink)
	{
		m_parent = parent;
		m_name = name;
		m_link = link;
		m_rssLink = rssLink;

		m_buttonRemoveSource.addClickListener(event -> m_parent.removeSource(
				() -> {
					m_buttonRemoveSource.setEnabled(false);
					Notification.show("Quelle entfernt");
				},
				errorMessage -> Notification.show("Quelle konnte nicht entfernt werden!\n" + errorMessage),
				m_name));
	}

	public String getName()
	{
		return m_name;
	}

	public String getLink()
	{
		if(m_link.isEmpty())
		{
			return "nicht vorhanden";
		}
		return m_link;
	}

	public String getRssLink()
	{
		if(m_rssLink.isEmpty())
		{
			return "nicht vorhanden";
		}
		return m_rssLink;
	}

	public Button getRemoveButton()
	{
		return m_buttonRemoveSource;
	}

	private MainUI m_parent;
	private String m_name;
	private String m_link;
	private String m_rssLink;
	private final Button m_buttonRemoveSource = new Button("Quelle entfernen");
}
