package me.metlabnews.Model.DataAccess;

import me.metlabnews.Model.DataAccess.DbConnectors.MariaDbConnector;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
//import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class MariaDbConnectorTest
{

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}


	@Test
	public void addSubscriber()
	{
		try
		{
			Organisation organisation = new Organisation("TestOrg");
			String email = "mm1@test.de";
			String password = "pass";
			String firstName = "Max";
			String lastName = "Mustermann";

			Subscriber subscriber = new Subscriber(email, password, firstName, lastName,
			                                       organisation, true);
			MariaDbConnector.getInstance().addSubscriber(subscriber);
		}
		catch(Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void getSubscriberByEmail()
	{
	}

	@Test
	public void addOrganisation()
	{
		Organisation organisation = new Organisation("TestOrg");
		try
		{
			MariaDbConnector.getInstance().addOrganisation(organisation);
		}
		catch(DataCouldNotBeAddedException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void getOrganisationByName()
	{
	}
}