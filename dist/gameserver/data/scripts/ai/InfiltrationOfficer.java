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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Guard;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

public class InfiltrationOfficer extends Guard implements OnAttackListener, OnMagicUseListener
{
	public enum State
	{
		AI_IDLE(0),
		AI_FOLLOW(1),
		AI_ATTACK_GENERATOR(2),
		AI_NEXT_STEP(3);
		@SuppressWarnings("unused")
		private final int _id;
		
		State(int id)
		{
			_id = id;
		}
	}
	
	private final static int GENERATOR = 33216;
	private final static int[][] POINTS =
	{
		{
			-117032,
			212568,
			-8617
		},
		{
			-117896,
			214264,
			-8617
		},
		{
			-119208,
			213768,
			-8617
		},
	};
	private boolean configured = false;
	private short _step = 0;
	private State _state = State.AI_IDLE;
	private long lastFollowPlayer = 0;
	private boolean attacksGenerator = false;
	private long lastOfficerSay = 0;
	private Player player = null;
	
	public InfiltrationOfficer(NpcInstance actor)
	{
		super(actor);
		actor.setRunning();
	}
	
	private void config()
	{
		if (!configured)
		{
			getActor().getFollowTarget().addListener(this);
			configured = true;
		}
	}
	
	@Override
	public void onAttack(Creature actor, Creature target)
	{
		if (isUnderState(State.AI_FOLLOW) && target.isMonster())
		{
			getActor().getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}
	
	@Override
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
	{
		if (isUnderState(State.AI_FOLLOW) && target.isMonster())
		{
			getActor().getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}
	
	public void setState(State state)
	{
		config();
		_state = state;
	}
	
	public boolean isUnderState(State state)
	{
		return _state == state;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
	}
	
	@Override
	public void onEvtDeSpawn()
	{
		if (getActor().getFollowTarget() != null)
		{
			getActor().getFollowTarget().removeListener(this);
		}
	}
	
	@Override
	protected void thinkAttack()
	{
		super.thinkAttack();
	}
	
	@Override
	protected boolean thinkActive()
	{
		if (isUnderState(State.AI_IDLE))
		{
			return false;
		}
		NpcInstance actor = getActor();
		if (player == null)
		{
			player = actor.getFollowTarget().getPlayer();
		}
		if (player == null)
		{
			return false;
		}
		actor.setRunning();
		if (isUnderState(State.AI_FOLLOW) && actor.getAggroList().isEmpty() && ((System.currentTimeMillis() - lastFollowPlayer) > 2000))
		{
			lastFollowPlayer = System.currentTimeMillis();
			actor.setFollowTarget(player);
			actor.moveToLocation(player.getLoc(), Rnd.get(200), true);
		}
		else if (isUnderState(State.AI_NEXT_STEP))
		{
			if (_step < POINTS.length)
			{
				actor.setFollowTarget(player);
				actor.moveToLocation(new Location(POINTS[_step][0], POINTS[_step][1], POINTS[_step][2]), 0, true);
				++_step;
			}
			setState(State.AI_IDLE);
		}
		else if (isUnderState(State.AI_ATTACK_GENERATOR))
		{
			if (!attacksGenerator)
			{
				actor.getAggroList().clear();
				actor.getAggroList().addDamageHate(actor.getReflection().getAllByNpcId(GENERATOR, true).get(0), 0, Integer.MAX_VALUE);
				setIntention(CtrlIntention.AI_INTENTION_ATTACK);
				attacksGenerator = true;
			}
			if ((System.currentTimeMillis() - lastOfficerSay) > 3000)
			{
				actor.broadcastPacket(new NpcSay(actor, ChatType.ALL, NpcString.DONT_COME_BACK_HERE));
				lastOfficerSay = System.currentTimeMillis();
			}
		}
		return false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (getActor().getFollowTarget() != null)
		{
			getActor().getFollowTarget().sendPacket(new ExShowScreenMessage(NpcString.IF_TERAIN_DIES_MISSION_WILL_FAIL, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
		}
	}
}
