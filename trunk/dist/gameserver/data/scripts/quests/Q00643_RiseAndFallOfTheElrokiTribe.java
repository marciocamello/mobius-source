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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00643_RiseAndFallOfTheElrokiTribe extends Quest implements ScriptFile
{
	// Npcs
	private static final int SINGSING = 32106;
	private static final int KARAKAWEI = 32117;
	// Items
	private static final int BONES_OF_A_PLAINS_DINOSAUR = 8776;
	private static final int[] REWARDS =
	{
		8712,
		8713,
		8714,
		8715,
		8716,
		8717,
		8718,
		8719,
		8720,
		8721,
		8722
	};
	// Monsters
	private static final int[] PLAIN_DINOSAURS =
	{
		22208,
		22209,
		22210,
		22211,
		22212,
		22213,
		22221,
		22222,
		22226,
		22227,
		22742,
		22743,
		22744,
		22745
	};
	// Other
	private static final int DROP_CHANCE = 75;
	
	public Q00643_RiseAndFallOfTheElrokiTribe()
	{
		super(true);
		addStartNpc(SINGSING);
		addTalkId(KARAKAWEI);
		addQuestItem(BONES_OF_A_PLAINS_DINOSAUR);
		for (int npc : PLAIN_DINOSAURS)
		{
			addKillId(npc);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final long count = qs.getQuestItemsCount(BONES_OF_A_PLAINS_DINOSAUR);
		
		switch (event)
		{
			case "singsing_q0643_05.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "shaman_caracawe_q0643_06.htm":
				if (count >= 300)
				{
					qs.takeItems(BONES_OF_A_PLAINS_DINOSAUR, 300);
					qs.giveItems(REWARDS[Rnd.get(REWARDS.length)], 5, false);
				}
				else
				{
					htmltext = "shaman_caracawe_q0643_05.htm";
				}
				break;
			
			case "None":
				htmltext = null;
				break;
			
			case "Quit":
				htmltext = null;
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		
		if (qs.getCond() == 0)
		{
			if (qs.getPlayer().getLevel() >= 75)
			{
				htmltext = "singsing_q0643_01.htm";
			}
			else
			{
				htmltext = "singsing_q0643_04.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (qs.getState() == STARTED)
		{
			if (npcId == SINGSING)
			{
				final long count = qs.getQuestItemsCount(BONES_OF_A_PLAINS_DINOSAUR);
				
				if (count == 0)
				{
					htmltext = "singsing_q0643_08.htm";
				}
				else
				{
					htmltext = "singsing_q0643_08.htm";
					qs.takeItems(BONES_OF_A_PLAINS_DINOSAUR, -1);
					qs.giveItems(ADENA_ID, count * 1374, false);
				}
			}
			else if (npcId == KARAKAWEI)
			{
				htmltext = "shaman_caracawe_q0643_02.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.rollAndGive(BONES_OF_A_PLAINS_DINOSAUR, 1, DROP_CHANCE);
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
