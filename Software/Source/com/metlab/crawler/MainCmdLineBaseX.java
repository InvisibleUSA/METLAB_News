package com.metlab.crawler;

import com.metlab.controller.BaseXController;
import org.basex.core.cmd.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class MainCmdLineBaseX
{
	public static void main(String... args)
	{
		BaseXController bxc = BaseXController.getInstance();
		System.out.println(bxc.execute(new List()));
		System.out.println(bxc.execute(new List("ClippingDB")));

		String         input;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		while(true)
		{
			try
			{
				System.out.println("cmd | query | stop ?");
				input = bf.readLine();
				if(input.equals("stop"))
				{
					break;
				}
				else if(input.equals("query"))
				{
					String input2;
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
					String input2;
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
		bxc.stop();
	}
}
