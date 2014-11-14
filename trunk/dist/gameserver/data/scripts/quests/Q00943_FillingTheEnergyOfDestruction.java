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
import lineage2.gameserver.utils.Util;

/**
 * @author GodWorld & Bonux
 */
public class Q00943_FillingTheEnergyOfDestruction extends Quest implements ScriptFile
{
	// NPCs
	private static final int SEED_TALISMAN_SUPERVISOR = 33715;
	// Monsters
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
	// Items
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
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "seed_talisman_manager_q0943_03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "seed_talisman_manager_q0943_08.htm":
				qs.giveItems(ENERGY_OF_DESTRUCTION, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isStarted())
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
			if (isAvailableFor(qs.getPlayer()))
			{
				if (qs.isNowAvailable())
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
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(RAID_BOSSES, npc.getId()))
		{
			qs.giveItems(CORE_OF_TWISTED_MAGIC, 1);
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(2);
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}