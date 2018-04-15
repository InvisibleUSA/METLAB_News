package me.metlabnews.Model.DataAccess.Queries;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.DbConnectors.RelationalDbConnector;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class RelationalQueryTest
{
	@BeforeAll
	public static void initialize()
	{
		ConfigurationManager.getInstance().initialize();
		RelationalDbConnector.getInstance().initialize();
		m_Organisation = new Organisation("Test");
		m_Organisation.setId(1);
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

	@Test
	public void addAndRemoveOrganisation()
	{
		Organisation organisation = new Organisation("TempOrg");
		assertTrue(new AddOrganisationQuery(organisation).execute());
		assertTrue(new RemoveOrganisationQuery(organisation).execute());
	}


	private static Organisation m_Organisation;
}