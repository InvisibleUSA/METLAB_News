package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Mail.MailDeliverer;
import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddClipping;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddYaCyArticle;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QuerySearchArticleRSS;
import me.metlabnews.Model.DataAccess.Queries.YaCy.QuerySearchArticle;
import me.metlabnews.Model.Entities.Article;
import me.metlabnews.Model.Entities.Clipping;
import me.metlabnews.Model.Entities.ObservationProfile;



/**
 * This class is responsible for generating clippings from a given profile.
 * It also sends the resulting clipping to the profile owner and saves it for later access.
 *
 * @author Erik Hennig
 */
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
	 * @inheritDoc
	 *
	 * generates a clipping by asking YaCy and BaseX for articles
	 * saves YaCy results to BaseX, so its available for future access
	 * sends clipping via e-mail to its owner
	 * stores clipping for website access in basex
	 */
	@Override
	public void run()
	{
		m_clipping = new Clipping(m_profile);

		getRSSArticles();

		getYaCyArticles();

		sendMail();

		storeClippingInBaseX();
	}

	private void storeClippingInBaseX()
	{
		QueryAddClipping query = new QueryAddClipping();
		query.clipping = m_clipping;
		if (!query.execute())
		{
			Logger.getInstance().logError(this, "Unknown Datebase Error: Clipping could not be saved.");
		}
		Logger.getInstance().logDebug(this, "Stored clipping for profile " + "in BaseX"); //FIXME add profile ID
	}

	private void getYaCyArticles()
	{
		QuerySearchArticle query = new QuerySearchArticle();
		query.setSortByDate(true);
		query.setMaximumRecords(13);

		if(!query.execute())
		{
			Logger.getInstance().logError(this, "Unknown YaCy access error. Could not get YaCy clipping results.");
			return;
		}
		Logger.getInstance().logDebug(this, "Fetched potential HTML Articles");

		for(Article a : query.getArticles())
		{
			m_clipping.addArticle(a);
			//Write to BaseX
			QueryAddYaCyArticle q = new QueryAddYaCyArticle(a);
			if(!q.execute())
			{
				Logger.getInstance().logError(this, "Unknown Database Error");
			}
		}
	}


	private void getRSSArticles()
	{
		QuerySearchArticleRSS query = new QuerySearchArticleRSS(m_profile);
		if(!query.execute())
		{
			Logger.getInstance().logError(this, "Unknown Database Error. Could not get RSS clipping results.");
			return;
		}

		Logger.getInstance().logDebug(this, "Fetched potential RSS Articles");

		for(Article a : query.getCandidates())
		{
			m_clipping.addArticle(a);
		}
	}

	private void sendMail()
	{
		MailDeliverer m = new MailDeliverer();
		m.send(m_profile.getUserMail(), "New clipping", m_clipping.prettyPrintHTML());
	}
}
