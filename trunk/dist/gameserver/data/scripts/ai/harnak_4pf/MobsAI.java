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
package ai.harnak_4pf;

import instances.HarnakUndergroundRuins;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MobsAI extends Fighter
{
	/**
	 * Field moveLoc1.
	 */
	private static final Location moveLoc1 = new Location(-107930, 206328, -10872);
	/**
	 * Field moveLoc2.
	 */
	private static final Location moveLoc2 = new Location(-107930, 208861, -10872);
	/**
	 * Field SKILL_IDS.
	 */
	private static final int[] SKILL_IDS =
	{
		14612,
		14613,
		14614
	};
	/**
	 * Field ULTIMATE_BUFF_ID. (value is 4318)
	 */
	private static final int ULTIMATE_BUFF_ID = 4318;
	/**
	 * Field selected.
	 */
	private boolean selected = false;
	/**
	 * Field NEXT_MOB_ID.
	 */
	private final int NEXT_MOB_ID;
	/**
	 * Field MSG2_ID. Field MSG1_ID.
	 */
	private final int MSG1_ID, MSG2_ID;
	/**
	 * Field ROOM_ID.
	 */
	private final int ROOM_ID;
	/**
	 * Field NEXT_GROUP.
	 */
	private final String NEXT_GROUP;
	/**
	 * Field IS_LAST_GROUP.
	 */
	private final boolean IS_LAST_GROUP;
	
	/**
	 * Constructor for MobsAI.
	 * @param actor NpcInstance
	 */
	public MobsAI(NpcInstance actor)
	{
		super(actor);
		NEXT_MOB_ID = actor.getParameter("nextMobId", -1);
		MSG1_ID = actor.getParameter("msg1Id", -1);
		MSG2_ID = actor.getParameter("msg2Id", -1);
		ROOM_ID = actor.getParameter("room", -1);
		NEXT_GROUP = actor.getParameter("nextGroup", "");
		IS_LAST_GROUP = actor.getParameter("lastGroup", false);
		AI_TASK_ATTACK_DELAY = 50;
		AI_TASK_ACTIVE_DELAY = 250;
		getActor().setRunning();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if (ROOM_ID == 2)
		{
			if (MSG2_ID > 0)
			{
				selectMe();
			}
		}
		else if (ROOM_ID == 3)
		{
			final Reflection r = getActor().getReflection();
			if (!(r instanceof HarnakUndergroundRuins))
			{
				return;
			}
			for (Player p : getActor().getReflection().getPlayers())
			{
				getActor().getAggroList().addDamageHate(p, 1, 10000000);
				addTaskAttack(p);
			}
		}
	}
	
	/**
	 * Method selectMe.
	 */
	public void selectMe()
	{
		selected = true;
		if (MSG1_ID > 0)
		{
			Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(MSG1_ID));
		}
		addTaskMove((ROOM_ID == 1) ? moveLoc1 : moveLoc2, false);
		doTask();
		addTimer(1, 3000);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);
		if (!selected)
		{
			broadCastScriptEvent("ATTACK_HIM", attacker, 1500);
		}
		if ((ROOM_ID == 2) || (ROOM_ID == 3))
		{
			if (Rnd.chance(5))
			{
				int SKILL_ID = SKILL_IDS[0];
				if (getActor().getCurrentHpPercents() < 60)
				{
					SKILL_ID = SKILL_IDS[1];
				}
				else if (getActor().getCurrentHpPercents() < 35)
				{
					SKILL_ID = SKILL_IDS[2];
				}
				final Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
				skill.getEffects(getActor(), getActor(), false, false);
			}
		}
		if (ROOM_ID == 2)
		{
			if (IS_LAST_GROUP && (getActor().getCurrentHpPercents() < 80) && (getActor().getEffectList().getEffectsBySkillId(ULTIMATE_BUFF_ID) == null))
			{
				final Skill skill = SkillTable.getInstance().getInfo(ULTIMATE_BUFF_ID, 1);
				skill.getEffects(getActor(), getActor(), false, false);
			}
		}
	}
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		if (event.equalsIgnoreCase("ATTACK_HIM"))
		{
			selected = false;
			final Creature attacker = (Creature) arg1;
			getActor().getAggroList().addDamageHate(attacker, 1, 10000000);
			addTaskAttack(attacker);
		}
		else if (event.equalsIgnoreCase("SELECT_ME"))
		{
			if (ROOM_ID == 1)
			{
				selectMe();
			}
		}
		else if (event.equalsIgnoreCase("FAIL_INSTANCE"))
		{
			getActor().deleteMe();
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		final Reflection r = getActor().getReflection();
		if (!(r instanceof HarnakUndergroundRuins))
		{
			return;
		}
		if (ROOM_ID == 1)
		{
			((HarnakUndergroundRuins) r).decreaseFirstRoomMobsCount();
			if (selected)
			{
				selected = false;
				if (NEXT_MOB_ID > 0)
				{
					final List<NpcInstance> npcs = r.getAllByNpcId(NEXT_MOB_ID, true);
					if (!npcs.isEmpty())
					{
						npcs.get(0).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "SELECT_ME");
					}
				}
			}
		}
		else if (ROOM_ID == 2)
		{
			if (!NEXT_GROUP.isEmpty() && r.getAllByNpcId(getActor().getNpcId(), true).isEmpty())
			{
				((HarnakUndergroundRuins) r).increaseSecondRoomGroup();
				r.spawnByGroup(NEXT_GROUP);
				broadCastScriptEvent("ATTACK_HIM", killer, 3000);
			}
			else if (IS_LAST_GROUP)
			{
				((HarnakUndergroundRuins) r).increaseSecondRoomGroup();
			}
		}
	}
	
	/**
	 * Method onEvtTimer.
	 * @param timerId int
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		if (!isActive())
		{
			return;
		}
		if (timerId == 1)
		{
			if (MSG2_ID > 0)
			{
				Functions.npcSayInRange(getActor(), 1500, NpcString.valueOf(MSG2_ID));
			}
		}
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
}
