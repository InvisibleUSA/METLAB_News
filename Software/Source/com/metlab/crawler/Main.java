package com.metlab.crawler;

import com.metlab.clippingDaemon.ClippingGenerator;
import com.metlab.controller.BaseXController;
import org.basex.core.cmd.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;



public class Main
{
    public static void main(String... args)
    {
	    Add add = new Add("test/file", "<profile>\n" +
			    "    <name>{name}</name>\n" +
			    "    <uid>{UNIQUE-ID}</uid>\n" +
			    "    <generationtime>23:59:23</generationtime>\n" +
			    "    <keywords>\n" +
			    "        <keyword>{Suchbegriff}</keyword>\n" +
			    "    </keywords>\n" +
			    "<sources>\n" +
			    "        <source>{source-name}</source>\n" +
			    "</sources>\n" +
			    "</profile>");
	    Add add1 = new Add("test/file1", "<profile>\n" +
			    "    <name>{name1}</name>\n" +
			    "    <uid>{UNIQUE-ID123}</uid>\n" +
			    "    <generationtime>12:23:23</generationtime>\n" +
			    "    <keywords>\n" +
			    "        <keyword>{Suchbegriff1}</keyword>\n" +
			    "    </keywords>\n" +
			    "<sources>\n" +
			    "        <source>{source-name}</source>\n" +
			    "</sources>\n" +
			    "</profile>");
	    Add add2 = new Add("test/file1", "<profile>\n" +
			    "    <name>{name2}</name>\n" +
			    "    <uid>{UNIQUE-ID13}</uid>\n" +
			    "    <generationtime>13:43:58</generationtime>\n" +
			    "    <keywords>\n" +
			    "        <keyword>{Suchbegriff2}</keyword>\n" +
			    "    </keywords>\n" +
			    "<sources>\n" +
			    "        <source>{source-name}</source>\n" +
			    "</sources>\n" +
			    "</profile>");

        BaseXController bxc = BaseXController.getInstance();

	    Crawler c = new Crawler();
	    c.setDebug(false);
	    new Thread(c).start();

	    System.out.println(bxc.execute(add));
	    System.out.println(bxc.execute(add1));
	    System.out.println(bxc.execute(add2));
	    //System.out.println(bxc.execute(new List()));
	    System.out.println(bxc.execute(new List("ClippingDB")));

	    ClippingGenerator cg = new ClippingGenerator();
	    cg.enqueueNextProfile();

	    String         input;
	    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

	    while(true)
	    {
		    try
		    {
			    System.out.println("cmd | query?");
			    input = bf.readLine();
			    if(input.equals("stop"))
			    {
				    break;
			    }
			    else if(input.equals("query"))
			    {
				    String input2 = "";
				    while(true)
				    {
					    System.out.println("enter query:");
					    input2 = bf.readLine();
					    if(input2.equals("stop"))
					    {
						    break;
					    }
					    System.out.println(bxc.query(input2));
				    }
			    }
			    else if(input.equals("cmd"))
			    {
				    String input2 = "";
				    while(true)
				    {
					    System.out.println("enter Command:");
					    input2 = bf.readLine();
					    if(input2.equals("stop"))
					    {
						    break;
					    }
					    System.out.println(bxc.execute(input2));
				    }
			    }
		    }
		    catch(IOException e)
		    {
			    e.printStackTrace();
		    }
	    }
	    try
	    {
		    bf.close();
	    }
	    catch(IOException e)
	    {
		    e.printStackTrace();
	    }
	    c.stop();
	    bxc.stop();
	    exit(0);
    }
}
