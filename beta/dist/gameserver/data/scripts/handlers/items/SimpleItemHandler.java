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
package handlers.items;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
abstract class SimpleItemHandler extends ScriptItemHandler
{
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 * @see lineage2.gameserver.model.interfaces.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		Player player;
		
		if (playable.isPlayer())
		{
			player = (Player) playable;
		}
		else if (playable.isPet())
		{
			player = playable.getPlayer();
		}
		else
		{
			return false;
		}
		
		if (player.isInFlyingTransform())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getId()));
			return false;
		}
		
		return useItemImpl(player, item, ctrl);
	}
	
	/**
	 * Method useItemImpl.
	 * @param player Player
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	protected abstract boolean useItemImpl(Player player, ItemInstance item, boolean ctrl);
	
	/**
	 * Method useItem.
	 * @param player Player
	 * @param item ItemInstance
	 * @param count long
	 * @return boolean
	 */
	public static boolean useItem(Player player, ItemInstance item, long count)
	{
		if (player.getInventory().destroyItem(item, count))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_USE_S1).addItemName(item.getId()));
			return true;
		}
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INCORRECT_ITEM_COUNT));
		return false;
	}
}
