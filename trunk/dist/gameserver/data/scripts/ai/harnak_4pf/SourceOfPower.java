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

import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SourceOfPower extends DefaultAI
{
	/**
	 * Field SKILL_ID. (value is 14625)
	 */
	private static final int SKILL_ID = 14625;
	/**
	 * Field LIGHT_HEAL_ID. (value is 14736)
	 */
	private static final int LIGHT_HEAL_ID = 14736;
	/**
	 * Field MSG1.
	 */
	private static final NpcString MSG1 = NpcString.I_HERMUNKUS_GIVE_MY_POWER_TO_THOSE_WHO_FIGHT_FOR_ME;
	/**
	 * Field MSG2.
	 */
	private static final NpcString MSG2 = NpcString.THOUGH_SMALL_THIS_POWER_WILL_HELP_YOU_GREATLY;
	/**
	 * Field controlNpc.
	 */
	private final boolean controlNpc;
	/**
	 * Field useLightHeal.
	 */
	private final boolean useLightHeal;
	/**
	 * Field firstCast.
	 */
	private boolean firstCast;
	
	/**
	 * Constructor for SourceOfPower.
	 * @param actor NpcInstance
	 */
	public SourceOfPower(NpcInstance actor)
	{
		super(actor);
		controlNpc = actor.getParameter("ControlNpc", false);
		useLightHeal = actor.getParameter("useLightHeal", false);
		firstCast = true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final List<Player> players = World.getAroundPlayers(getActor(), 300, 300);
		if (!players.isEmpty())
		{
			final Player p = players.get(0);
			final Skill skill;
			if (!useLightHeal)
			{
				skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
				addTaskCast(p, skill);
				if (firstCast)
				{
					addTimer(1, 7000);
					firstCast = false;
					getActor().broadcastPacket(new ExShowScreenMessage(MSG1, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 1, true, 0));
					if (controlNpc)
					{
						Functions.npcSayToPlayer(getActor(), p, NpcString.RRECEIVE_THIS_POWER_FROM_THE_ANCIENT_GIANT, ChatType.TELL);
						Functions.npcSayToPlayer(getActor(), p, NpcString.USE_THIS_NEW_POWER_WHEN_THE_TIME_IS_RIGHT, ChatType.TELL);
					}
				}
				else
				{
					getActor().broadcastPacket(new ExShowScreenMessage(MSG2, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 1, true, 0));
				}
			}
			else
			{
				if (firstCast)
				{
					addTimer(2, 100);
					firstCast = false;
				}
			}
		}
		return super.thinkActive();
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
			getActor().deleteMe();
		}
		else if (timerId == 2)
		{
			final List<Player> players = World.getAroundPlayers(getActor(), 500, 300);
			if (!players.isEmpty())
			{
				final Player p = players.get(0);
				final Skill skill = SkillTable.getInstance().getInfo(LIGHT_HEAL_ID, 1);
				addTaskCast(p, skill);
				addTimer(2, 3500);
			}
		}
	}
}
