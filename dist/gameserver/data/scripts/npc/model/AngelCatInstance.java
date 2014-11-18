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
		
		if (command.equals("give_buff"))
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
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			PreparedStatement statement = con.prepareStatement(SELECT_DATA);
			statement.setString(1, player.getAccountName());
			ResultSet rs = statement.executeQuery();
			
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
			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	private void addInfo(Player player)
	{
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement statement = con.prepareStatement(INSERT_DATA);
			statement.setString(1, player.getAccountName());
			statement.setString(2, VAR_DATA);
			statement.setString(3, Long.toString(System.currentTimeMillis()));
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			return;
		}
		
	}
	
	public void deleteInfo(String account)
	{
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement statement = con.prepareStatement(DELETE_DATA);
			statement.setString(1, account);
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			// empty catch clause
		}
		
	}
}