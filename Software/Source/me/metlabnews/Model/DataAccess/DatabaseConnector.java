package me.metlabnews.Model.DataAccess;

public class DatabaseConnector
{
	private MariaConnector mariaConnector = new MariaConnector();
	private BaseXConnector baseXConnector = new BaseXConnector();

	public void bla()
	{
		mariaConnector.query("Hallo");
	}
}
