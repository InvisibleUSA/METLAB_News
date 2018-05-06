package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.server.ClientSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ejb.Local;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;



class BaseXConnectorTest
{

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}
	@Test
	void query()
	{
		//TODO write test
		ConfigurationManager cm = mock(ConfigurationManager.class);
		when(cm.getBaseXPassword()).thenReturn("mepmep");
		when(cm.getBaseXUsername()).thenReturn("mepmep");
		when(cm.getBaseXPath()).thenReturn("mepmep");
		//ClientSession
	}

	@Test
	void addProfile()
	{
		QueryAddProfile qap = new QueryAddProfile();
		qap.name = "abctest" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		ArrayList<String> keywords = new ArrayList<>();
		ArrayList<String> sources = new ArrayList<>();
		keywords.add("USA");
		sources.add("Spiegel");
		qap.profile = new ObservationProfile(qap.name, "metlabnews@gmail.com", keywords, sources, LocalTime.now().plusSeconds(60));
		assert qap.execute();
	}
}