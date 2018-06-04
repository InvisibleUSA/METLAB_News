package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import java.sql.*;

import me.metlabnews.Model.Common.Logger;


public class QueryGetSourceLink extends MariaDBQueryBase
{

	private String m_sourceLink;
	private String m_sourceName;

	static
	{
		Logger.getInstance().register(QueryGetSourceLink.class, Logger.Channel.RDBMS);
	}

	public QueryGetSourceLink(String sourceName)
	{
		m_sourceName = sourceName;
	}

	public void setSourceName(String sourceName)
	{
		m_sourceName = sourceName;
	}

	@Override
	protected Object[] createSQLQuery()
	{
		return new String[] {"SELECT Link FROM Quellen WHERE Name='" + m_sourceName + "'"};
	}

	@Override
	protected void processResults(Connection conn, Object[] q)
	{
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
			if(rs.next())
			{
				m_sourceLink = rs.getString("Link");
			}
		}
		catch(SQLException e)
		{
			Logger.getInstance().logError(this, "SQL Exception in GetSourceLink");
		}
	}

	public String getSourceLink()
	{
		return m_sourceLink;
	}
}
