package me.metlabnews.Model.Common;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;



@SuppressWarnings("FieldCanBeLocal")
public class Mail
{
	public        String To;
	@SuppressWarnings("WeakerAccess")
	final public  String From       = "metlabnews@gmail.com";
	final private String password   = "metlab54321";
	public        String Subject;
	public        String Text;
	final private String SMTPServer = "smtp.gmail.com";

	public void send()
	{
		SimpleEmail email = new SimpleEmail();
		try
		{
			email.setSmtpPort(465);
			//email.setDebug(true);
			email.setAuthenticator(new DefaultAuthenticator(From, password));
			email.setSSLOnConnect(true);

			email.setHostName(SMTPServer);
			email.addTo(To);
			email.setFrom(From);
			email.setSubject(Subject);
			email.setMsg(Text);
			email.send();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//TODO refactor logging
		//TODO documentation
	}
}
