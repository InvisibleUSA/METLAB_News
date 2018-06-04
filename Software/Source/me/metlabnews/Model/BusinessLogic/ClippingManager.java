package me.metlabnews.Model.BusinessLogic;

import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetClippings;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryAddSource;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryGetSources;
import me.metlabnews.Model.DataAccess.Queries.MariaDB.QueryRemoveSource;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.NewsSource;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import me.metlabnews.Presentation.*;

import java.util.List;



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

		int                          resultCount = fetchQuery.getResults().size();
		ClippingDataRepresentation[] resultSet   = new ClippingDataRepresentation[resultCount];
		List<Clipping>               resultList  = fetchQuery.getResults();

		int index = 0;
		for(Clipping clipping : resultList)
		{
			resultSet[index] = new ClippingDataRepresentation(clipping.getProfile().getID(),
			                                                        clipping.prettyPrintHTML(),
			                                                        clipping.getGenerationTime().toString());
			index++;
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
		if(session.getUser().getClass() != SystemAdministrator.class)
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
		if(session.getUser().getClass() != SystemAdministrator.class)
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


	public void getAvailableSources(Session session, IUserInterface.IFetchSourcesEvent onSuccess,
	                                IUserInterface.IGenericFailureEvent onFailure)
	{
		if(!session.isLoggedIn())
		{
			onFailure.execute(Messages.NotLoggedIn);
			return;
		}
		if(session.getUser().getClass() != Subscriber.class && session.getUser().getClass() != SystemAdministrator.class)
		{
			onFailure.execute(Messages.IllegalOperation);
			return;
		}

		QueryGetSources query = new QueryGetSources();
		if(!query.execute())
		{
			onFailure.execute(Messages.UnknownError);
		}
		else
		{
			int resultCount = query.getSources().size();
			SourceDataRepresentation[] resultSet = new SourceDataRepresentation[resultCount];
			for(int idx = 0; idx < resultCount; ++idx)
			{
				resultSet[idx] = new SourceDataRepresentation(query.getSources().get(idx));
			}
			onSuccess.execute(resultSet);
		}
	}



	private static ClippingManager m_instance = null;
}
