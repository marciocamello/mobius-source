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
package ai;

import java.util.ArrayList;
import java.util.List;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author ALF
 */
public final class Wispes extends DefaultAI
{
	public Wispes(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
		AI_TASK_ATTACK_DELAY = 2000;
	}
	
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			NpcInstance npc = getActor();
			
			if (npc == null)
			{
				return true;
			}
			
			List<Creature> target = new ArrayList<>();
			
			for (Player player : World.getAroundPlayers(npc, 300, 300))
			{
				if (player.getEffectList().getEffectsBySkillId(12001) == null)
				{
					target.add(player);
					
					if (npc.getNpcId() == 32915)
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, 14064, 1, 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(14064, 1), target, true);
					}
					else if (npc.getNpcId() == 32916)
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, 14065, 1, 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(14065, 1), target, true);
					}
				}
				
				if (target.size() > 0)
				{
					target.clear();
					npc.deleteMe();
				}
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}