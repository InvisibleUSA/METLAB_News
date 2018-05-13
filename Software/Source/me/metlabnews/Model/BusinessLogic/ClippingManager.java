package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetClippings;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Presentation.IUserInterface;
import me.metlabnews.Presentation.Messages;
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
	                                                 String profileID)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetClippings fetchQuery = new QueryGetClippings();
		fetchQuery.profileID = profileID;
		if(!fetchQuery.execute())
		{
			onFailure.execute(Messages.UnknownError);
			return;
		}

		onSuccess.execute();
	}


	public void addSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                      IUserInterface.IGenericFailureEvent onFailure, String name, String link, String rssLink)
	{

	}


	public void removeSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                         IUserInterface.IGenericFailureEvent onFailure, String name)
	{

	}



	private static ClippingManager m_instance = null;
}
