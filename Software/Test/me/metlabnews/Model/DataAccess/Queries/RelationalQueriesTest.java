package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class RelationalQueriesTest
{
	@BeforeAll
	public static void initialize()
	{
		ConfigurationManager.getInstance().initialize();
		RelationalDbConnector.getInstance().initialize();
		m_Organisation = new Organisation(m_organisationName);
		assertTrue(new AddOrganisationQuery(m_Organisation).execute());
	}

	@AfterAll
	public static void close()
	{
		try
		{
			new RemoveOrganisationQuery(m_Organisation).execute();
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
	public void getOrganisation()
	{
		assertTrue(new GetOrganisationQuery(m_organisationName).execute());
	}

	@Test
	public void addAndRemoveTempOrganisation()
	{
		Organisation temp = new Organisation("TEMP");
		assertTrue(new AddOrganisationQuery(temp).execute());
		assertTrue(new RemoveOrganisationQuery(temp).execute());
	}

	@Test
	public void addAndRemoveSubscriber()
	{
		Subscriber subscriber = new Subscriber("max.mustermann@test.de", "123",
		                                       "Max", "Mustermann", m_Organisation,
		                                       false);
		assertTrue(new AddSubscriberQuery(subscriber).execute());
		assertTrue(new RemoveSubscriberQuery(subscriber).execute());
	}

	@Test
	public void getSystemAdministrator()
	{
		assertTrue(new GetSystemAdministratorQuery("sys.admin@test.de").execute());
	}



	private static Organisation m_Organisation;
	private final static String m_organisationName = "TestOrg";
}