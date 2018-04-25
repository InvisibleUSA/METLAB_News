package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;



@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
/**
 * Simple class that stores information needed to send an e-mail and can send this e-mail.
 * The class relies on the Apache Commons Simple Mail API.
 * @version 1.0
 * @author Erik
 */
public class Mail
{
	public        String To;
	@SuppressWarnings("WeakerAccess")
	final public  String From       = ConfigurationManager.getInstance().getMailFromAddress();
	final private String password   = ConfigurationManager.getInstance().getMailPassword();
	public        String Subject;
	public        String Text;
	final private String SMTPServer = ConfigurationManager.getInstance().getMailSMTPServer();
	final private int    SMTPPort   = ConfigurationManager.getInstance().getMailSMTPPort();

	/**
	 * Send the mail stored in this instance. Any exception is logged in the mail channel.
	 * The mail is send
	 *
	 * @return true: send was successful  false: an exception occurred
	 */
	public boolean send()
	{
		SimpleEmail email = new SimpleEmail();
		try
		{
			email.setSmtpPort(SMTPPort);
			//email.setDebug(true);
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
}
