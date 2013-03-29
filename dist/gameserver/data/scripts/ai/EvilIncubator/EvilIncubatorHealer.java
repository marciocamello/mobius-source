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
package ai.EvilIncubator;

import instances.EvilIncubator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author KilRoy
 */
public class EvilIncubatorHealer extends Fighter
{
	private boolean _thinking = false;
	private final Skill recharge = SkillTable.getInstance().getInfo(6728, 1);
	private final Skill heal = SkillTable.getInstance().getInfo(6724, 1);
	
	ScheduledFuture<?> _followTask;
	
	public EvilIncubatorHealer(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean thinkActive()
	{
		EvilIncubator instance = (EvilIncubator) getActor().getReflection();
		NpcInstance actor = getActor();
		Creature following = actor.getFollowTarget();
		if (instance.getInstanceStage() >= 1)
		{
			if ((following == null) || !actor.isFollow)
			{
				Player master = getMaster();
				if (master != null)
				{
					actor.setFollowTarget(master);
					actor.setRunning();
					actor.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, master, Config.FOLLOW_RANGE);
				}
			}
		}
		super.thinkActive();
		return false;
	}
	
	@Override
	protected void onEvtThink()
	{
		NpcInstance actor = getActor();
		if (_thinking || actor.isActionsDisabled() || actor.isAfraid() || actor.isDead() || actor.isMovementDisabled())
		{
			return;
		}
		
		_thinking = true;
		try
		{
			if (!Config.BLOCK_ACTIVE_TASKS && ((getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) || (getIntention() == CtrlIntention.AI_INTENTION_IDLE)))
			{
				thinkActive();
			}
			else if (getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
			{
				thinkFollow();
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			_thinking = false;
		}
	}
	
	private Player getMaster()
	{
		if (!getActor().getReflection().getPlayers().isEmpty())
		{
			return getActor().getReflection().getPlayers().get(0);
		}
		return null;
	}
	
	protected void thinkFollow()
	{
		NpcInstance actor = getActor();
		
		Creature target = actor.getFollowTarget();
		
		if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000) || actor.isMovementDisabled())
		{
			actor.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			return;
		}
		
		if (actor.isFollow && (actor.getFollowTarget() == target))
		{
			clientActionFailed();
			return;
		}
		
		if (actor.isInRange(target, Config.FOLLOW_RANGE + 20))
		{
			clientActionFailed();
		}
		
		if (_followTask != null)
		{
			_followTask.cancel(false);
			_followTask = null;
		}
		
		_followTask = ThreadPoolManager.getInstance().schedule(new ThinkFollow(), 250L);
		
		Reflection ref = actor.getReflection();
		
		if (ref != null)
		{
			Map<Skill, Integer> d_skill = new HashMap<>();
			double distance = actor.getDistance(target);
			if (ref.getInstancedZoneId() == 185)
			{
				if (target.getCurrentHpPercents() < 70)
				{
					addDesiredSkill(d_skill, target, distance, heal);
				}
				if (target.getCurrentMpPercents() < 50)
				{
					addDesiredSkill(d_skill, target, distance, recharge);
				}
				
				Skill r_skill = selectTopSkill(d_skill);
				chooseTaskAndTargets(r_skill, target, distance);
				doTask();
			}
		}
	}
	
	protected class ThinkFollow extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			if (actor == null)
			{
				return;
			}
			
			EvilIncubator instance = (EvilIncubator) actor.getReflection();
			
			Creature target = actor.getFollowTarget();
			
			if ((target == null) || (actor.getDistance(target) > 4000) || (instance.getInstanceStage() < 1))
			{
				setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				return;
			}
			
			if (!actor.isInRange(target, Config.FOLLOW_RANGE + 20) && (!actor.isFollow || (actor.getFollowTarget() != target)))
			{
				Location loc = new Location(target.getX() + Rnd.get(-60, 60), target.getY() + Rnd.get(-60, 60), target.getZ());
				actor.followToCharacter(loc, target, Config.FOLLOW_RANGE, false);
			}
			_followTask = ThreadPoolManager.getInstance().schedule(this, 250L);
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	public void addTaskAttack(Creature target)
	{
	}
}