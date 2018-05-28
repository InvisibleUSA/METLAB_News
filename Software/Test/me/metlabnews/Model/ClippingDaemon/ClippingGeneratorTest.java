package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.BaseXConnector;
import me.metlabnews.Model.DataAccess.Queries.QueryBase;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.basex.core.Command;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Replace;
import org.basex.core.cmd.XQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;



class ClippingGeneratorTest
{

	@SuppressWarnings("WeakerAccess")
	static String xmldata = "<article>\n" +
			"\t<title><![CDATA[WH Smith voted US's worst High Street shop in Which? survey]]></title>\n" +
			"\t<source><![CDATA[BBC News]]></source>\n" +
			"\t<link><![CDATA[http://www.bbc.co.uk/news/business-44274654]]></link>\n" +
			"\t<description><![CDATA[Consumers have criticised the chain's out-of-date shops, expensive products and rude staff.]]></description>\n" +
			"\t<guid><![CDATA[BBC News257]]></guid>\n" +
			"\t<isRSS>true</isRSS>\n" +
			"\t<pubDate>Mo., 28 Mai 2018 01:06:20 +0200</pubDate>\n" +
			"</article>" +
			"<article>\n" +
			"\t<title><![CDATA[UK engineer Smiths starts merger talks with US rival]]></title>\n" +
			"\t<source><![CDATA[BBC News]]></source>\n" +
			"\t<link><![CDATA[http://www.bbc.co.uk/news/business-44276146]]></link>\n" +
			"\t<description><![CDATA[Smiths has held initial discussions with US listed ICU Medical about merging their medical businesses.]]></description>\n" +
			"\t<guid><![CDATA[BBC News258]]></guid>\n" +
			"\t<isRSS>true</isRSS>\n" +
			"\t<pubDate>Mo., 28 Mai 2018 08:43:57 +0200</pubDate>\n" +
			"</article>" +
			"<article>\n" +
			"\t<title><![CDATA[Kinocharts: „Solo: A Star Wars Story“ wird zur schweren Enttäuschung]]></title>\n" +
			"\t<source><![CDATA[kino.de]]></source>\n" +
			"\t<link><![CDATA[https://www.kino.de/film/solo-a-star-wars-story-2018/news/kinocharts-solo-a-star-wars-story-wird-zur-schweren-enttaeuschung/]]></link>\n" +
			"\t<description><![CDATA[Nach dem Star Wars-Film ist vor dem Star Wars-Film. US as keyword]]></description>\n" +
			"\t<guid><![CDATA[kino.de6815]]></guid>\n" +
			"\t<isRSS>true</isRSS>\n" +
			"\t<pubDate>Mo., 28 Mai 2018 09:52:59 +0200</pubDate>\n" +
			"</article>";

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}

	@Test
	void generate()
	{
		ArgumentCaptor<Command> ac  = ArgumentCaptor.forClass(Command.class);
		BaseXConnector          bxc = null;
		try
		{
			bxc = mockBaseX();

			QueryBase.getDBConnector().replaceBaseXConnector(bxc);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		ArrayList<String> keywords = new ArrayList<>();
		ArrayList<String> sources  = new ArrayList<>();
		keywords.add("US");
		sources.add("BBC News");
		sources.add("kino.de");
		ObservationProfile op = new ObservationProfile("Testprofile", "ede1998@arcor.de", "unemployed", keywords,
		                                               sources,
		                                               Duration.ofSeconds(6));
		ClippingGenerator cg = new ClippingGenerator(op);
		cg.m_sendMail = false;
		cg.run();



		try
		{
			Mockito.verify(bxc, Mockito.atLeastOnce()).query(ac.capture());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		for(Command cmd : ac.getAllValues())
		{
			System.out.println(cmd + "\n");
		}

		Clipping c = cg.getClipping();
		System.out.println(c);

		assertEquals("<clipping>\n" +
				             "\t<profileID>" + op.getID() + "</profileID>\n" +
				             "\t<generationtime>" + c.getGenerationTime().format(
				DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss")) + "</generationtime>\n" +
				             "\t<articles>\n" +
				             "\t\t<articleID>BBC News257</articleID>\n" +
				             "\t\t<articleID>BBC News258</articleID>\n" +
				             "\t\t<articleID>kino.de6815</articleID>\n" +
				             "\t</articles>\n" +
				             "</clipping>\n", c.toString());
		Duration between = Duration.between(c.getGenerationTime(), LocalDateTime.now());
		assertFalse(between.isNegative(), "Creation time is after current time");
		assertTrue(between.compareTo(Duration.ofSeconds(15)) < 0, "Time difference is too large");
	}

	private static BaseXConnector mockBaseX() throws IOException
	{
		BaseXConnector bxc = mock(BaseXConnector.class);
		when(bxc.query(any(Replace.class))).thenReturn(""); //Update profile generation time
		when(bxc.query(any(Add.class))).thenReturn(""); //Add clipping to basex; add yacy articles
		when(bxc.query(any(XQuery.class))).thenReturn(xmldata); //Fetch RSS articles
		return bxc;
	}
}