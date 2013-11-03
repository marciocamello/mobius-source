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
package npc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author cruel
 */
public final class AngelCatInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VAR_DATA = "angelCat_buff";
	private static final String SELECT_DATA = "SELECT var, value FROM account_gsdata WHERE account_name=? AND var LIKE 'angelCat_buff'";
	private static final String INSERT_DATA = "INSERT INTO account_gsdata VALUES (?, ?, ?)";
	private static final String DELETE_DATA = "DELETE FROM account_gsdata WHERE account_name=?";
	
	public AngelCatInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (command.equalsIgnoreCase("give_buff"))
		{
			if (loadInfo(player))
			{
				addInfo(player);
				ItemFunctions.addItem(player, 35669, 1, true);
			}
			else
			{
				player.sendPacket(SystemMsg.THIS_ACCOUNT_HAS_ALREADY_RECEIVED_A_GIFT);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	private boolean loadInfo(Player player)
	{
		String value = null;
		
		ResultSet rs = null;
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(SELECT_DATA);)
		{
			statement.setString(1, player.getAccountName());
			rs = statement.executeQuery();
			
			while (rs.next())
			{
				value = rs.getString("value");
				long l = (System.currentTimeMillis() - Long.parseLong(value)) / 1000;
				if (l < 86400)
				{
					return false;
				}
				deleteInfo(player.getAccountName());
			}
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			// DbUtils.closeQuietly(rs); //FIXME: creating error while compiling the scripts on server start (see below).
			/**
			 * ERROR server\gameserver\data\scripts\npc\model\AngelCatInstance.java:0,0: Internal compiler error: java.lang.ArrayIndexOutOfBoundsException: 4 at org.eclipse.jdt.internal.compiler.codegen.ExceptionLabel.placeEnd(ExceptionLabel.java:41)
			 */
		}
		return true;
	}
	
	private void addInfo(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_DATA);
			statement.setString(1, player.getAccountName());
			statement.setString(2, VAR_DATA);
			statement.setString(3, Long.toString(System.currentTimeMillis()));
			statement.execute();
		}
		catch (Exception e)
		{
			return;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public void deleteInfo(String account)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_DATA);
			statement.setString(1, account);
			statement.execute();
		}
		catch (Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}