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



	private static ClippingManager m_instance = null;
}
