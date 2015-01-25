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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExChangeAttributeItemList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mikhael(Nache)
 */
public class ChangeAttributeCrystal extends ScriptItemHandler
{
	private static final int[] _itemIds =
	{
		33502,
		33833,
		33834,
		33835,
		33836,
		33837
	};
	
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		
		Player player = (Player) playable;
		if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_CHANGE_AN_ATTRIBUTE_WHILE_USING_A_PRIVATE_STORE_OR_WORKSHOP));
			return false;
		}
		
		switch (item.getId())
		{
			case 33502:
			case 33833:
				sendAttributeItemList(item.getId(), player, Grade.S, Grade.S);
				break;
			case 33834:
				sendAttributeItemList(item.getId(), player, Grade.S80);
				break;
			case 33835:
				sendAttributeItemList(item.getId(), player, Grade.R);
				break;
			case 33836:
				sendAttributeItemList(item.getId(), player, Grade.R95);
				break;
			case 33837:
				sendAttributeItemList(item.getId(), player, Grade.R99);
				break;
		}
		return false;
	}
	
	private boolean sendAttributeItemList(int itemId, Player player, ItemTemplate.Grade... grades)
	{
		List<ItemInfo> itemsList = new ArrayList<>();
		ItemInstance[] items = player.getInventory().getItems();
		for (ItemInstance item : items)
		{
			if (item.isWeapon() && (item.getAttackElementValue() > 0))
			{
				if (ArrayUtils.contains(grades, item.getTemplate().getItemGrade()))
				{
					itemsList.add(new ItemInfo(item));
				}
			}
		}
		if (itemsList.size() == 0)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ITEM_FOR_CHANGING_AN_ATTRIBUTE_DOES_NOT_EXIST));
			return false;
		}
		
		player.sendPacket(new ExChangeAttributeItemList(itemId, itemsList.toArray(new ItemInfo[itemsList.size()])));
		return true;
	}
	
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
	
}