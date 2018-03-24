package com.metlab.clippingDaemon;

import com.metlab.controller.BaseXController;
import org.basex.core.cmd.Add;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class Main {
	public static void main(String[] args)
	{
		//To debug set break points in ClippingGenerator; The added profile will be called for generation after 12 seconds
		String[] src = {"Stuttgarter Zeitung"};
		String[] key = {"VVS"};
		addTestProfileToBaseX("ede1998@arcor.de", key, src);
		ClippingGenerator cg = new ClippingGenerator();
		new Thread(cg).start();
	}

	private static void addTestProfileToBaseX(String usermail, String[] keywords, String[] sources)
	{
		final String nowMinus   = LocalTime.now().plusSeconds(12).format(DateTimeFormatter.ofPattern("HH-mm-ss"));
		final String nowDefault = LocalTime.now().plusSeconds(12).format(DateTimeFormatter.ofPattern("HH:mm:ss"));

		StringBuilder kStr = new StringBuilder();
		for(String k : keywords)
		{
			kStr.append("<keyword>").append(k).append("</keyword>");
		}
		StringBuilder sStr = new StringBuilder();
		for(String s : sources)
		{
			sStr.append("<source>").append(s).append("</source>");
		}

		final String XMLfile = "<profile><name>debug-profile-" + nowMinus + "</name><owner>" + usermail + "</owner>" +
				"<generationtime>" + nowDefault + "</generationtime>" +
				"<keywords>" + kStr + "</keywords>" +
				"<sources>" + sStr + "</sources></profile>";
		final String XMLpath = "/profiles/debug-profile-" + nowMinus;

		Add             testprofile = new Add(XMLpath, XMLfile);
		BaseXController bxc         = BaseXController.getInstance();
		bxc.execute(testprofile);
	}
}
