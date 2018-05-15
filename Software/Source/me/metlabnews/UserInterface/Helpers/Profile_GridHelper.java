package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;



public class Profile_GridHelper
{
	public Profile_GridHelper(MainUI parent, String id, String email, String name,
	                          List<String> keywords, List<String> sources, Boolean isActive,
	                          LocalDateTime lastGenerationTime, Duration interval)
	{
		m_parent = parent;
		m_profileId = id;
		m_email = email;
		m_profileName = name;
		m_keywords = keywords;
		m_sources = sources;
		m_isActive = isActive;
		m_lastGenerationTime = lastGenerationTime;
		m_interval = interval;

		setupListSelects(keywords, sources);

		m_checkBoxIsActive.setValue(true);
		m_checkBoxIsActive.addValueChangeListener(
				event -> {
					m_isActive = m_checkBoxIsActive.getValue();
					m_parent.updateProfileAction(
							() -> Notification.show("Profil aktualisiert"),
							Notification::show,
							m_profileId, m_profileName,
							m_keywords.toArray(), m_sources.toArray(),
							m_interval, m_isActive);
				});

		m_buttonDelete.addClickListener(
				event -> m_parent.deleteProfile(
						() -> m_buttonDelete.setEnabled(false),
						Notification::show,
						m_email,
						m_profileId));
	}

	@Override
	public String toString()
	{
		return m_profileName;
	}

	public String getID()
	{
		return m_profileId;
	}

	public String getName()
	{
		return m_profileName;
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

	public Boolean getIsActive()
	{
		return m_isActive;
	}

	public CheckBox getIsActiveCheckBox()
	{
		return m_checkBoxIsActive;
	}

	public LocalDateTime getLastTime()
	{
		return m_lastGenerationTime;
	}

	public String getNextTime()
	{
		return m_lastGenerationTime.plusSeconds(m_interval.getSeconds()).toString();
	}

	public Duration getInterval()
	{
		return m_interval;
	}

	public String getIntervalString()
	{
		long   time    = m_interval.getSeconds();
		long seconds = time % 60;
		time = time / 60;
		long minutes = time % 60;
		time = time / 60;
		long hours = time % 24;
		time = time / 24;
		long days = time;
		return String.format("%03d:%02d:%02d:%02d", days,hours ,minutes ,seconds);
	}

	public Button getDeleteButton()
	{
		return m_buttonDelete;
	}

	private MainUI        m_parent;
	private String        m_profileId;
	private String        m_email;
	private String        m_profileName;
	private List<String>  m_keywords;
	private List<String>  m_sources;
	private Boolean       m_isActive;
	private LocalDateTime m_lastGenerationTime;
	private Duration      m_interval;
	private final NativeSelect<String> m_keywordsView     = new NativeSelect<>();
	private final NativeSelect<String> m_sourcesView      = new NativeSelect<>();
	private final CheckBox             m_checkBoxIsActive = new CheckBox("aktiv");
	private final Button               m_buttonDelete     = new Button("Löschen");

	private void setupListSelects(List<String> keywords, List<String> sources)
	{
		String       displayKeywords = "Suchbegriffe zeigen";
		String       displaySources  = "Quellen zeigen";
		List<String> selectKeywords  = new ArrayList<>();
		List<String> selectSources   = new ArrayList<>();
		selectKeywords.add(displayKeywords);
		selectSources.add(displaySources);
		selectKeywords.addAll(keywords);
		selectSources.addAll(sources);
		m_keywordsView.setEmptySelectionAllowed(false);
		m_sourcesView.setEmptySelectionAllowed(false);
		m_keywordsView.setItems(selectKeywords);
		m_sourcesView.setItems(selectSources);
		m_keywordsView.setSelectedItem(displayKeywords);
		m_sourcesView.setSelectedItem(displaySources);
		m_keywordsView.addValueChangeListener(event -> m_keywordsView.setSelectedItem(displayKeywords));
		m_sourcesView.addValueChangeListener(event -> m_sourcesView.setSelectedItem(displaySources));
	}
}
