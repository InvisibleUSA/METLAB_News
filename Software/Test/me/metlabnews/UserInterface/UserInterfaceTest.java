package me.metlabnews.UserInterface;

import me.metlabnews.Model.Entities.Organisation;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;



public class UserInterfaceTest
{

	@Test
	public void registerNewUser()
	{
		Organisation b = mock(Organisation.class);
		when(b.getName()).thenReturn("mepmep");

		Assert.assertTrue(b.getName().equals("mepmep"));
	}
}
