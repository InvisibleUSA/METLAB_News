package me.metlabnews.Model.BusinessLogic;



public class UserManager
{
	public static UserManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new UserManager();
		}
		return m_instance;
	}

	private UserManager()
	{

	}

	public void userLogin(String email, String password)
	{
		System.out.println("[Message] Model.BusinessLogic.UserManager.userLogin():");
		System.out.println("    email: " + email + " password: " + password);
	}



	private static UserManager m_instance = null;
}
