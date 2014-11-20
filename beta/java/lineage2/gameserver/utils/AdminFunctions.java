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
package lineage2.gameserver.utils;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.components.ChatType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AdminFunctions
{
	public final static Location JAIL_SPAWN = new Location(-114648, -249384, -2984);
	
	/**
	 * Constructor for AdminFunctions.
	 */
	private AdminFunctions()
	{
	}
	
	/**
	 * Method kick.
	 * @param player String
	 * @param reason String
	 * @return true, if successful
	 */
	public static boolean kick(String player, String reason)
	{
		Player plyr = World.getPlayer(player);
		
		if (plyr == null)
		{
			return false;
		}
		
		return kick(plyr, reason);
	}
	
	/**
	 * Method kick.
	 * @param player Player
	 * @param reason String
	 * @return true, if successful
	 */
	private static boolean kick(Player player, String reason)
	{
		if (Config.ALLOW_CURSED_WEAPONS && Config.DROP_CURSED_WEAPONS_ON_KICK)
		{
			if (player.isCursedWeaponEquipped())
			{
				player.setPvpFlag(0);
				CursedWeaponsManager.getInstance().dropPlayer(player);
			}
		}
		
		player.kick();
		return true;
	}
	
	/**
	 * Method give.
	 * @param charName String
	 * @param itemid integer
	 * @param amount integer
	 * @param reason String
	 * @return
	 */
	public static boolean give(String charName, int itemid, int amount, String reason)
	{
		Player player = World.getPlayer(charName);
		
		if (player != null)
		{
			if (player.getInventory().addItem(itemid, amount) != null)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method banChat.
	 * @param adminChar Player
	 * @param adminName String
	 * @param charName String
	 * @param val integer
	 * @param reason String
	 * @return String
	 */
	public static String banChat(Player adminChar, String adminName, String charName, int val, String reason)
	{
		Player player = World.getPlayer(charName);
		
		if (player != null)
		{
			charName = player.getName();
		}
		else if (CharacterDAO.getInstance().getObjectIdByName(charName) == 0)
		{
			return "Player " + charName + " not found.";
		}
		
		if (((adminName == null) || adminName.isEmpty()) && (adminChar != null))
		{
			adminName = adminChar.getName();
		}
		
		if ((reason == null) || reason.isEmpty())
		{
			reason = "Unknown";
		}
		
		String result, announce = null;
		
		if (val == 0)
		{
			if ((adminChar != null) && !adminChar.getPlayerAccess().CanUnBanChat)
			{
				return "You have no right to withdraw the ban chat.";
			}
			
			if (Config.BANCHAT_ANNOUNCE)
			{
				announce = Config.BANCHAT_ANNOUNCE_NICK && (adminName != null) && !adminName.isEmpty() ? adminName + " lifted ban chat Player " + charName + "." : "With Player " + charName + " Remove ban chat.";
			}
			
			Log.add(adminName + " lifted ban chat Player " + charName + ".", "banchat", adminChar);
			result = "You removed the ban chat Player " + charName + ".";
		}
		else if (val < 0)
		{
			if ((adminChar != null) && (adminChar.getPlayerAccess().BanChatMaxValue > 0))
			{
				return "You can ban for no more than " + adminChar.getPlayerAccess().BanChatMaxValue + " minute.";
			}
			
			if (Config.BANCHAT_ANNOUNCE)
			{
				announce = Config.BANCHAT_ANNOUNCE_NICK && (adminName != null) && !adminName.isEmpty() ? adminName + " Chat ban player " + charName + " for an indefinite period, the reason: " + reason + "." : "Banned Chat Player " + charName + " for an indefinite period, the reason: " + reason + ".";
			}
			
			Log.add(adminName + " Chat banned Player " + charName + " for an indefinite period, the reason: " + reason + ".", "banchat", adminChar);
			result = "You are banned from chat Player " + charName + " for an indefinite period.";
		}
		else
		{
			if ((adminChar != null) && !adminChar.getPlayerAccess().CanUnBanChat && ((player == null) || (player.getNoChannel() != 0)))
			{
				return "You may not change the ban time.";
			}
			
			if ((adminChar != null) && (adminChar.getPlayerAccess().BanChatMaxValue != -1) && (val > adminChar.getPlayerAccess().BanChatMaxValue))
			{
				return "You can ban for no more than " + adminChar.getPlayerAccess().BanChatMaxValue + " minute.";
			}
			
			if (Config.BANCHAT_ANNOUNCE)
			{
				announce = Config.BANCHAT_ANNOUNCE_NICK && (adminName != null) && !adminName.isEmpty() ? adminName + " Chat banned Player " + charName + " on " + val + " minute, cause: " + reason + "." : "Banned Chat Player " + charName + " on " + val + " minute, reasons: " + reason + ".";
			}
			
			Log.add(adminName + " Chat banned Player " + charName + " on " + val + " minute, reasons: " + reason + ".", "banchat", adminChar);
			result = "You are banned from chat Player " + charName + " on " + val + " minute.";
		}
		
		if (player != null)
		{
			updateNoChannel(player, val, reason);
		}
		else
		{
			AutoBan.ChatBan(charName, val, reason, adminName);
		}
		
		if (announce != null)
		{
			if (Config.BANCHAT_ANNOUNCE_FOR_ALL_WORLD)
			{
				Announcements.getInstance().announceToAll(announce);
			}
			else
			{
				Announcements.shout(adminChar, announce, ChatType.CRITICAL_ANNOUNCE);
			}
		}
		
		return result;
	}
	
	/**
	 * Method updateNoChannel.
	 * @param player Player
	 * @param time int
	 * @param reason String
	 */
	private static void updateNoChannel(Player player, int time, String reason)
	{
		player.updateNoChannel(time * 60000);
		
		if (time == 0)
		{
			player.sendMessage("You are unbanned for all chats.");
		}
		else if (time > 0)
		{
			if ((reason == null) || reason.isEmpty())
			{
				player.sendMessage("You are banned in all chats, time remained " + time + " min.");
			}
			else
			{
				player.sendMessage("You are banned in all chats, reason: " + reason + ", time remained " + time + " min.");
			}
		}
		else if ((reason == null) || reason.isEmpty())
		{
			player.sendMessage("You are banned in all chats permanently.");
		}
		else
		{
			player.sendMessage("You are banned in all chats permanently, reason: " + reason + ".");
		}
	}
}
