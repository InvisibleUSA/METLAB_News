package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;



@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
/**
 * Simple class that stores information needed to send an e-mail and can send this e-mail.
 * The class relies on the Apache Commons Simple SimpleMail API.
 * @version 1.0
 * @author Erik
 */
public class SimpleMail
{
	static
	{
		Logger.getInstance().register(SimpleMail.class, Logger.Channel.Mail);
	}

	@SuppressWarnings("WeakerAccess")
	public        String To         = null;
	public        String Subject    = null;
	public        String Text       = null;
	final public  String From       = ConfigurationManager.getInstance().getMailFromAddress();
	final private String password   = ConfigurationManager.getInstance().getMailPassword();
	final private String SMTPServer = ConfigurationManager.getInstance().getMailSMTPServer();
	final private int    SMTPPort   = ConfigurationManager.getInstance().getMailSMTPPort();

	/**
	 * Send the mail stored in this instance. Any exception is logged in the mail channel.
	 * The mail is send
	 *
	 * @return true: sending was successful  false: an exception occurred
	 */
	public boolean send()
	{
		SimpleEmail email = new SimpleEmail();
		try
		{
			email.setSmtpPort(SMTPPort);
			email.setAuthenticator(new DefaultAuthenticator(From, password));
			email.setSSLOnConnect(true);

			email.setHostName(SMTPServer);
			email.addTo(To);
			email.setFrom(From);
			email.setSubject(Subject);
			email.setMsg(Text);
			email.send();
			return true;
		}
		catch(Exception e)
		{
			Logger.getInstance().logError(this, e.toString());
			return false;
		}
	}

	public boolean sendHTML()
	{
		HtmlEmail email = new HtmlEmail();
		try
		{
			email.setSmtpPort(SMTPPort);
			email.setAuthenticator(new DefaultAuthenticator(From, password));
			email.setSSLOnConnect(true);


			email.setHostName(SMTPServer);
			email.addTo(To);
			email.setFrom(From);
			email.setSubject(Subject);
			email.setHtmlMsg(Text);
			email.send();
			return true;
		}
		catch(Exception e)
		{
			Logger.getInstance().logError(this, e.toString());
			return false;
		}
	}


	/**
	 * Overload method of send(). Send the mail stored in this instance.
	 * Any exception is logged in the mail channel.
	 * The mail is send
	 *
	 * @param to      The receiver of this E-SimpleMail
	 * @param subject Subject
	 * @param text    The E-SimpleMail Text
	 * @return true: send was successful  false: an exception occurred
	 */
	public boolean send(String to, String subject, String text)
	{
		if(to != null && subject != null && text != null)
		{
			To = to;
			Subject = subject;
			Text = text;
			return send();
		}
		return false;
	}
}
