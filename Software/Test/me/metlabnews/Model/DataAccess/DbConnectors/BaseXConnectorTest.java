package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetClippings;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.server.ClientSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



class BaseXConnectorTest
{

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}

	@Test
	void addProfile()
	{
		QueryAddProfile qap = new QueryAddProfile();
		String name = "abctest" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		ArrayList<String> keywords = new ArrayList<>();
		ArrayList<String> sources = new ArrayList<>();
		keywords.add("USA");
		sources.add("Spiegel");
		qap.profile = new ObservationProfile(name, "metlabnews@gmail.com", "unemployed",
		                                     keywords, sources, /*LocalDateTime.now(),*/ Duration.ofSeconds(60));
		assert qap.execute();
	}

	@Test
	void queryParallel()
	{
		Thread[] queries = new Thread[1];
		Runnable query = () -> {
			QueryGetClippings q = new QueryGetClippings();
			q.execute();
		};
		for(int i = 0; i < 1; i++)
		{
			queries[i] = new Thread(query);
		}
		for(Thread t : queries)
		{
			t.start();
		}
		System.out.println("Threads started.");
	}
}