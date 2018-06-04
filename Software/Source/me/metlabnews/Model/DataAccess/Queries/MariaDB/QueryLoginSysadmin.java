package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Entities.SystemAdministrator;

import java.sql.*;


/**
 * Created by ln on 12.04.18.
 */
public class QueryLoginSysadmin extends MariaDBQueryBase
{

	public String email;
	public String password;
	public boolean adminLoginSuccessful = false;
	public boolean userExists           = false;
	public SystemAdministrator sysadmin;

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT * FROM SystemAdmins WHERE EMail = ?", email};
	}

	@Override
	protected void processResults(Connection conn, Object[] q)
	{
		String readEmail    = "";
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
				readEmail = rs.getString("EMail");
				readPassword = rs.getString("Password");
			}
		}
		catch(SQLException e)
		{
			return;
		}
		if(readEmail.isEmpty())
		{
			userExists = false;
			adminLoginSuccessful = false;
			return;
		}
		if(!readPassword.equals(password))
		{
			userExists = true;
			adminLoginSuccessful = false;
			return;
		}
		userExists = true;
		adminLoginSuccessful = true;
		sysadmin = new SystemAdministrator(email, password, null, null);
	}
}
