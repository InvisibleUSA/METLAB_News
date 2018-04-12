package me.metlabnews.Model.BusinessLogic;



public class ClippingManager
{
	public static ClippingManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ClippingManager();
		}
		return m_instance;
	}


	private ClippingManager()
	{
	}




	private static ClippingManager m_instance = null;
}
