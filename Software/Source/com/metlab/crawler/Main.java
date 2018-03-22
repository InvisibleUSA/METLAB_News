package com.metlab.crawler;

import com.metlab.controller.BaseXController;
import org.basex.core.cmd.XQuery;



public class Main
{
    public static void main(String... args)
    {
        BaseXController bxc = BaseXController.getInstance();

	    String s  = "fn:min(for $time in /profile/keywords/keyword where (fn:current-time() < xs:time($time)) return xs:time($time))";
	    XQuery xq = new XQuery(s);
	    System.out.println("Starting execution.");
        System.out.println(bxc.execute(xq));

	    //Crawler c = new Crawler();
	    // new Thread(c).start();

	    BaseXController.getInstance().stop();
    }
}
