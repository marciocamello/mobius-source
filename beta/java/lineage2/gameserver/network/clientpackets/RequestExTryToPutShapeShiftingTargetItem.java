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

import lineage2.gameserver.data.xml.holder.EnchantItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeTargetType;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeType;
import lineage2.gameserver.network.serverpackets.ExPutShapeShiftingTargetItemResult;
import lineage2.gameserver.network.serverpackets.ExShapeShiftingResult;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;
import lineage2.gameserver.utils.Util;

/**
 * @author kick
 **/
public class RequestExTryToPutShapeShiftingTargetItem extends L2GameClientPacket
{
	private int _targetItemObjId;
	
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		if (player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		
		if ((targetItem == null) || (stone == null))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		if (!targetItem.canBeAppearance())
		{
			player.sendPacket(ExPutShapeShiftingTargetItemResult.FAIL);
			return;
		}
		
		if ((targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getId());
		
		if (appearanceStone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		if (((appearanceStone.getType() != ShapeType.RESTORE) && (targetItem.getVisualId() > 0)) || ((appearanceStone.getType() == ShapeType.RESTORE) && (targetItem.getVisualId() == 0)))
		{
			player.sendPacket(ExPutShapeShiftingTargetItemResult.FAIL);
			return;
		}
		
		if (!targetItem.isOther() && (targetItem.getTemplate().getItemGrade() == Grade.NONE))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_MODIFY_OR_RESTORE_NO_GRADE_ITEMS));
			return;
		}
		
		Grade[] stoneGrades = appearanceStone.getGrades();
		
		if ((stoneGrades != null) && (stoneGrades.length > 0))
		{
			if (!Util.contains(stoneGrades, targetItem.getTemplate().getItemGrade()))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ITEM_GRADES_DO_NOT_MATCH));
				return;
			}
		}
		
		ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
		
		if ((targetTypes == null) || (targetTypes.length == 0))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		if (!Util.contains(targetTypes, ShapeTargetType.ALL))
		{
			if (targetItem.isWeapon())
			{
				if (!Util.contains(targetTypes, ShapeTargetType.WEAPON))
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WEAPONS_ONLY));
					return;
				}
			}
			else if (targetItem.isArmor())
			{
				if (!Util.contains(targetTypes, ShapeTargetType.ARMOR))
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ARMOR_ONLY));
					return;
				}
			}
			else
			{
				if (!Util.contains(targetTypes, ShapeTargetType.ACCESSORY))
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS));
					return;
				}
			}
		}
		
		ExItemType[] itemTypes = appearanceStone.getItemTypes();
		
		if ((itemTypes != null) && (itemTypes.length > 0))
		{
			if (!Util.contains(itemTypes, targetItem.getExItemType()))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS));
				return;
			}
		}
		
		if (targetItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			return;
		}
		
		player.sendPacket(new ExPutShapeShiftingTargetItemResult(ExPutShapeShiftingTargetItemResult.SUCCESS_RESULT, appearanceStone.getCost()));
	}
}