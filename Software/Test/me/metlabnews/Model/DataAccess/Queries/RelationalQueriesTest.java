package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
//import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
//import org.junit.runners.MethodSorters;

import static org.junit.jupiter.api.Assertions.*;



// Does not work in JUnit 5
// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
// Fuck you JUnit! Just fuck you!
class RelationalQueriesTest
{
	@BeforeAll
	static void initialize()
	{
		ConfigurationManager.getInstance().initialize();
		RelationalDbConnector.getInstance().initialize();
		m_organisation = new Organisation(m_organisationName);
		assertTrue(new AddOrganisationQuery(m_organisation).execute());
	}

	@AfterAll
	static void close()
	{
		try
		{
			new RemoveOrganisationQuery(m_organisation).execute();
		}
		catch(Exception e)
		{
			// This can happen in two different scenarios:
			// 1) There is an actual bug in AddOrganisationQuery or in RemoveOrganisationQuery
			// 2) There occurs a mysteriously fucked up behaviour in MariaDB itself
			System.err.println("[MAY OR MAY NOT BE AN ERROR] " + e.toString());
		}
	}


	@Test
	void getOrganisation()
	{
		assertTrue(new GetOrganisationQuery(m_organisationName).execute());
	}

	@Test
	void addAndRemoveTempOrganisation()
	{
		Organisation temp = new Organisation("TEMP");
		assertTrue(new AddOrganisationQuery(temp).execute());
		assertTrue(new RemoveOrganisationQuery(temp).execute());
	}

	@Test
	void getSystemAdministrator()
	{
		assertTrue(new GetSystemAdministratorQuery("sys.admin@test.de").execute());
	}

	@Test
	void addVerifyRemoveSubscriber()
	{
		// necessary because JUnit is a useless heap of shit
		addSubscriber();
		verifySubscriber();
		removeSubscriber();
	}

	private void addSubscriber()
	{
		m_subscriber = new Subscriber("max.mustermann@test.de", "123",
		                              "Max", "Mustermann", m_organisation,
		                              false);
		assertTrue(new AddSubscriberQuery(m_subscriber).execute());
	}

	private void verifySubscriber()
	{
		m_subscriber.setVerificationPending(false);
		assertTrue(new UpdateSubscriberQuery(m_subscriber).execute());
	}

	private void removeSubscriber()
	{
		assertTrue(new RemoveSubscriberQuery(m_subscriber).execute());
	}



	private static Organisation m_organisation;
	private final static String m_organisationName = "TestOrg";
	private static Subscriber m_subscriber;
}