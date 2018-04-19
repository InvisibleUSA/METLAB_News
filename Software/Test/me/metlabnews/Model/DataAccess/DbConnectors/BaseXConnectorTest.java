package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.basex.server.ClientSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;



class BaseXConnectorTest
{

	@Test
	void query()
	{
		//TODO write test
		ConfigurationManager cm = mock(ConfigurationManager.class);
		when(cm.getBaseXPassword()).thenReturn("mepmep");
		when(cm.getBaseXUsername()).thenReturn("mepmep");
		when(cm.getBaseXPath()).thenReturn("mepmep");
		//ClientSession
	}
}