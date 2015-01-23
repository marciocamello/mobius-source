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
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestDestroyItem extends L2GameClientPacket
{
	private int _objectId;
	private long _count;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM));
			return;
		}
		
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING));
			return;
		}
		
		long count = _count;
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (count < 1)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_DESTROY_IT_BECAUSE_THE_NUMBER_IS_INCORRECT));
			return;
		}
		
		if (!activeChar.isGM() && item.isHeroWeapon())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.HERO_WEAPONS_CANNOT_BE_DESTROYED));
			return;
		}
		
		PetInstance pet = activeChar.getSummonList().getPet();
		
		if ((pet != null) && (pet.getControlItemObjId() == item.getObjectId()))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AS_YOUR_PET_IS_CURRENTLY_OUT_ITS_SUMMONING_ITEM_CANNOT_BE_DESTROYED));
			return;
		}
		
		if (!activeChar.isGM() && !item.canBeDestroyed(activeChar))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_ITEM_CANNOT_BE_DESTROYED));
			return;
		}
		
		if (_count > item.getCount())
		{
			count = item.getCount();
		}
		
		boolean crystallize = item.canBeCrystallized(activeChar);
		int crystalId = item.getTemplate().getCrystalType().cry;
		int crystalAmount = item.getTemplate().getCrystalCount();
		
		if (crystallize)
		{
			int level = activeChar.getSkillLevel(Skill.SKILL_CRYSTALLIZE);
			
			if ((level < 1) || (((crystalId - ItemTemplate.CRYSTAL_D) + 1) > level))
			{
				crystallize = false;
			}
		}
		
		Log.LogItem(activeChar, Log.Delete, item, count);
		
		if (!activeChar.getInventory().destroyItemByObjectId(_objectId, count))
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (PetDataTable.isPetControlItem(item))
		{
			PetDataTable.deletePet(item, activeChar);
		}
		
		if (crystallize)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED));
			ItemFunctions.addItem(activeChar, crystalId, crystalAmount, true);
		}
		else
		{
			if (count > 1)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_S_DISAPPEARED).addItemName(item.getId()).addLong(count));
			}
			else
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_DISAPPEARED).addItemName(item.getId()));
			}
		}
		
		activeChar.sendChanges();
	}
}
