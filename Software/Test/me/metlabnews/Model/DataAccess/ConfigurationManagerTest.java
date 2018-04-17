package me.metlabnews.Model.DataAccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class ConfigurationManagerTest
{
	@BeforeAll
	static void initialize()
	{
		ConfigurationManager.getInstance().initialize();
	}


	// region Security

	@Test
	void getSecurityPasswordMinimumLength()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordMinimumLength();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordMinimumLength() failed: ", e);
		}
	}

	@Test
	void getSecurityPasswordLowerCaseLetterRequired()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordLowerCaseLetterRequired();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordLowerCaseLetterRequired() failed: ", e);
		}
	}

	@Test
	void getSecurityPasswordUpperCaseLetterRequired()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordUpperCaseLetterRequired();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordUpperCaseLetterRequired() failed: ", e);
		}
	}

	@Test
	void getSecurityPasswordSpecialCharacterRequired()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordSpecialCharacterRequired();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordSpecialCharacterRequired() failed: ", e);
		}
	}

	@Test
	void getSecurityPasswordDigitRequired()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordDigitRequired();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordDigitRequired() failed: ", e);
		}
	}

	@Test
	void getSecurityPasswordWhitespaceForbidden()
	{
		try
		{
			ConfigurationManager.getInstance().getSecurityPasswordWhitespaceForbidden();
		}
		catch(Exception e)
		{
			fail("getSecurityPasswordWhitespaceForbidden() failed: ", e);
		}
	}

	// endregion Security


	// region RDBMS

	@Test
	void getRdbmsDriver()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsDriver();
		}
		catch(Exception e)
		{
			fail("getRdbmsDriver() failed: ", e);
		}
	}

	@Test
	void getRdbmsRemoteUrl()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsRemoteUrl();
		}
		catch(Exception e)
		{
			fail("getRdbmsRemoteUrl() failed: ", e);
		}
	}

	@Test
	void getRdbmsLocalUrl()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsLocalUrl();
		}
		catch(Exception e)
		{
			fail("getRdbmsLocalUrl() failed: ", e);
		}
	}

	@Test
	void getRdbmsUseLocalDB()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsUseLocalDB();
		}
		catch(Exception e)
		{
			fail("getRdbmsUseLocalDB() failed: ", e);
		}
	}

	@Test
	void getRdbmsUsername()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsUsername();
		}
		catch(Exception e)
		{
			fail("getRdbmsUsername() failed: ", e);
		}
	}

	@Test
	void getRdbmsPassword()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsPassword();
		}
		catch(Exception e)
		{
			fail("getRdbmsPassword() failed: ", e);
		}
	}

	@Test
	void getRdbmsSqlDialect()
	{
		try
		{
			ConfigurationManager.getInstance().getRdbmsSqlDialect();
		}
		catch(Exception e)
		{
			fail("getRdbmsSqlDialect() failed: ", e);
		}
	}

	// endregion RDBMS
}