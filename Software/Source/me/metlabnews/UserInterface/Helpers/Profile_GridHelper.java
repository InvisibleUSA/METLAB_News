package me.metlabnews.UserInterface.Helpers;

import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import me.metlabnews.UserInterface.MainUI;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;



public class Profile_GridHelper
{
	public Profile_GridHelper(MainUI parent, String email, String name, List<String> keywords, List<String> sources, LocalDateTime lastGenerationTiem, Duration interval)
	{
		m_parent = parent;
		m_email = email;
		m_profileName = name;
		m_keywordsView.setData(keywords);
		m_sourcesView.setData(sources);
		m_lastGenerationTime = lastGenerationTiem;
		m_interval = interval;

		m_keywordsView.setEmptySelectionAllowed(false);
		m_keywordsView.setSelectedItem("Suchbegriffe zeigen");
		m_keywordsView.addValueChangeListener(event -> m_keywordsView.setSelectedItem("Suchbegriffe zeigen"));
		m_sourcesView.setEmptySelectionAllowed(false);
		m_sourcesView.setSelectedItem("Quellen zeigen");
		m_sourcesView.addValueChangeListener(event -> m_sourcesView.setSelectedItem("Quellen zeigen"));

		m_buttonDelete.addClickListener(event -> m_parent.deleteProfile(
				() -> m_buttonDelete.setEnabled(false),
				errorMessage -> Notification.show(errorMessage),
				m_email,
				m_profileName));
	}

	public String getName()
	{
		return m_profileName;
	}

	public NativeSelect<String> getKeywords()
	{
		return m_keywordsView;
	}

	public NativeSelect<String> getSources()
	{
		return m_sourcesView;
	}

	public String getNextTime()
	{
		return m_lastGenerationTime.plusSeconds(m_interval.getSeconds()).toString();
	}

	public String getInterval()
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
	private String        m_email;
	private String        m_profileName;
	private LocalDateTime m_lastGenerationTime;
	private Duration      m_interval;
	private final NativeSelect<String> m_keywordsView = new NativeSelect<>();
	private final NativeSelect<String> m_sourcesView  = new NativeSelect<>();
	private final Button               m_buttonDelete = new Button("Löschen");
}
