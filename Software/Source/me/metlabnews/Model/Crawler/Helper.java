package me.metlabnews.Model.Crawler;

import me.metlabnews.Model.Common.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;



public class Helper
{
	/**
	 * @param url the url from which to get an document
	 * @return the extracted document as a String
	 * @throws IOException if connection to url not possible
	 */
	public static String getHTTPResponse(String url) throws IOException
	{
		URL            doc_url = new URL(url);
		InputStream    is      = doc_url.openStream();
		BufferedReader rd      = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder  sb      = new StringBuilder();
		int            cp;
		while((cp = rd.read()) != -1)
		{
			sb.append((char)cp);
		}
		return sb.toString();
	}

	public static String formatForFileName(String title)
	{
		String result = title
				.replace("/", "")
				.replace("<", "")
				.replace(">", "")
				.replace(":", "")
				.replace("\"", "")
				.replace("\\", "")
				.replace("|", "")
				.replace("?", "")
				.replace("*", "")
				.replace(".", "")
				.replace(" ", "");
		if(result.length() > 200)
		{
			result = result.substring(0, 200);
		}
		return result;
	}

	public static String getHTTPResponse(String address, String user, String pw) throws IOException
	{
		Logger.getInstance().logDebug(Helper.class, "url: " + address);
		URL               url        = new URL(address);
		String            encoding   = Base64.getEncoder().encodeToString(
				(user + ":" + pw).getBytes(Charset.forName("UTF-8")));
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		InputStream    content = connection.getInputStream();
		BufferedReader in      = new BufferedReader(new InputStreamReader(content));
		StringBuilder  sb      = new StringBuilder();
		String         line;
		while((line = in.readLine()) != null)
		{
			sb.append(line);
		}
		return sb.toString();
	}
}
