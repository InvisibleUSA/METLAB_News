package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Common.SimpleMail;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryGetRelevantArticlesFromRSS;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.ObservationProfile;



class ClippingGenerator implements Runnable
{
	static
	{
		Logger.getInstance().register(ClippingGenerator.class, Logger.Channel.ClippingDaemon);
	}
	private ObservationProfile m_profile;
	private Clipping           m_clipping;

	ClippingGenerator(ObservationProfile op)
	{
		m_profile = op;
	}

	@Override
	public void run()
	{
		m_clipping = new Clipping(m_profile);
		QueryGetRelevantArticlesFromRSS query = new QueryGetRelevantArticlesFromRSS(m_profile);
		if(!query.execute())
		{
			Logger.getInstance().logError(this, "Unknown Database Error");
			return;
		}

		Logger.getInstance().logDebug(this, "Fetched RSS Articles");

		for(Article a : query.getCandidates())
		{
			m_clipping.addArticle(a);
		}

		SimpleMail m = new SimpleMail();
		m.To = m_profile.getUserMail();
		m.Subject = "New clipping";
		m.Text = m_clipping.prettyPrint();
		m.send();
	}
}
