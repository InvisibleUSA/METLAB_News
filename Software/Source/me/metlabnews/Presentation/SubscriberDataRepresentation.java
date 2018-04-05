package me.metlabnews.Presentation;



public class SubscriberDataRepresentation
{
	public SubscriberDataRepresentation(String email, String firstName,
	                                    String LastName, boolean admin,
	                                    boolean pending)
	{
		m_email = email;
		m_firstName = firstName;
		m_lastName = LastName;
		m_isOrganisationAdministrator = admin;
		m_verificationPending = pending;
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
		m_stringBuilder.append("; Admin: ");
		m_stringBuilder.append(m_isOrganisationAdministrator ? "Ja" : "Nein");
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



	private String m_email;
	private String m_firstName;
	private String m_lastName;
	private boolean m_isOrganisationAdministrator;
	private boolean m_verificationPending;
	StringBuilder m_stringBuilder = new StringBuilder();
}
