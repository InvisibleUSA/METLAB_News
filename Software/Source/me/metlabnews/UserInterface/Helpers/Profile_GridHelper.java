package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.*;
import me.metlabnews.UserInterface.MainUI;

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

		m_keywordsView.setEmptySelectionAllowed(false);
		m_keywordsView.setData(m_keywords);
		m_keywordsView.setSelectedItem("Suchbegriffe zeigen");
		m_keywordsView.addValueChangeListener(event -> m_keywordsView.setSelectedItem("Suchbegriffe zeigen"));

		m_sourcesView.setEmptySelectionAllowed(false);
		m_sourcesView.setData(m_sources);
		m_sourcesView.setSelectedItem("Quellen zeigen");
		m_sourcesView.addValueChangeListener(event -> m_sourcesView.setSelectedItem("Quellen zeigen"));

		m_checkBoxIsActive.setValue(m_isActive);
		m_checkBoxIsActive.addValueChangeListener(
				event -> m_parent.updateProfileAction(
						() -> Notification.show("Profil aktualisiert"),
						Notification::show,
						m_profileName,
						(String[])m_keywords.toArray(),
						(String[])m_sources.toArray(),
						m_interval, m_isActive));

		m_buttonDelete.addClickListener(event -> m_parent.deleteProfile(
				() -> m_buttonDelete.setEnabled(false),
				Notification::show,
				m_email,
				m_profileName));
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
		long seconds = m_interval.getSeconds();
		if(seconds < 120)
		{
			return seconds + "Sekunden";
		}
		long minutes = seconds / 60;
		if(minutes < 120)
		{
			return "etwa" + minutes + "Minuten";
		}
		long hours = minutes / 60;
		if(hours < 48)
		{
			return "etwa" + hours + "Stunden";
		}
		long days = hours / 24;
		return "etwa" + days + "Tage";
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
}
