package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



class ClippingDaemonTest
{
	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
		Logger.getInstance().disable(Logger.Channel.XMLTag, Logger.LogLevel.DEBUG);
	}

	@Test
	void run()
	{
		QueryAddProfile qap = new QueryAddProfile();
		String name = "abctest" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		ArrayList<String> keywords = new ArrayList<>();
		ArrayList<String> sources  = new ArrayList<>();
		keywords.add("USA");
		sources.add("Spiegel");
		qap.profile = new ObservationProfile(name, "ede1998@arcor.de", "unemployed", keywords, sources, /*LocalDateTime.now(),*/
		                                     Duration.ofSeconds(6));
		assert qap.execute();

		ClippingDaemon cd = new ClippingDaemon();
		cd.initialize();

		try
		{
			Thread.sleep(7000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		try
		{
			cd.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}