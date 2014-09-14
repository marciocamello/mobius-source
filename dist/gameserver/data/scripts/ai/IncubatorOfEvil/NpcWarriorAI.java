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
package ai.IncubatorOfEvil;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Iqman
 */
public final class NpcWarriorAI extends Fighter
{
	private NpcInstance target = null;
	
	public NpcWarriorAI(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		startAttack();
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		
		if (Rnd.chance(3))
		{
			switch (actor.getId())
			{
				case 33172:
					Functions.npcSay(actor, NpcString.THE_ONLY_GOOD_SHILEN_CREATURE_IS_A_DEAD_ONE);
					break;
				
				case 33170:
					Functions.npcSay(actor, NpcString.GET_BEHIND_ME_GET_BEHIND_ME);
					break;
				
				default:
					break;
			}
		}
		
		return startAttack();
	}
	
	private boolean startAttack()
	{
		NpcInstance actor = getActor();
		
		if (target == null)
		{
			List<NpcInstance> around = actor.getAroundNpc(3000, 150);
			
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (checkTarget(npc))
					{
						if ((target == null) || (actor.getDistance3D(npc) < actor.getDistance3D(target)))
						{
							target = npc;
						}
					}
				}
			}
		}
		
		if ((target != null) && !actor.isAttackingNow() && !actor.isCastingNow() && !target.isDead() && GeoEngine.canSeeTarget(actor, target, false) && target.isVisible())
		{
			actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 1);
			return true;
		}
		
		if ((target != null) && (!target.isVisible() || target.isDead() || !GeoEngine.canSeeTarget(actor, target, false)))
		{
			target = null;
			return false;
		}
		
		return false;
	}
	
	private boolean checkTarget(NpcInstance target)
	{
		if (target == null)
		{
			return false;
		}
		
		int _id = target.getId();
		
		if ((_id == 33170) || (_id == 33171) || (_id == 33172) || (_id == 33173) || (_id == 33174) || (_id == 33414) || (_id == 33415) || (_id == 33416))
		{
			return false;
		}
		
		return true;
	}
}