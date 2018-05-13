package me.metlabnews.Presentation;

public class ClippingDataRepresentation
{
	public ClippingDataRepresentation(String htmlContent, String dateCreated)
	{
		m_htmlContent = htmlContent;
		m_dateCreated = dateCreated;
	}

	public String getContent()
	{
		return m_htmlContent;
	}

	public String getDate()
	{
		return m_dateCreated;
	}

	private String m_htmlContent;
	private String m_dateCreated;
}
