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
package ai.residences;

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import npc.model.residences.SiegeGuardInstance;

public abstract class SiegeGuard extends DefaultAI
{
	public SiegeGuard(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 1000;
	}
	
	@Override
	public SiegeGuardInstance getActor()
	{
		return (SiegeGuardInstance) super.getActor();
	}
	
	@Override
	public int getMaxPathfindFails()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	@Override
	public boolean canSeeInSilentMove(Playable target)
	{
		return !target.isSilentMoving() || Rnd.chance(10);
	}
	
	@Override
	protected boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isAlikeDead() || target.isInvul())
		{
			return false;
		}
		if (target.isPlayable())
		{
			if (!canSeeInSilentMove((Playable) target))
			{
				return false;
			}
			if (!canSeeInHide((Playable) target))
			{
				return false;
			}
			if (target.isPlayer() && ((Player) target).isGM() && target.isInvisible())
			{
				return false;
			}
			if (((Playable) target).getNonAggroTime() > System.currentTimeMillis())
			{
				return false;
			}
			if (target.isPlayer() && !target.getPlayer().isActive())
			{
				return false;
			}
			if (actor.isMonster() && target.isInZonePeace())
			{
				return false;
			}
		}
		AggroList.AggroInfo ai = actor.getAggroList().get(target);
		if ((ai != null) && (ai.hate > 0))
		{
			if (!target.isInRangeZ(actor.getSpawnedLoc(), MAX_PURSUE_RANGE))
			{
				return false;
			}
		}
		else if (!target.isInRangeZ(actor.getSpawnedLoc(), 600))
		{
			return false;
		}
		if (!canAttackCharacter(target))
		{
			return false;
		}
		if (!GeoEngine.canSeeTarget(actor, target, false))
		{
			return false;
		}
		actor.getAggroList().addDamageHate(target, 0, 2);
		if ((target.isServitor() || target.isPet()))
		{
			actor.getAggroList().addDamageHate(target.getPlayer(), 0, 1);
		}
		startRunningTask(AI_TASK_ATTACK_DELAY);
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		return true;
	}
	
	@Override
	protected boolean isGlobalAggro()
	{
		return true;
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		SiegeGuardInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		if ((target == null) || !actor.isAutoAttackable(target))
		{
			return;
		}
		super.onEvtAggression(target, aggro);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isActionsDisabled())
		{
			return true;
		}
		if (_def_think)
		{
			if (doTask())
			{
				clearTasks();
			}
			return true;
		}
		long now = System.currentTimeMillis();
		if ((now - _checkAggroTimestamp) > Config.AGGRO_CHECK_INTERVAL)
		{
			_checkAggroTimestamp = now;
			List<Creature> chars = World.getAroundCharacters(actor);
			CollectionUtils.eqSort(chars, _nearestTargetComparator);
			for (Creature cha : chars)
			{
				if (checkAggression(cha))
				{
					return true;
				}
			}
		}
		Location sloc = actor.getSpawnedLoc();
		if (!actor.isInRange(sloc, 250))
		{
			teleportHome();
			return true;
		}
		return false;
	}
	
	@Override
	protected Creature prepareTarget()
	{
		SiegeGuardInstance actor = getActor();
		if (actor.isDead())
		{
			return null;
		}
		List<Creature> hateList = actor.getAggroList().getHateList();
		Creature hated = null;
		for (Creature cha : hateList)
		{
			if (!checkTarget(cha, MAX_PURSUE_RANGE))
			{
				actor.getAggroList().remove(cha, true);
				continue;
			}
			hated = cha;
			break;
		}
		if (hated != null)
		{
			setAttackTarget(hated);
			return hated;
		}
		return null;
	}
	
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		return getActor().isAutoAttackable(target);
	}
}
