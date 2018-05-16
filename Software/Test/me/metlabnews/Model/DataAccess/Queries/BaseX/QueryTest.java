package me.metlabnews.Model.DataAccess.Queries.BaseX;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



class QueryTest
{

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();


		m_originalKeywords.add("Revolution");
		m_originalKeywords.add("Buergerkrieg");
		m_originalKeywords.add("Chaos");

		m_originalSources.add("Stuttgarter Zeitung");
		m_originalSources.add("FAZ");

		QueryAddProfile qap = new QueryAddProfile();
		qap.profile = new ObservationProfile(m_originalName, m_originalUserMail, m_originalOrganization,
		                                     m_originalKeywords, m_originalSources, m_originalDuration);
		m_originalID = qap.profile.getID();
		assert qap.execute();
		assert qap.result.isEmpty();
	}

	@Test
	void QueryProfilesByEmail()
	{
		QueryGetProfilesByEmail qgp = new QueryGetProfilesByEmail();
		qgp.subscriberEmail = m_originalUserMail;
		assert qgp.execute();

		if(qgp.getResults().size() > 1)
		{
			System.out.println("Profile for same email already in DB. Test might fail because of that.");
		}

		assertNotEquals(0, qgp.getResults().size());
		for(ObservationProfile op : qgp.getResults())
		{
			assertEquals(m_originalDuration, op.getGenerationPeriod());
			assertEquals(m_originalUserMail, op.getUserMail());
			assertEquals(m_originalOrganization, op.getOrganisationID());
			assertEquals(m_originalID, op.getID());
			assertEquals(m_originalName, op.getName());
			assertIterableEquals(m_originalKeywords, op.getKeywords());
			assertIterableEquals(m_originalSources, op.getSources());
			assertFalse(op.isActive());
			//maybe check lastgeneration =~= now()
		}
	}

	@Test
	void QueryProfileById()
	{
		QueryGetProfileById qgp = new QueryGetProfileById();
		qgp.profileID = m_originalID;
		assert qgp.execute();

		ObservationProfile op = qgp.getProfile();
		assertNotNull(op);
		assertEquals(m_originalDuration, op.getGenerationPeriod());
		assertEquals(m_originalUserMail, op.getUserMail());
		assertEquals(m_originalOrganization, op.getOrganisationID());
		assertEquals(m_originalID, op.getID());
		assertEquals(m_originalName, op.getName());
		assertIterableEquals(m_originalKeywords, op.getKeywords());
		assertIterableEquals(m_originalSources, op.getSources());
		assertFalse(op.isActive());
		//maybe check lastgeneration =~= now()
	}

	@AfterAll
	static void cleanUp()
	{
		QueryRemoveProfile qrd = new QueryRemoveProfile();
		qrd.profileID = m_originalID;
		assert qrd.execute();
		assert qrd.result.isEmpty();
	}

	private static ArrayList<String> m_originalKeywords = new ArrayList<>();

	private static ArrayList<String> m_originalSources = new ArrayList<>();

	private static final String   m_originalUserMail     = "tester@test.com";
	private static final String   m_originalName         = "Test-Profil";
	private static final String   m_originalOrganization = "Alphabet";
	private static final Duration m_originalDuration     = Duration.ofHours(12);
	private static       String   m_originalID           = "";
}