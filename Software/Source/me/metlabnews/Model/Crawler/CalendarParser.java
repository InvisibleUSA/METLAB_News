package me.metlabnews.Model.Crawler;

import java.util.Calendar;
import java.util.TimeZone;



public class CalendarParser
{
	/**
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
	public static Calendar parseCalendar(String pubDate) throws NumberFormatException
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
}
