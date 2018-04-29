package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class HTMLMailTest
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
		HTMLMail m = new HTMLMail();
		m.send("metlabnews@gmail.com", "test", m.getHTMLContent());
	}

	@Test
	void send1()
	{
		HTMLMail m = new HTMLMail();
		m.send("metlabnews@gmail.com", "test", "<html><body><h1>test</h1><p>Hallo</p></body></html>");
	}
}