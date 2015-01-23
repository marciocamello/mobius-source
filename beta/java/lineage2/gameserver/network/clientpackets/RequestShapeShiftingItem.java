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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.data.xml.holder.AppearanceStoneHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeTargetType;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeType;
import lineage2.gameserver.network.serverpackets.ExShapeShiftingResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;
import lineage2.gameserver.utils.Util;

/**
 * @author kick
 **/
public class RequestShapeShiftingItem extends L2GameClientPacket
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
			player.setAppearanceExtractItem(null);
			return;
		}
		
		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		
		if ((targetItem == null) || (stone == null))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if (stone.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if (!targetItem.canBeAppearance())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if ((targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		AppearanceStone appearanceStone = AppearanceStoneHolder.getInstance().getAppearanceStone(stone.getId());
		
		if (appearanceStone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if (((appearanceStone.getType() != ShapeType.RESTORE) && (targetItem.getVisualId() > 0)) || ((appearanceStone.getType() == ShapeType.RESTORE) && (targetItem.getVisualId() == 0)))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if (!targetItem.isOther() && (targetItem.getTemplate().getItemGrade() == Grade.NONE))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		Grade[] stoneGrades = appearanceStone.getGrades();
		
		if ((stoneGrades != null) && (stoneGrades.length > 0))
		{
			if (!Util.contains(stoneGrades, targetItem.getTemplate().getItemGrade()))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
		}
		
		ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
		
		if ((targetTypes == null) || (targetTypes.length == 0))
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		if (!Util.contains(targetTypes, ShapeTargetType.ALL))
		{
			if (targetItem.isWeapon())
			{
				if (!Util.contains(targetTypes, ShapeTargetType.WEAPON))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
			else if (targetItem.isArmor())
			{
				if (!Util.contains(targetTypes, ShapeTargetType.ARMOR))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
			else
			{
				if (!Util.contains(targetTypes, ShapeTargetType.ACCESSORY))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
		}
		
		ExItemType[] itemTypes = appearanceStone.getItemTypes();
		
		if ((itemTypes != null) && (itemTypes.length > 0))
		{
			if (!Util.contains(itemTypes, targetItem.getExItemType()))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
		}
		
		ItemInstance extracItem = player.getAppearanceExtractItem();
		int extracItemId = 0;
		
		if ((appearanceStone.getType() != ShapeType.RESTORE) && (appearanceStone.getType() != ShapeType.FIXED))
		{
			if (extracItem == null)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (!extracItem.canBeAppearance())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if ((extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			/*
			 * if(!extracItem.isOther() && extracItem.getGrade() == ItemGrade.NONE) { player.sendPacket(ExShapeShiftingResult.FAIL); player.setAppearanceStone(null); player.setAppearanceExtractItem(null); return; }
			 */
			if (!extracItem.isOther() && (targetItem.getTemplate().getItemGrade().ordinal() <= extracItem.getTemplate().getItemGrade().ordinal()))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (extracItem.getVisualId() > 0)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (targetItem.getExItemType() != extracItem.getExItemType())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (extracItem.getOwnerId() != player.getObjectId())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			extracItemId = extracItem.getId();
		}
		
		if (targetItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}
		
		inventory.writeLock();
		
		try
		{
			long cost = appearanceStone.getCost();
			
			if (cost > player.getAdena())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA));
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (stone.getCount() < 1L)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			
			if (appearanceStone.getType() == ShapeType.NORMAL)
			{
				if (!inventory.destroyItem(extracItem, 1L))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
			
			inventory.destroyItem(stone, 1L);
			player.reduceAdena(cost);
			
			final boolean equipped = targetItem.isEquipped();
			if (equipped)
			{
				inventory.unEquipItem(targetItem);
			}
			
			switch (appearanceStone.getType())
			{
				case RESTORE:
					targetItem.setVisualId(0);
					break;
				
				case NORMAL:
					targetItem.setVisualId(extracItem.getId());
					break;
				
				case BLESSED:
					targetItem.setVisualId(extracItem.getId());
					break;
				
				case FIXED:
					targetItem.setVisualId(appearanceStone.getExtractItemId());
					break;
				
				default:
					break;
			}
			
			targetItem.setJdbcState(JdbcEntityState.UPDATED);
			targetItem.update();
			
			if (equipped)
			{
				inventory.equipItem(targetItem);
			}
			
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION).addLong(cost));
		}
		finally
		{
			inventory.writeUnlock();
		}
		
		// send packets
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(targetItem);
		player.sendPacket(iu);
		player.setAppearanceStone(null);
		player.setAppearanceExtractItem(null);
		player.sendPacket(new ExShapeShiftingResult(ExShapeShiftingResult.SUCCESS_RESULT, targetItem.getId(), extracItemId));
	}
}