package me.metlabnews.Model.DataAccess.Queries.MariaDB;


import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;

import java.sql.*;


public class QueryLoginUser extends MariaDBQueryBase
{

	public String email;
	public String password;
	public boolean userLoginSuccessful = false;
	public boolean userExists          = false;
	public boolean userVerified        = false;
	public boolean isDeactivated       = false;
	public Subscriber subscriber;
	public boolean byPassword    = false;
	public boolean checkPassword = true;

	@Override
	protected Object[] createSQLQuery()
	{
		if(byPassword)
		{
			return new String[] {"SELECT * FROM Abonnent WHERE PW = ?", password};
		}
		return new String[] {"SELECT * FROM Abonnent WHERE EMail = ?", email};
	}

	@Override
	protected void processResults(Connection conn, Object[] q)
	{
		String email        = "";
		String readPassword = "";
		try
		{
            PreparedStatement ps = conn.prepareStatement((String)q[0]);
            for(int i = 1; i < q.length; i++)
            {
                if(q[i] instanceof String)
                {
                    ps.setString(i, (String)q[i]);
                }
                else if(q[i] instanceof Integer)
                {
                    ps.setInt(i, (int)q[i]);
                }
                else if(q[i] instanceof Date)
                {
                    ps.setDate(i, (Date)q[i]);
                }
            }
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				email = rs.getString("EMail");
				readPassword = rs.getString("PW");
				m_name = rs.getString("Name");
				m_vName = rs.getString("VName");
				m_organisation = rs.getString("Firma");
				isAdmin = rs.getString("isAdmin");
				userVerified = rs.getString("isVerified").equals("1");
				isDeactivated = !(rs.getString("deactivatedSince") == null);
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(email.isEmpty())
		{
			userExists = false;
			userLoginSuccessful = false;
			return;
		}
		if(!readPassword.equals(password) && checkPassword)
		{
			userExists = true;
			userLoginSuccessful = false;
			return;
		}
		if(!userVerified)
		{
			userExists = true;
			userLoginSuccessful = false;
			return;
		}
		if(isDeactivated)
		{
			userExists = true;
			userLoginSuccessful = false;
			return;
		}
		subscriber = new Subscriber(email, readPassword, m_vName, m_name, new Organisation(m_organisation),
		                            isAdmin.equals("1"));
		userExists = true;
		userLoginSuccessful = true;
	}

	private String m_name;
	private String m_vName;
	private String m_organisation;
	private String isAdmin;
}
