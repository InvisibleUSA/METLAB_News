package me.metlabnews.Model.BusinessLogic;



import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Session;



/**
 *
 */
public class ClippingManager
{
	public static ClippingManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ClippingManager();
		}
		return m_instance;
	}

	private ClippingManager()
	{
	}


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param profileName
	 */
	public void getAllClippingsForObservationProfile(Session session, IUserInterface.IGenericEvent onSuccess,
	                                                 IUserInterface.IGenericFailureEvent onFailure,
	                                                 String profileName)
	{

	}


	public void addSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                      IUserInterface.IGenericFailureEvent onFailure, String name, String link, boolean isRss)
	{

	}


	public void removeSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                         IUserInterface.IGenericFailureEvent onFailure, String name)
	{

	}



	private static ClippingManager m_instance = null;
}
