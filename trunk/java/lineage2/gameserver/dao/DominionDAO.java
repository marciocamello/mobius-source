/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.residence.Dominion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DominionDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(DominionDAO.class);
	/**
	 * Field _instance.
	 */
	private static final DominionDAO _instance = new DominionDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT lord_object_id, wards FROM dominion WHERE id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT lord_object_id, wards FROM dominion WHERE id=?";
	/**
	 * Field UPDATE_SQL_QUERY. (value is ""UPDATE dominion SET lord_object_id=?, wards=? WHERE id=?"")
	 */
	public static final String UPDATE_SQL_QUERY = "UPDATE dominion SET lord_object_id=?, wards=? WHERE id=?";
	
	/**
	 * Method getInstance.
	 * @return DominionDAO
	 */
	public static DominionDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param dominion Dominion
	 */
	public void select(Dominion dominion)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, dominion.getId());
			rset = statement.executeQuery();
			if (rset.next())
			{
				dominion.setLordObjectId(rset.getInt("lord_object_id"));
				String flags = rset.getString("wards");
				if (!flags.isEmpty())
				{
					String[] values = flags.split(";");
					for (String value : values)
					{
						dominion.addFlag(Integer.parseInt(value));
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Dominion.loadData(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method update.
	 * @param residence Dominion
	 */
	public void update(Dominion residence)
	{
		if (!residence.getJdbcState().isUpdatable())
		{
			return;
		}
		residence.setJdbcState(JdbcEntityState.STORED);
		update0(residence);
	}
	
	/**
	 * Method update0.
	 * @param dominion Dominion
	 */
	private void update0(Dominion dominion)
	{
		String wardsString = "";
		int[] flags = dominion.getFlags();
		if (flags.length > 0)
		{
			for (int flag : flags)
			{
				wardsString += flag + ";";
			}
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_SQL_QUERY);
			statement.setInt(1, dominion.getLordObjectId());
			statement.setString(2, wardsString);
			statement.setInt(3, dominion.getId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("DominionDAO#update0(Dominion): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
