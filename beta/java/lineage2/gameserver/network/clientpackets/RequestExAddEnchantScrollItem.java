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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.EnchantScrollInfo;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.network.serverpackets.ExPutEnchantScrollItemResult;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.Log;

public class RequestExAddEnchantScrollItem extends L2GameClientPacket
{
	private int _itemObjectId;
	
	public RequestExAddEnchantScrollItem()
	{
	}
	
	@Override
	protected void readImpl()
	{
		_itemObjectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if ((player.isActionsDisabled()) || (player.isInStoreMode()) || (player.isInTrade()))
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}
		
		PcInventory inventory = player.getInventory();
		ItemInstance itemToEnchant = inventory.getItemByObjectId(_itemObjectId);
		ItemInstance scroll = player.getEnchantScroll();
		
		if ((itemToEnchant == null) || (scroll == null))
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}
		
		Log.add(player.getName() + "|Trying to put enchant|" + itemToEnchant.getId() + "|+" + itemToEnchant.getEnchantLevel() + "|" + itemToEnchant.getObjectId(), "enchants");
		
		EnchantScrollInfo enchantScroll = EnchantScrollManager.getScrollInfo(scroll.getId());
		
		if ((!itemToEnchant.canBeEnchanted()) || (itemToEnchant.isStackable()))
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL));
			player.sendActionFailed();
			return;
		}
		
		if ((itemToEnchant.getLocation() != ItemLocation.INVENTORY) && (itemToEnchant.getLocation() != ItemLocation.PAPERDOLL))
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS));
			return;
		}
		
		if (player.isInStoreMode())
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP));
			return;
		}
		
		if ((scroll = inventory.getItemByObjectId(scroll.getObjectId())) == null)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}
		
		if (enchantScroll == null)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}
		if (itemToEnchant.getTemplate().getItemGrade().externalOrdinal != enchantScroll.getGrade().externalOrdinal)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL));
			player.sendActionFailed();
			return;
		}
		
		int itemType = itemToEnchant.getTemplate().getType2();
		switch (itemType)
		{
			case 1:
				if (itemType == 0)
				{
					player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL));
					player.sendActionFailed();
					return;
				}
				
				break;
			case 2:
				if ((itemType == 1) || (itemType == 2))
				{
					player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL));
					player.sendActionFailed();
					return;
				}
				
				break;
		}
		if ((itemToEnchant.getEnchantLevel() >= enchantScroll.getMax()) || (itemToEnchant.getEnchantLevel() < enchantScroll.getMin()))
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS));
			return;
		}
		
		if (itemToEnchant.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}
		
		player.sendPacket(ExPutEnchantScrollItemResult.SUCCESS);
		player.setEnchantScroll(scroll);
	}
}
