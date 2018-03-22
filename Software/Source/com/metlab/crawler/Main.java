package com.metlab.crawler;

import com.metlab.controller.BaseXServerController;



public class Main
{
    public static void main(String... args)
    {
        new BaseXServerController();
        Crawler c = new Crawler();
        new Thread(c).start();
    }
}
