/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.ai;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.PlayableAI.nextAction;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Die;
import lineage2.gameserver.utils.Location;

public class CharacterAI extends AbstractAI
{
	public CharacterAI(Creature actor)
	{
		super(actor);
	}
	
	@Override
	protected void onIntentionIdle()
	{
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
	}
	
	@Override
	protected void onIntentionActive()
	{
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		onEvtThink();
	}
	
	@Override
	protected void onIntentionAttack(Creature target)
	{
		setAttackTarget(target);
		clientStopMoving();
		changeIntention(CtrlIntention.AI_INTENTION_ATTACK, target, null);
		onEvtThink();
	}
	
	@Override
	protected void onIntentionCast(Skill skill, Creature target)
	{
		setAttackTarget(target);
		changeIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
		onEvtThink();
	}
	
	@Override
	protected void onIntentionFollow(Creature target, Integer offset)
	{
		changeIntention(CtrlIntention.AI_INTENTION_FOLLOW, target, offset);
		onEvtThink();
	}
	
	@Override
	protected void onIntentionInteract(GameObject object)
	{
	}
	
	@Override
	protected void onIntentionPickUp(GameObject item)
	{
	}
	
	@Override
	protected void onIntentionRest()
	{
	}
	
	@Override
	protected void onIntentionCoupleAction(Player player, Integer socialId)
	{
	}
	
	@Override
	protected void onEvtArrivedBlocked(Location blocked_at_pos)
	{
		Creature actor = getActor();
		if (actor.isPlayer())
		{
			Location loc = ((Player) actor).getLastServerPosition();
			if (loc != null)
			{
				actor.setLoc(loc, true);
			}
			actor.stopMove();
		}
		onEvtThink();
	}
	
	@Override
	protected void onEvtForgetObject(GameObject object)
	{
		if (object == null)
		{
			return;
		}
		Creature actor = getActor();
		if (actor.isAttackingNow() && (getAttackTarget() == object))
		{
			actor.abortAttack(true, true);
		}
		if (actor.isCastingNow() && (getAttackTarget() == object))
		{
			actor.abortCast(true, true);
		}
		if (getAttackTarget() == object)
		{
			setAttackTarget(null);
		}
		if (actor.getTargetId() == object.getObjectId())
		{
			actor.setTarget(null);
		}
		if (actor.getFollowTarget() == object)
		{
			actor.setFollowTarget(null);
		}
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		Creature actor = getActor();
		actor.abortAttack(true, true);
		actor.abortCast(true, true);
		actor.stopMove();
		actor.broadcastPacket(new Die(actor));
		setIntention(CtrlIntention.AI_INTENTION_IDLE);
	}
	
	@Override
	protected void onEvtFakeDeath()
	{
		clientStopMoving();
		setIntention(CtrlIntention.AI_INTENTION_IDLE);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
	}
	
	public void Attack(GameObject target, boolean forceUse, boolean dontMove)
	{
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
	}
	
	public void Cast(Skill skill, Creature target)
	{
		Cast(skill, target, false, false);
	}
	
	public void Cast(Skill skill, Creature target, boolean forceUse, boolean dontMove)
	{
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
	}
	
	@Override
	protected void onEvtThink()
	{
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
	
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
	}
	
	@Override
	protected void onEvtReadyToAct()
	{
	}
	
	@Override
	protected void onEvtArrived()
	{
	}
	
	@Override
	protected void onEvtArrivedTarget()
	{
	}
	
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
	}
	
	@Override
	protected void onEvtSpawn()
	{
	}
	
	@Override
	public void onEvtDeSpawn()
	{
	}
	
	public void stopAITask()
	{
	}
	
	public void startAITask()
	{
	}
	
	public void setNextAction(nextAction action, Object arg0, Object arg1, boolean arg2, boolean arg3)
	{
	}
	
	public void clearNextAction()
	{
	}
	
	public boolean isActive()
	{
		return true;
	}
	
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
	}
	
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
	}
	
	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
	}
	
	protected void addTimer(int timerId, long delay)
	{
		addTimer(timerId, null, null, delay);
	}
	
	protected void addTimer(int timerId, Object arg1, long delay)
	{
		addTimer(timerId, arg1, null, delay);
	}
	
	protected void addTimer(int timerId, Object arg1, Object arg2, long delay)
	{
		ThreadPoolManager.getInstance().schedule(new Timer(timerId, arg1, arg2), delay);
	}
	
	protected class Timer extends RunnableImpl
	{
		private final int _timerId;
		private final Object _arg1;
		private final Object _arg2;
		
		public Timer(int timerId, Object arg1, Object arg2)
		{
			_timerId = timerId;
			_arg1 = arg1;
			_arg2 = arg2;
		}
		
		@Override
		public void runImpl()
		{
			notifyEvent(CtrlEvent.EVT_TIMER, _timerId, _arg1, _arg2);
		}
	}
	
	protected void broadCastScriptEvent(String event, int radius)
	{
		broadCastScriptEvent(event, null, null, radius);
	}
	
	protected void broadCastScriptEvent(String event, Object arg1, int radius)
	{
		broadCastScriptEvent(event, arg1, null, radius);
	}
	
	protected void broadCastScriptEvent(String event, Object arg1, Object arg2, int radius)
	{
		List<NpcInstance> npcs = World.getAroundNpc(getActor(), radius, radius);
		for (NpcInstance npc : npcs)
		{
			npc.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, event, arg1, arg2);
		}
	}
}
