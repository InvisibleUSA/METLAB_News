package me.metlabnews.Model.Common.Mail;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;



/**
 * <p>This class sends Email in MIME Type. You can send Email in HTML with it.
 * The HTML-File is stored in the 'Resources' folder of the project.</p>
 * <p>Example:</p>
 * <p>
 * {@code}
 * <p>MailDeliverer m = new MailDeliverer();</p>
 * <p>m.send("test@test.de, "Subject Title");</p>
 * </p>
 *
 * @author Tobias
 * @version 1.0
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class MailDeliverer
{
	static
	{
		Logger.getInstance().register(MailDeliverer.class, Logger.Channel.Mail);
	}


	@SuppressWarnings("WeakerAccess")
	final private Logger log        = Logger.getInstance();
	final public  String From       = ConfigurationManager.getInstance().getMailFromAddress();
	final private String Password   = ConfigurationManager.getInstance().getMailPassword();
	final private String SMTPServer = ConfigurationManager.getInstance().getMailSMTPServer();
	final private int    SMTPPort   = ConfigurationManager.getInstance().getMailSMTPPort();


	/**
	 * This method returns the properties for the Email
	 *
	 * @return the properties of the Email
	 */
	private Properties getProperties()
	{
		try
		{
			return new Properties()
			{{
				put("mail.smtp.auth", true);
				put("mail.smtp.starttls.enable", true);
				put("mail.smtp.host", SMTPServer);
				put("mail.smtp.port", SMTPPort);
			}};
		}
		catch(Exception e)
		{
			log.logError(this, "Exception in getProperties: " + e.toString());
			return null;
		}
	}


	/**
	 * This method returns the Session for the Email
	 *
	 * @return the session Object of the Email
	 */
	private Session getSession()
	{
		Properties prop;

		if((prop = getProperties()) != null)
		{
			return Session.getInstance(prop, new Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(From, Password);
				}
			});
		}
		return null;
	}


	/**
	 * This Method reads the HTML File in and returns it.
	 *
	 * @return The HTML-File for sending an Email
	 */
	public String getHTMLContent()
	{
		StringBuilder stringBuilder = new StringBuilder();
		String        sep           = File.separator;
		String HTMLPath = System.getProperty(
				"user.dir") + sep + "Software" + sep + "Resources" + sep + "HTMLClippingMail.html";

		try(BufferedReader reader = new BufferedReader(new FileReader(HTMLPath)))
		{
			String str;
			while((str = reader.readLine()) != null)
			{
				stringBuilder.append(str);
			}
		}
		catch(IOException e)
		{
			log.logError(this, "Error reading HTML-File: " + e.toString());
		}
		return stringBuilder.toString();
	}


	/**
	 * Send the mail stored in this instance. Any exception is logged in the mail channel.
	 * The mail is send
	 *
	 * @param to      The receiver of this HTML E-Mail
	 * @param subject Subject
	 * @return true: send was successful  false: an exception occurred
	 */
	public boolean send(String to, String subject, String message)
	{
		if(to != null)
		{
			try
			{
				Session session;
				if((session = getSession()) != null)
				{
					Transport.send(
							new MimeMessage(session)
							{{
								setFrom(new InternetAddress(From));
								setSubject(subject);
								setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
								setContent(message, "text/html; charset=utf-8");
							}});
					log.logActivity(this, "E-Mail successfully sent to: " + to);
					return true;
				}
				else
				{
					log.logError(this, "FATAL Error in sending E-Mail with Session. Session is null");
					throw new MessagingException();
				}
			}
			catch(MessagingException e)
			{
				log.logError(this, "Error sending SimpleMail: " + e.toString());
			}
		}
		return false;
	}
}