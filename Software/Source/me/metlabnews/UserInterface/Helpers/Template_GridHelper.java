package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import me.metlabnews.UserInterface.MainUI;

import java.util.List;



public class Template_GridHelper
{
	public Template_GridHelper(MainUI parent, String email, String name, List<String> keywords, List<String> sources)
	{
		m_parent = parent;
		m_email = email;
		m_templateName = name;
		m_keywords = keywords;
		m_sources = sources;

		m_keywordsView.setEmptySelectionAllowed(false);
		m_keywordsView.setData(m_keywords);
		m_keywordsView.setSelectedItem("Suchbegriffe zeigen");
		m_keywordsView.addValueChangeListener(event -> m_keywordsView.setSelectedItem("Suchbegriffe zeigen"));
		m_sourcesView.setEmptySelectionAllowed(false);
		m_sourcesView.setData(m_sources);
		m_sourcesView.setSelectedItem("Quellen zeigen");
		m_sourcesView.addValueChangeListener(event -> m_sourcesView.setSelectedItem("Quellen zeigen"));

		m_buttonDelete.addClickListener(event -> m_parent.removeTemplate(
				() -> m_buttonDelete.setEnabled(false),
				errorMessage -> Notification.show(errorMessage),
				m_templateName));
	}

	@Override
	public String toString()
	{
		return m_templateName;
	}

	public String getName()
	{
		return m_templateName;
	}

	public NativeSelect<String> getKeywordsSelect()
	{
		return m_keywordsView;
	}

	public List<String> getKeywords()
	{
		return m_keywords;
	}

	public NativeSelect<String> getSourcesSelect()
	{
		return m_sourcesView;
	}

	public List<String> getSources()
	{
		return m_sources;
	}

	public Button getDeleteButton()
	{
		return m_buttonDelete;
	}

	private MainUI       m_parent;
	private String       m_email;
	private String       m_templateName;
	private List<String> m_keywords;
	private List<String> m_sources;
	private final NativeSelect<String> m_keywordsView = new NativeSelect<>();
	private final NativeSelect<String> m_sourcesView  = new NativeSelect<>();
	private final Button               m_buttonDelete = new Button("Löschen");
}
