package com.metlab.crawler;

import com.metlab.controller.BaseXController;
import org.basex.core.cmd.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class Main
{
    public static void main(String... args)
    {
        BaseXController bxc = BaseXController.getInstance();

	    String s  = "fn:min(for $time in /profile/keywords/keyword where (fn:current-time() < xs:time($time)) return xs:time($time))";
	    XQuery xq = new XQuery("");
	    //Open   db  = new Open("test");
	    //Add    add = new Add("/test/file.xml", "<test></test>");
	    //System.out.println("Starting execution.");
	    //System.out.println(xq);
	    // System.out.println(bxc.execute(db));
	    //System.out.println(bxc.execute(add));
	    //System.out.println(bxc.execute(xq));

	    Crawler c = new Crawler();
	    new Thread(c).start();

	    System.out.println(bxc.execute(new List()));
	    System.out.println(bxc.execute(new List("ClippingDB")));

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
				    System.out.println("query:");
				    System.out.println(bxc.query(bf.readLine()));
			    }
			    else if(input.equals("cmd"))
			    {
				    System.out.println("cmd:");
				    System.out.println(bxc.execute(bf.readLine()));
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
    }
}
