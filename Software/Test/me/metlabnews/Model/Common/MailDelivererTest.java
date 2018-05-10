package me.metlabnews.Model.Common;

import me.metlabnews.Model.Common.Mail.MailDeliverer;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;



class MailDelivererTest
{

	@BeforeAll
	static void init()
	{
		ConfigurationManager.getInstance().initialize();
		Logger.getInstance().initialize();
	}

	@Test
	void send()
	{
		MailDeliverer m = new MailDeliverer();
		assertTrue(m.send("metlabnews@gmail.com", "JUnit-Test", m.getHTMLContent()));
	}

	@Test
	void send1()
	{
		MailDeliverer m = new MailDeliverer();
		assertTrue(m.send("metlabnews@gmail.com", "JUnit-Test", "<html><body><h1>test</h1><p>Hallo</p></body></html>"));
	}
}