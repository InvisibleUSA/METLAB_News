package me.metlabnews.Presentation;

public class ClippingDataRepresentation
{
	public ClippingDataRepresentation(String profileID, String htmlContent, String dateCreated)
	{
		m_profileId = profileID;
		m_htmlContent = htmlContent;
		m_dateCreated = dateCreated;
	}

	public String getprofileId()
	{
		return m_profileId;
	}

	public String getContent()
	{
		return m_htmlContent;
	}

	public String getDate()
	{
		return m_dateCreated;
	}



	private String m_profileId;
	private String m_htmlContent;
	private String m_dateCreated;
}
