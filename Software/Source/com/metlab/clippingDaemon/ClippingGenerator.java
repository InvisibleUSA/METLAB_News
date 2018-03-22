package com.metlab.clippingDaemon;

import com.metlab.controller.BaseXController;
import com.metlab.crawler.Profile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;



public class ClippingGenerator implements Runnable
{

	boolean m_stop = false;
	Queue<Profile> m_profiles; //profiles sorted by time to generate clipping

	public void stopProcessing()
	{
		m_stop = true;
	}

	@Override
	public void run()
	{
		while(!m_stop)
		{
			//XQUERY for next time a profile must be clipped
			//fn:min(for $time in /profile/keywords/keyword where (fn:current-time() < xs:time($time)) return xs:time($time))
			//queue front check
			m_profiles.peek();
			String s = "fn:min(for $time in /profile/keywords/keyword where (fn:current-time() < xs:time($time)) return xs:time($time))";
		}
	}

	public void enqueueNextProfile()
	{
		//fetch profile
		String query = "for $profile in /profile " +
				"where fn:current-time() < xs:time($profile/generationtime) " +
				"order by $profile/generationtime return $profile";

		BaseXController bxc = BaseXController.getInstance();


		String                 doc       = "<profiles>" + bxc.query(query) + "</profiles>";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document               feed;
		try
		{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			feed = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

		//parse
		for(int i = 0; i < feed.getChildNodes().item(0).getChildNodes().getLength(); i++)
		{
			Node profile = feed.getChildNodes().item(0).getChildNodes().item(i);
			if(!profile.hasChildNodes())
			{
				continue;
			}


			String            name;
			ArrayList<String> keywords = new ArrayList<>();
			ArrayList<String> sources  = new ArrayList<>();
			LocalTime         generationTime;


			println(profile.getNodeName());

			ArrayList<Node> aln;

			aln = getChildrenByName(profile, "name");
			print(aln.get(0).getNodeName() + "    ");
			name = aln.get(0).getChildNodes().item(0).getNodeValue();
			println(name);

			aln = getChildrenByName(profile, "generationtime");
			print(aln.get(0).getNodeName() + "    ");
			String tmp = aln.get(0).getChildNodes().item(0).getNodeValue();
			println(tmp);
			generationTime = LocalTime.parse(tmp);

			aln = getChildrenByName(profile, "keywords");
			aln = getChildrenByName(aln.get(0), "keyword");
			println(aln.get(0).getNodeName());
			for(Node n : aln)
			{
				keywords.add(aln.get(0).getChildNodes().item(0).getNodeValue());
			}
			println(keywords.toString());

			aln = getChildrenByName(profile, "sources");
			aln = getChildrenByName(aln.get(0), "source");
			println(aln.get(0).getNodeName());
			for(Node n : aln)
			{
				sources.add(aln.get(0).getChildNodes().item(0).getNodeValue());
			}
			println(sources.toString());

			Profile p = new Profile(name, keywords, sources, generationTime);
			System.out.println(p);
		}
		//enqueue
	}


	private boolean debug_printSteps = false;

	private void print(String s)
	{
		if(debug_printSteps)
		{
			System.out.print(s);
		}
	}

	private void println(String s)
	{
		if(debug_printSteps)
		{
			System.out.println(s);
		}
	}

	private ArrayList<Node> getChildrenByName(Node n, String s)
	{
		ArrayList<Node> res = new ArrayList<>();
		for(int i = 0; i < n.getChildNodes().getLength(); i++)
		{
			if(Objects.equals(n.getChildNodes().item(i).getNodeName(), s))
			{
				res.add(n.getChildNodes().item(i));
			}
		}
		return res;
	}
	private void sendClipping(Clipping c)
	{

	}

	private Clipping generateClipping(Profile p)
	{
		return new Clipping(p);
	}
}
