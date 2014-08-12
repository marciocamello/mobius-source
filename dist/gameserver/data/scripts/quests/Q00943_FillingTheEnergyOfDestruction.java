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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author GodWorld & Bonux
 */
public class Q00943_FillingTheEnergyOfDestruction extends Quest implements ScriptFile
{
	// NPC's
	private static final int SEED_TALISMAN_SUPERVISOR = 33715;
	// Monster's
	private static final int[] RAID_BOSSES =
	{
		29195,
		29196,
		29212,
		29194,
		25779,
		25867,
		29213,
		29218,
		25825,
		29236,
		29238
	};
	// Item's
	private static final int CORE_OF_TWISTED_MAGIC = 35668;
	private static final int ENERGY_OF_DESTRUCTION = 35562;
	
	public Q00943_FillingTheEnergyOfDestruction()
	{
		super(false);
		addStartNpc(SEED_TALISMAN_SUPERVISOR);
		addTalkId(SEED_TALISMAN_SUPERVISOR);
		addKillId(RAID_BOSSES);
		addQuestItem(CORE_OF_TWISTED_MAGIC);
		addLevelCheck(90, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("seed_talisman_manager_q0943_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("seed_talisman_manager_q0943_08.htm"))
		{
			st.giveItems(ENERGY_OF_DESTRUCTION, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		String htmltext = "noquest";
		
		if (npcId == SEED_TALISMAN_SUPERVISOR)
		{
			if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "seed_talisman_manager_q0943_06.htm";
				}
				else if (cond == 2)
				{
					htmltext = "seed_talisman_manager_q0943_07.htm";
				}
			}
			else
			{
				if (isAvailableFor(st.getPlayer()))
				{
					if (st.isNowAvailable())
					{
						htmltext = "seed_talisman_manager_q0943_01.htm";
					}
					else
					{
						htmltext = "seed_talisman_manager_q0943_05.htm";
					}
				}
				else
				{
					htmltext = "seed_talisman_manager_q0943_04.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (ArrayUtils.contains(RAID_BOSSES, npcId))
		{
			if (cond == 1)
			{
				st.giveItems(CORE_OF_TWISTED_MAGIC, 1);
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
		//
	}
	
	@Override
	public void onReload()
	{
		//
	}
	
	@Override
	public void onShutdown()
	{
		//
	}
}