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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class FollowNpc extends DefaultAI
{
	private boolean _thinking = false;
	ScheduledFuture<?> _followTask;
	
	public FollowNpc(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean randomWalk()
	{
		if (getActor() instanceof MonsterInstance)
		{
			return true;
		}
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
	
	protected void thinkFollow()
	{
		NpcInstance actor = getActor();
		Creature target = actor.getFollowTarget();
		if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000) || actor.isMovementDisabled())
		{
			clientActionFailed();
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
	}
	
	protected class ThinkFollow extends RunnableImpl
	{
		public NpcInstance getActor()
		{
			return FollowNpc.this.getActor();
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			if (actor == null)
			{
				return;
			}
			Creature target = actor.getFollowTarget();
			if ((target == null) || target.isAlikeDead() || (actor.getDistance(target) > 4000))
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
}
