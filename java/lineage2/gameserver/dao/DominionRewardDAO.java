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
import java.util.Collection;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.utils.SqlBatch;

import org.apache.log4j.Logger;
import org.napile.primitive.maps.IntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DominionRewardDAO
{
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO dominion_rewards (id, object_id, static_badges, online_reward, kill_reward) VALUES"")
	 */
	private static final String INSERT_SQL_QUERY = "INSERT INTO dominion_rewards (id, object_id, static_badges, online_reward, kill_reward) VALUES";
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM dominion_rewards WHERE id=?"")
	 */
	private static final String SELECT_SQL_QUERY = "SELECT * FROM dominion_rewards WHERE id=?";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM dominion_rewards WHERE id=? AND object_id=?"")
	 */
	private static final String DELETE_SQL_QUERY = "DELETE FROM dominion_rewards WHERE id=? AND object_id=?";
	/**
	 * Field DELETE_SQL_QUERY2. (value is ""DELETE FROM dominion_rewards WHERE id=?"")
	 */
	private static final String DELETE_SQL_QUERY2 = "DELETE FROM dominion_rewards WHERE id=?";
	/**
	 * Field _log.
	 */
	private static final Logger _log = Logger.getLogger(DominionRewardDAO.class);
	/**
	 * Field _instance.
	 */
	private static final DominionRewardDAO _instance = new DominionRewardDAO();
	
	/**
	 * Method getInstance.
	 * @return DominionRewardDAO
	 */
	public static DominionRewardDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param d Dominion
	 */
	public void select(Dominion d)
	{
		DominionSiegeEvent siegeEvent = d.getSiegeEvent();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, d.getId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int playerObjectId = rset.getInt("object_id");
				int staticBadges = rset.getInt("static_badges");
				int onlineReward = rset.getInt("online_reward");
				int killReward = rset.getInt("kill_reward");
				siegeEvent.setReward(playerObjectId, DominionSiegeEvent.STATIC_BADGES, staticBadges);
				siegeEvent.setReward(playerObjectId, DominionSiegeEvent.KILL_REWARD, killReward);
				siegeEvent.setReward(playerObjectId, DominionSiegeEvent.ONLINE_REWARD, onlineReward);
			}
		}
		catch (Exception e)
		{
			_log.error("DominionRewardDAO:select(Dominion): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method insert.
	 * @param d Dominion
	 */
	public void insert(Dominion d)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY2);
			statement.setInt(1, d.getId());
			statement.execute();
			DominionSiegeEvent siegeEvent = d.getSiegeEvent();
			Collection<IntObjectMap.Entry<int[]>> rewards = siegeEvent.getRewards();
			SqlBatch b = new SqlBatch(INSERT_SQL_QUERY);
			for (IntObjectMap.Entry<int[]> entry : rewards)
			{
				StringBuilder sb = new StringBuilder("(");
				sb.append(d.getId()).append(',');
				sb.append(entry.getKey()).append(',');
				sb.append(entry.getValue()[DominionSiegeEvent.STATIC_BADGES]).append(',');
				sb.append(entry.getValue()[DominionSiegeEvent.ONLINE_REWARD]).append(',');
				sb.append(entry.getValue()[DominionSiegeEvent.KILL_REWARD]).append(')');
				b.write(sb.toString());
			}
			if (!b.isEmpty())
			{
				statement.executeUpdate(b.close());
			}
		}
		catch (final Exception e)
		{
			_log.error("DominionRewardDAO.insert(Dominion):", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param d Dominion
	 * @param objectId int
	 */
	public void delete(Dominion d, int objectId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, d.getId());
			statement.setInt(2, objectId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("DominionRewardDAO:delete(Dominion): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
