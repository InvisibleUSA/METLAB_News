package com.metlab.crawler;

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
        BaseXController bxc = BaseXController.getInstance();

	    Crawler c = new Crawler();
	    c.setDebug(false);
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
