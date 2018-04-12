package me.metlabnews.Presentation;



import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.User;



public class UserDataRepresentation
{
	public UserDataRepresentation(String email, String firstName,
	                              String LastName, boolean orgAdmin,
	                              boolean sysAdmin, boolean pending)
	{
		m_email = email;
		m_firstName = firstName;
		m_lastName = LastName;
		m_isOrganisationAdministrator = orgAdmin;
		m_isSystemAdministrator = sysAdmin;
		m_verificationPending = pending;
	}


	public UserDataRepresentation(User user)
	{
		m_email = user.getEmail();
		m_firstName = user.getFirstName();
		m_lastName = user.getLastName();

		if(user.getClass() == Subscriber.class)
		{
			m_isSystemAdministrator = false;
			Subscriber subscriber = (Subscriber)user;
			m_verificationPending = subscriber.isVerificationPending();
			m_isOrganisationAdministrator = subscriber.isOrganisationAdministrator();
		}
		else
		{
			m_isSystemAdministrator = true;
			m_verificationPending = false;
			m_isOrganisationAdministrator = false;
		}
	}


	@Override
	public String toString()
	{
		m_stringBuilder.append("Vorname: ");
		m_stringBuilder.append(m_firstName);
		m_stringBuilder.append("; Nachname: ");
		m_stringBuilder.append(m_lastName);
		m_stringBuilder.append("; Email: ");
		m_stringBuilder.append(m_email);
		m_stringBuilder.append("; Administrator einer Organisation: ");
		m_stringBuilder.append(m_isOrganisationAdministrator ? "Ja" : "Nein");
		m_stringBuilder.append("; Systemadministrator: ");
		m_stringBuilder.append(m_isSystemAdministrator ? "Ja" : "Nein");
		m_stringBuilder.append("; Verifiziert: ");
		m_stringBuilder.append(!m_verificationPending ? "Ja" : "Nein");

		return m_stringBuilder.toString();
	}


	public String getEmail()
	{
		return m_email;
	}

	public String getFirstName()
	{
		return m_firstName;
	}

	public String getLastName()
	{
		return m_lastName;
	}

	public boolean isVerificationPending()
	{
		return m_verificationPending;
	}

	public boolean isOrganisationAdministrator()
	{
		return m_isOrganisationAdministrator;
	}

	public boolean isSystemAdministrator()
	{
		return m_isSystemAdministrator;
	}



	private String m_email;
	private String m_firstName;
	private String m_lastName;
	private boolean m_isOrganisationAdministrator;
	private boolean m_verificationPending;
	private boolean m_isSystemAdministrator;
	StringBuilder m_stringBuilder = new StringBuilder();
}
