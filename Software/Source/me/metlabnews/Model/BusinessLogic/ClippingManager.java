package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetClippings;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryAddSource;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryRemoveSource;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.NewsSource;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Presentation.ClippingDataRepresentation;
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
	 * @param profileID
	 */
	public void getAllClippingsForObservationProfile(Session session, IUserInterface.IFetchClippingsEvent onSuccess,
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

		int resultCount = fetchQuery.getResults().size();
		ClippingDataRepresentation[] resultSet = new ClippingDataRepresentation[resultCount];
		for(int idx = 0; idx < resultCount; ++idx)
		{
			Clipping clipping = fetchQuery.getResults().get(idx);
			resultSet[idx] = new ClippingDataRepresentation(clipping.getGenerationTime().toString(),
			                                                clipping.prettyPrintHTML(),
			                                                clipping.getGenerationTime().toString());
		}
		onSuccess.execute(resultSet);
	}


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param name
	 * @param link
	 * @param rssLink
	 */
	public void addSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                      IUserInterface.IGenericFailureEvent onFailure, String name, String link, String rssLink)
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

		QueryAddSource query = new QueryAddSource();
		query.source = new NewsSource(name, link, rssLink);
		if(!query.execute())
		{
			onFailure.execute(Messages.InvalidSource);
		}
		else
		{
			onSuccess.execute();
		}
	}


	/**
	 * @param session
	 * @param onSuccess
	 * @param onFailure
	 * @param name
	 */
	public void removeSource(Session session, IUserInterface.IGenericEvent onSuccess,
	                         IUserInterface.IGenericFailureEvent onFailure, String name)
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

		QueryRemoveSource query = new QueryRemoveSource();
		query.uniqueName = name;
		if(!query.execute())
		{
			onFailure.execute(Messages.SourceNotFound);
		}
		else
		{
			onSuccess.execute();
		}
	}



	private static ClippingManager m_instance = null;
}
