package me.metlabnews.Model.Entities;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.XMLTag;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



class ObservationProfileTest
{
	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}

	@Test
	void toXML()
	{
		ArrayList<String> keywords = new ArrayList<>();
		keywords.add("Kinderbuch");
		keywords.add("Zeichentrick");
		ArrayList<String> sources = new ArrayList<>();
		sources.add("kika");
		sources.add("dunno");
		sources.add("dinno");
		ObservationProfile ob = new ObservationProfile("Peter Pan", "peter@pan.de",
		                                               "unemployed", keywords, sources,
		                                               Duration.ofMinutes(12));
		System.out.println(ob.toXML());
		XMLTag xt = new XMLTag(ob.toXML());
		assertEquals(xt.child("name").value(), "Peter Pan");
		assertEquals(xt.child("owner").value(), "peter@pan.de");
		assertEquals(xt.child("period").value(), "PT12M");

		XMLTag xt1 = xt;
		xt = xt1.child("keywords");
		assertEquals(xt.child("keyword", 0).value(), "Kinderbuch");
		assertEquals(xt.child("keyword", 1).value(), "Zeichentrick");

		xt = xt1.child("sources");
		assertEquals(xt.child("source", 0).value(), "kika");
		assertEquals(xt.child("source", 1).value(), "dunno");
		assertEquals(xt.child("source", 2).value(), "dinno");
	}
}