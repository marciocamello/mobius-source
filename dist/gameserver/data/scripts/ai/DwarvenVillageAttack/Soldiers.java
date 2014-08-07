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
package ai.DwarvenVillageAttack;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

import org.apache.commons.lang3.ArrayUtils;

public final class Soldiers extends Fighter
{
	private static final int[] ATTACK_IDS =
	{
		19171,
		19172
	};
	
	public Soldiers(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 10;
	}
	
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
	
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		NpcInstance actor = getActor();
		
		if (getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
		{
			AggroList.AggroInfo ai = actor.getAggroList().get(target);
			return (ai != null) && (ai.hate > 0);
		}
		
		return ArrayUtils.contains(ATTACK_IDS, target.getNpcId());
	}
	
	@Override
	public boolean checkAggression(Creature target)
	{
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		
		if (target.isNpc() && !ArrayUtils.contains(ATTACK_IDS, target.getNpcId()))
		{
			return false;
		}
		
		return super.checkAggression(target);
	}
}
