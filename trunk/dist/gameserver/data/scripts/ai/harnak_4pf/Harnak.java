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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;
import quests._10338_SeizeYourDestiny;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Harnak extends Fighter
{
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
	 * Field firstMsg.
	 */
	private boolean firstMsg = false;
	/**
	 * Field secondMsg.
	 */
	private boolean secondMsg = false;
	/**
	 * Field thirdMsg.
	 */
	private boolean thirdMsg = false;
	/**
	 * Field sealLaunched.
	 */
	private boolean sealLaunched = false;
	/**
	 * Field seal_active.
	 */
	private int seal_active = 0;
	
	/**
	 * Constructor for Harnak.
	 * @param actor NpcInstance
	 */
	public Harnak(NpcInstance actor)
	{
		super(actor);
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
		if (event.equalsIgnoreCase("SEAL_ACTIVATED"))
		{
			seal_active++;
			if (seal_active == 2)
			{
				final Reflection r = getActor().getReflection();
				if (!(r instanceof HarnakUndergroundRuins))
				{
					return;
				}
				for (Player p : r.getPlayers())
				{
					QuestState st = p.getQuestState(_10338_SeizeYourDestiny.class);
					if (st != null)
					{
						st.setCond(3);
						st.playSound(Quest.SOUND_MIDDLE);
					}
				}
				((HarnakUndergroundRuins) r).successEndInstance();
			}
		}
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
		if (!firstMsg)
		{
			firstMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.FREE_ME_FROM_THIS_BINDING_OF_LIGHT, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if (!secondMsg && (getActor().getCurrentHpPercents() <= 80.0))
		{
			secondMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.DESTROY_THE_GHOST_OF_HARNAK_THIS_CORRUPTED_CREATURE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if (!thirdMsg && (getActor().getCurrentHpPercents() <= 60.0))
		{
			thirdMsg = true;
			getActor().broadcastPacket(new ExShowScreenMessage(NpcString.FREE_ME_AND_I_PROMISE_YOU_THE_POWER_OF_GIANTS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		else if (!sealLaunched && (getActor().getCurrentHpPercents() <= 50.0))
		{
			sealLaunched = true;
			final Reflection r = getActor().getReflection();
			if (!(r instanceof HarnakUndergroundRuins))
			{
				return;
			}
			((HarnakUndergroundRuins) r).startLastStage();
		}
		if (Rnd.chance(10))
		{
			int SKILL_ID = SKILL_IDS[0];
			if (getActor().getCurrentHpPercents() < 90)
			{
				SKILL_ID = SKILL_IDS[1];
			}
			else if (getActor().getCurrentHpPercents() < 75)
			{
				SKILL_ID = SKILL_IDS[2];
			}
			final Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			skill.getEffects(getActor(), getActor(), false, false);
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
		((HarnakUndergroundRuins) r).successEndInstance();
	}
}
