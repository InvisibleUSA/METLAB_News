package me.metlabnews.Model.DataAccess.Queries.MariaDB;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.Entities.NewsSource;

import java.sql.*;


/**
 * Returns the number of articles saved in BaseX + 1
 * so basically the number for the next article
 *
 * @author Benjamin Gerlach
 */
public class QueryGetSourceArticleCounter extends MariaDBQueryBase {

	private int        m_numArticles;
	public NewsSource source;

	@Override
	protected Object[] createSQLQuery() {
		return new String[] {"SELECT rss_articlecounter FROM Quellen WHERE Name = ?", source.getName()};
	}

	@Override
	protected void processResults(Connection conn, Object[] q) {
		try {
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
			if(rs.next()) {
				m_numArticles = rs.getInt("rss_articlecounter");
			}
			rs.close();
		}
		catch(SQLException e) {
			Logger.getInstance().logError(this, "SQLException: " + e.getMessage() + ":" + e.getCause());
		}
	}

	public int getNumArticles() {
		return m_numArticles;
	}
}
