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

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAI extends RunnableImpl
{
	protected static final Logger _log = LoggerFactory.getLogger(AbstractAI.class);
	protected final Creature _actor;
	private HardReference<? extends Creature> _attackTarget = HardReferences.emptyRef();
	private CtrlIntention _intention = CtrlIntention.AI_INTENTION_IDLE;
	
	protected AbstractAI(Creature actor)
	{
		_actor = actor;
	}
	
	@Override
	public void runImpl()
	{
	}
	
	public void changeIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		_intention = intention;
		if ((intention != CtrlIntention.AI_INTENTION_CAST) && (intention != CtrlIntention.AI_INTENTION_ATTACK))
		{
			setAttackTarget(null);
		}
	}
	
	public final void setIntention(CtrlIntention intention)
	{
		setIntention(intention, null, null);
	}
	
	public final void setIntention(CtrlIntention intention, Object arg0)
	{
		setIntention(intention, arg0, null);
	}
	
	public void setIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		if ((intention != CtrlIntention.AI_INTENTION_CAST) && (intention != CtrlIntention.AI_INTENTION_ATTACK))
		{
			setAttackTarget(null);
		}
		Creature actor = getActor();
		if (!actor.isVisible())
		{
			if (_intention == CtrlIntention.AI_INTENTION_IDLE)
			{
				return;
			}
			intention = CtrlIntention.AI_INTENTION_IDLE;
		}
		actor.getListeners().onAiIntention(intention, arg0, arg1);
		switch (intention)
		{
			case AI_INTENTION_IDLE:
				onIntentionIdle();
				break;
			case AI_INTENTION_ACTIVE:
				onIntentionActive();
				break;
			case AI_INTENTION_REST:
				onIntentionRest();
				break;
			case AI_INTENTION_ATTACK:
				onIntentionAttack((Creature) arg0);
				break;
			case AI_INTENTION_CAST:
				onIntentionCast((Skill) arg0, (Creature) arg1);
				break;
			case AI_INTENTION_PICK_UP:
				onIntentionPickUp((GameObject) arg0);
				break;
			case AI_INTENTION_INTERACT:
				onIntentionInteract((GameObject) arg0);
				break;
			case AI_INTENTION_FOLLOW:
				onIntentionFollow((Creature) arg0, (Integer) arg1);
				break;
			case AI_INTENTION_COUPLE_ACTION:
				onIntentionCoupleAction((Player) arg0, (Integer) arg1);
				break;
		}
	}
	
	public final void notifyEvent(CtrlEvent evt)
	{
		notifyEvent(evt, new Object[] {});
	}
	
	public final void notifyEvent(CtrlEvent evt, Object arg0)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			null,
			null
		});
	}
	
	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1,
			null
		});
	}
	
	public final void notifyEvent(CtrlEvent evt, Object arg0, Object arg1, Object arg2)
	{
		notifyEvent(evt, new Object[]
		{
			arg0,
			arg1,
			arg2
		});
	}
	
	public void notifyEvent(CtrlEvent evt, Object[] args)
	{
		Creature actor = getActor();
		if ((actor == null) || !actor.isVisible())
		{
			return;
		}
		actor.getListeners().onAiEvent(evt, args);
		switch (evt)
		{
			case EVT_THINK:
				onEvtThink();
				break;
			case EVT_ATTACKED:
				onEvtAttacked((Creature) args[0], ((Number) args[1]).intValue());
				break;
			case EVT_CLAN_ATTACKED:
				onEvtClanAttacked((Creature) args[0], (Creature) args[1], ((Number) args[2]).intValue());
				break;
			case EVT_AGGRESSION:
				onEvtAggression((Creature) args[0], ((Number) args[1]).intValue());
				break;
			case EVT_READY_TO_ACT:
				onEvtReadyToAct();
				break;
			case EVT_ARRIVED:
				onEvtArrived();
				break;
			case EVT_ARRIVED_TARGET:
				onEvtArrivedTarget();
				break;
			case EVT_ARRIVED_BLOCKED:
				onEvtArrivedBlocked((Location) args[0]);
				break;
			case EVT_FORGET_OBJECT:
				onEvtForgetObject((GameObject) args[0]);
				break;
			case EVT_DEAD:
				onEvtDead((Creature) args[0]);
				break;
			case EVT_FAKE_DEATH:
				onEvtFakeDeath();
				break;
			case EVT_FINISH_CASTING:
				onEvtFinishCasting((Integer) args[0], (Boolean) args[1]);
				break;
			case EVT_SEE_SPELL:
				onEvtSeeSpell((Skill) args[0], (Creature) args[1]);
				break;
			case EVT_SPAWN:
				onEvtSpawn();
				break;
			case EVT_DESPAWN:
				onEvtDeSpawn();
				break;
			case EVT_TIMER:
				onEvtTimer(((Number) args[0]).intValue(), args[1], args[2]);
				break;
			case EVT_SCRIPT_EVENT:
				onEvtScriptEvent(args[0].toString(), args[1], args[2]);
				break;
			case EVT_MENU_SELECTED:
				onEvtMenuSelected((Player) args[0], ((Number) args[1]).intValue(), ((Number) args[2]).intValue());
				break;
		}
	}
	
	protected void clientActionFailed()
	{
		Creature actor = getActor();
		if ((actor != null) && actor.isPlayer())
		{
			actor.sendActionFailed();
		}
	}
	
	public void clientStopMoving(boolean validate)
	{
		Creature actor = getActor();
		actor.stopMove(validate);
	}
	
	public void clientStopMoving()
	{
		Creature actor = getActor();
		actor.stopMove();
	}
	
	public Creature getActor()
	{
		return _actor;
	}
	
	public CtrlIntention getIntention()
	{
		return _intention;
	}
	
	public void setAttackTarget(Creature target)
	{
		_attackTarget = target == null ? HardReferences.<Creature> emptyRef() : target.getRef();
	}
	
	public Creature getAttackTarget()
	{
		return _attackTarget.get();
	}
	
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " for " + getActor();
	}
	
	protected abstract void onIntentionIdle();
	
	protected abstract void onIntentionActive();
	
	protected abstract void onIntentionRest();
	
	protected abstract void onIntentionAttack(Creature target);
	
	protected abstract void onIntentionCast(Skill skill, Creature target);
	
	protected abstract void onIntentionPickUp(GameObject item);
	
	protected abstract void onIntentionInteract(GameObject object);
	
	protected abstract void onIntentionCoupleAction(Player player, Integer socialId);
	
	protected abstract void onEvtThink();
	
	protected abstract void onEvtAttacked(Creature attacker, int damage);
	
	protected abstract void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage);
	
	protected abstract void onEvtAggression(Creature target, int aggro);
	
	protected abstract void onEvtReadyToAct();
	
	protected abstract void onEvtArrived();
	
	protected abstract void onEvtArrivedTarget();
	
	protected abstract void onEvtArrivedBlocked(Location blocked_at_pos);
	
	protected abstract void onEvtForgetObject(GameObject object);
	
	protected abstract void onEvtDead(Creature killer);
	
	protected abstract void onEvtFakeDeath();
	
	protected abstract void onEvtFinishCasting(int skill_id, boolean success);
	
	protected abstract void onEvtSeeSpell(Skill skill, Creature caster);
	
	protected abstract void onEvtSpawn();
	
	public abstract void onEvtDeSpawn();
	
	protected abstract void onIntentionFollow(Creature target, Integer offset);
	
	protected abstract void onEvtTimer(int timerId, Object arg1, Object arg2);
	
	protected abstract void onEvtScriptEvent(String event, Object arg1, Object arg2);
	
	protected abstract void onEvtMenuSelected(Player player, int ask, int reply);
}
