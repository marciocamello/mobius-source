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
package ai.GuillotineFortress;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.EffectList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.NpcUtils;

public final class GuillotineMonsters extends Fighter
{
	private boolean _chekerLocked = true;
	
	public GuillotineMonsters(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		NpcInstance actor = getActor();
		addTaskBuff(actor, SkillTable.getInstance().getInfo(15208, 9));
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);
		NpcInstance actor = getActor();
		if (attacker == null)
		{
			return;
		}
		
		Player player = (Player) attacker;
		double actor_hp_precent = actor.getCurrentHpPercents();
		
		if ((actor_hp_precent < 85) && _chekerLocked)
		{
			_chekerLocked = false;
			EffectList effectList = actor.getEffectList();
			if (effectList != null)
			{
				effectList.stopEffect(SkillTable.getInstance().getInfo(15208, 9));
				
				if (attacker.isPlayer())
				{
					player.sendPacket(new ExShowScreenMessage(NpcStringId.CHAOS_SHIELD_BREAKTHROUGH, 10000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
				}
			}
		}
		
		PcInventory playerInventory = player.getInventory();
		if (playerInventory == null)
		{
			return;
		}
		
		if ((playerInventory.getItemByItemId(34898) != null) && Rnd.chance(1))
		{
			NpcUtils.spawnSingle(23212, player.getLoc(), player.getReflection());
			player.getInventory().destroyItemByItemId(34898, 1);
		}
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		
		if ((killer != null) && killer.isPlayer() && Rnd.chance(4))
		{
			Player player = (Player) killer;
			player.getInventory().addItem(34898, 1);
		}
	}
}