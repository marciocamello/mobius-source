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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.data.xml.holder.RestorationInfoHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.skill.restoration.RestorationInfo;
import lineage2.gameserver.templates.skill.restoration.RestorationItem;
import lineage2.gameserver.utils.ItemFunctions;

public class Restoration extends Skill
{
	public Restoration(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if (!activeChar.isPlayable())
		{
			return false;
		}
		
		if (activeChar.isPlayer())
		{
			Player player = (Player) activeChar;
			
			if ((player.getWeightPenalty() >= 3) || (player.getInventory().getSize() > (player.getInventoryLimit() - 10)))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_CORRESPONDING_WORK_CANNOT_BE_PROCEEDED_BECAUSE_THE_INVENTORY_WEIGHT_QUANTITY_LIMIT_HAS_BEEN_EXCEEDED));
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (!activeChar.isPlayable())
		{
			return;
		}
		
		RestorationInfo restorationInfo = RestorationInfoHolder.getInstance().getRestorationInfo(this);
		
		if (restorationInfo == null)
		{
			return;
		}
		
		Playable playable = (Playable) activeChar;
		int itemConsumeId = restorationInfo.getItemConsumeId();
		int itemConsumeCount = restorationInfo.getItemConsumeCount();
		
		if ((itemConsumeId > 0) && (itemConsumeCount > 0))
		{
			if (ItemFunctions.getItemCount(playable, itemConsumeId) < itemConsumeCount)
			{
				playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_NOT_ENOUGH_NECESSARY_ITEMS_TO_USE_THE_SKILL));
				return;
			}
			
			ItemFunctions.removeItem(playable, itemConsumeId, itemConsumeCount, true);
		}
		
		List<RestorationItem> restorationItems = restorationInfo.getRandomGroupItems();
		
		if ((restorationItems == null) || (restorationItems.size() == 0))
		{
			return;
		}
		
		for (Creature target : targets)
		{
			if (target != null)
			{
				for (RestorationItem item : restorationItems)
				{
					ItemFunctions.addItem(playable, item.getId(), item.getRandomCount(), true);
				}
				
				getEffects(activeChar, target, false, false);
			}
		}
	}
}