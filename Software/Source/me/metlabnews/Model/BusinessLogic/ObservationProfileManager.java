package me.metlabnews.Model.BusinessLogic;



public class ObservationProfileManager
{
	public static ObservationProfileManager getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ObservationProfileManager();
		}
		return m_instance;
	}


	private ObservationProfileManager()
	{
	}




	private static ObservationProfileManager m_instance = null;
}
