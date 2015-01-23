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
package ai.FieldOfSilenceAndWhispers;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Exterminator extends Fighter
{
	private NpcInstance mob = null;
	private boolean _firstTimeAttacked = true;
	public static final NpcStringId[] MsgText =
	{
		NpcStringId.DRIVE_DEVICE_ENTIRE_DESTRUCTION_MOVING_SUSPENSION,
		NpcStringId.DRIVE_DEVICE_PARTIAL_DESTRUCTION_IMPULSE_RESULT
	};
	
	/**
	 * Constructor for Exterminator.
	 * @param actor NpcInstance
	 */
	public Exterminator(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		final NpcInstance actor = getActor();
		
		if (actor == null)
		{
			return;
		}
		
		if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
		{
			Functions.npcSay(actor, NpcStringId.TARGET_THREAT_LEVEL_LAUNCHING_STRONGEST_COUNTERMEASURE, ChatType.SHOUT, 5000);
		}
		
		super.onIntentionAttack(target);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		
		if (mob == null)
		{
			final List<NpcInstance> around = getActor().getAroundNpc(300, 300);
			
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if ((npc.getId() >= 22650) && (npc.getId() <= 22655))
					{
						if ((mob == null) || (getActor().getDistance3D(npc) < getActor().getDistance3D(mob)))
						{
							mob = npc;
						}
					}
				}
			}
		}
		
		if (mob != null)
		{
			actor.stopMove();
			actor.setRunning();
			getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, mob, 1);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		
		if (actor == null)
		{
			return;
		}
		
		if (_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			
			if (Rnd.chance(25))
			{
				Functions.npcSay(actor, Rnd.get(MsgText), ChatType.ALL, 5000);
			}
		}
		else if (Rnd.chance(10))
		{
			Functions.npcSay(actor, NpcStringId.TARGET_THREAT_LEVEL_LAUNCHING_STRONGEST_COUNTERMEASURE, ChatType.SHOUT, 5000);
		}
		
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}
