package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;



public class ProfileManager
{
	public static ProfileManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ProfileManager();
		}
		return m_instance;
	}


	private ProfileManager()
	{
	}


	public void createNewProfile(Session session, IUserInterface.IGenericEvent onSuccess,
	                             IUserInterface.IGenericFailureEvent onFailure,
	                             String name, String userMail, ArrayList<String> keywords,
	                             ArrayList<String> sources, LocalDateTime lastGenerationTime,
	                             Duration clippingPeriod)
	{

	}




	private static ProfileManager m_instance = null;
}
