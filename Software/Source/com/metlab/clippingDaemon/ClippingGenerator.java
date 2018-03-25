package com.metlab.clippingDaemon;

import com.metlab.controller.BaseXController;
import com.metlab.crawler.Article;
import com.metlab.crawler.Profile;
import com.metlab.crawler.Source;
import org.basex.core.cmd.Add;
import org.jsoup.Connection;

import javax.swing.text.DateFormatter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class ClippingGenerator implements Runnable
{

	private boolean             m_stop     = false;
	private LinkedList<Profile> m_profiles = new LinkedList<>(); //profiles sorted by time to generate clipping

	private LocalTime m_lastEnqueuing;

	public void stopProcessing()
	{
		m_stop = true;
	}

	@Override
	public void run()
	{
		enqueueNextProfiles(7);
		while(!m_stop)
		{
			if((m_profiles.size() <= 10) ||
					(m_lastEnqueuing.plusSeconds(10).isBefore(LocalTime.now())))
			{
				enqueueNextProfiles(5);
			}

			if(m_profiles.isEmpty())
			{
				continue;
			}

			//check if new generation must be started
			if(m_profiles.getFirst().getGenerationTime().isBefore(LocalTime.now()))
			{
				//Generate profile
				Clipping c = generateClipping(m_profiles.removeFirst());
				writeToBaseX(c);
				sendClipping(c);
			}
			Thread.yield();
		}
	}

	private void enqueueNextProfiles(int nprofiles)
	{
		//set begin of enqueueing operation to temporarily pause queries and minimize database queries
		m_lastEnqueuing = LocalTime.now();

		//FIXME neue profile werden nicht korrekt einsortiert
		//customize query string to return nprofiles profile (maximum)
		LocalTime time = (!m_profiles.isEmpty()) ? m_profiles.getLast().getGenerationTime() : m_lastEnqueuing;
		final String query = "fn:subsequence((for $profile in /profile " +
				"where xs:time('" + time + "') < xs:time($profile/generationtime) " +
				"order by $profile/generationtime return $profile), 1, " + nprofiles + ")";

		//fetch profile with customized query
		BaseXController bxc    = BaseXController.getInstance();
		String          result = bxc.query(query);


		//parse profiles and add them to the queue
		Tag profiles = new Tag("<profiles>" + result + "</profiles>");
		for(Tag profile : profiles.children("profile"))
		{
			Profile p = constructProfile(profile);
			System.out.println(p);

			m_profiles.addLast(p);
		}
	}

	private void writeToBaseX(Clipping c)
	{
		BaseXController bxc = BaseXController.getInstance();
		String filename = "/Clippings/" + c.getProfileName() + "/" + c.getGenerationTime().format(
				DateTimeFormatter.ofPattern("YYYY-MM-DD"));
		Add add = new Add(filename, c.toString());
		bxc.execute(add);
	}

	private static Profile constructProfile(Tag t)
	{
		String            name     = t.child("name").value();
		String            mail     = t.child("owner").value();
		ArrayList<String> keywords = new ArrayList<>();

		for(Tag key : t.child("keywords").children("keyword"))
		{
			keywords.add(key.value());
		}
		ArrayList<String> sources = new ArrayList<>();
		for(Tag src : t.child("sources").children("source"))
		{
			sources.add(src.value());
		}
		LocalTime generationTime = LocalTime.parse(t.child("generationtime").value());

		return new Profile(name, mail, keywords, sources, generationTime);
	}

	private static Article constructArticle(Tag t)
	{
		final String   title       = t.child("title").value();
		final String   link        = t.child("link").value();
		final String   guid        = t.child("guid").value();
		final Calendar pubDate     = parseCalendar(t.child("pubDate").value());
		final String   description = t.child("description").value();
		final Source   source      = new Source(t.child("source").value(), "", "");
		return new Article(title, source, link, description, guid, pubDate);
	}
	private void sendClipping(Clipping c)
	{
		Mail m = new Mail();

		m.To = c.getOwnerMail();

		m.Text = c.prettyPrint();
		m.Subject = c.getProfileName();

		m.send();
	}

	private Clipping generateClipping(Profile p)
	{
		if(p.getKeywords().isEmpty())
		{
			return new Clipping(p);
		}

		String search = concatenate(p.getKeywords());
		String src    = concatenate(p.getSources());
		final String query = "for $article in /article " +
				"where ((title|description) contains text {" + search + "} all) and (source=(" + src + ")) " +
				"return $article";
		BaseXController bxc    = BaseXController.getInstance();
		String          result = bxc.query(query);

		Clipping c = new Clipping(p);

		Tag articles = new Tag("<articles>" + result + "</articles>");
		for(Tag article : articles.children("article"))
		{
			Article a = constructArticle(article);
			System.out.println(a);

			c.addArticle(a);
		}
		return c;
	}

	/**
	 * TODO: DUPLICATE CODE, extract
	 *
	 * @param pubDate date to parse in format "ddd, DD MMM YYY HH:MM:SS zhhmm"
	 *                d = Day in Letters (Thu)
	 *                D = Day in decimals (23)
	 *                M = Month in Letters (JUN)
	 *                Y = Year in decimals (2008)
	 *                H = hour in decimals (20)
	 *                M = minute in decimals (34)
	 *                S = seconds in decimals (20)
	 *                h = offset hours
	 *                m = offset minutes
	 *                z = +|-
	 *                zhhmm = +0100
	 * @return Calendar representing given date
	 */
	private static Calendar parseCalendar(String pubDate) throws NumberFormatException
	{
		pubDate = pubDate.substring(pubDate.indexOf(",") + 2);
		String[] fields     = pubDate.split(" ");
		int      dayInMonth = Integer.parseInt(fields[0]);
		int      month      = parseMonth(fields[1]);
		int      year       = Integer.parseInt(fields[2]);
		String[] time       = fields[3].split(":");
		int      hour       = Integer.parseInt(time[0]);
		int      min        = Integer.parseInt(time[1]);
		int      sec        = Integer.parseInt(time[2]);
		Calendar c          = Calendar.getInstance();
		c.set(year, month, dayInMonth, hour, min, sec);

		if(fields[4].length() == 5)
		{
			int offsetHour   = Integer.parseInt(fields[4].substring(1, 3));
			int offsetMin    = Integer.parseInt(fields[4].substring(3, 5));
			int offsetMillis = (60 * offsetHour + offsetMin) * 60 * 1000;
			if(fields[4].charAt(0) == '+')
			{
				c.set(Calendar.ZONE_OFFSET, offsetMillis);
			}
			else if(fields[4].charAt(0) == '-')
			{
				c.set(Calendar.ZONE_OFFSET, -offsetMillis);
			}
		}
		else if(fields[4].length() == 3)
		{
			c.setTimeZone(TimeZone.getTimeZone(fields[4]));
		}
		return c;
	}

	private static int parseMonth(String month)
	{
		switch(month)
		{
			case "Jan":
				return 0;
			case "Feb":
				return 1;
			case "Mar":
				return 2;
			case "Apr":
				return 3;
			case "May":
				return 4;
			case "Jun":
				return 5;
			case "Jul":
				return 6;
			case "Aug":
				return 7;
			case "Sep":
				return 8;
			case "Oct":
				return 9;
			case "Nov":
				return 10;
			case "Dec":
				return 11;
			default:
				return -1;
		}
	}

	private static String concatenate(List<String> words)
	{
		if(words.isEmpty())
		{
			return null;
		}
		StringBuilder s      = new StringBuilder();
		String        prefix = "";
		for(String k : words)
		{
			s.append(prefix).append("'").append(k).append("'");
			prefix = ",";
		}
		return s.toString();
	}
}
