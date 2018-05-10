package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Mail.MailDeliverer;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QuerySearchArticleRSS;
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

	/**
	 * @inheritDoc generates a clipping and sends it via e-mail to its owner
	 */
	@Override
	public void run()
	{
		m_clipping = new Clipping(m_profile);
		QuerySearchArticleRSS query = new QuerySearchArticleRSS(m_profile);
		if(!query.execute())
		{
			Logger.getInstance().logError(this, "Unknown Database Error");
			return;
		}

		Logger.getInstance().logDebug(this, "Fetched potential RSS Articles");

		for(Article a : query.getCandidates())
		{
			m_clipping.addArticle(a);
		}

		sendMail();
	}


	//	public static void main(String... args)
//	{
//		ConfigurationManager.getInstance().initialize();
//		Logger.getInstance().initialize();
//
//		ObservationProfile p = new ObservationProfile("test", "test@tester.de", new ArrayList<>(), new ArrayList<>(),
//		                                              LocalTime.now());
//		ClippingGenerator cg = new ClippingGenerator(p);
//		cg.m_clipping = new Clipping(p);
//		cg.sendMail();
//	}
	private void sendMail()
	{
		MailDeliverer m = new MailDeliverer();
		m.send(m_profile.getUserMail(), "New clipping", m_clipping.prettyPrintHTML());
	}
}
