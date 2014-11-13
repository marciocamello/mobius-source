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

public class Q00298_LizardmensConspiracy extends Quest implements ScriptFile
{
	// Npcs
	private static final int PRAGA = 30333;
	private static final int ROHMER = 30344;
	// Items
	private static final int REPORT = 7182;
	private static final int SHINING_GEM = 7183;
	private static final int SHINING_RED_GEM = 7184;
	// Monsters
	private static final int MAILLE_LIZARDMAN_WARRIOR = 20922;
	private static final int MAILLE_LIZARDMAN_SHAMAN = 20923;
	private static final int MAILLE_LIZARDMAN_MATRIARCH = 20924;
	private static final int POISON_ARANEID = 20926;
	private static final int KING_OF_THE_ARANEID = 20927;
	private static final int[][] MobsTable =
	{
		{
			MAILLE_LIZARDMAN_WARRIOR,
			SHINING_GEM
		},
		{
			MAILLE_LIZARDMAN_SHAMAN,
			SHINING_GEM
		},
		{
			MAILLE_LIZARDMAN_MATRIARCH,
			SHINING_GEM
		},
		{
			POISON_ARANEID,
			SHINING_RED_GEM
		},
		{
			KING_OF_THE_ARANEID,
			SHINING_RED_GEM
		}
	};
	
	public Q00298_LizardmensConspiracy()
	{
		super(false);
		addStartNpc(PRAGA);
		addTalkId(PRAGA);
		addTalkId(ROHMER);
		addQuestItem(REPORT, SHINING_GEM, SHINING_RED_GEM);
		for (int[] element : MobsTable)
		{
			addKillId(element[0]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "guard_praga_q0298_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.giveItems(REPORT, 1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "magister_rohmer_q0298_0201.htm":
				qs.takeItems(REPORT, -1);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "magister_rohmer_q0298_0301.htm":
				if ((qs.getQuestItemsCount(SHINING_GEM) + qs.getQuestItemsCount(SHINING_RED_GEM)) > 99)
				{
					qs.takeItems(SHINING_GEM, -1);
					qs.takeItems(SHINING_RED_GEM, -1);
					qs.addExpAndSp(0, 42000);
					qs.exitCurrentQuest(true);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case PRAGA:
				if (cond < 1)
				{
					if (qs.getPlayer().getLevel() < 25)
					{
						htmltext = "guard_praga_q0298_0102.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "guard_praga_q0298_0101.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "guard_praga_q0298_0105.htm";
				}
				break;
			
			case ROHMER:
				if (cond < 1)
				{
					htmltext = "magister_rohmer_q0298_0202.htm";
				}
				else if (cond == 1)
				{
					htmltext = "magister_rohmer_q0298_0101.htm";
				}
				else if ((cond == 2) | ((qs.getQuestItemsCount(SHINING_GEM) + qs.getQuestItemsCount(SHINING_RED_GEM)) < 100))
				{
					htmltext = "magister_rohmer_q0298_0204.htm";
					qs.setCond(2);
				}
				else if ((cond == 3) && ((qs.getQuestItemsCount(SHINING_GEM) + qs.getQuestItemsCount(SHINING_RED_GEM)) > 99))
				{
					htmltext = "magister_rohmer_q0298_0203.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int rand = Rnd.get(10);
		
		if (qs.getCond() == 2)
		{
			for (int[] element : MobsTable)
			{
				if ((npcId == element[0]) && (rand < 6) && (qs.getQuestItemsCount(element[1]) < 50))
				{
					if ((rand < 2) && (element[1] == SHINING_GEM))
					{
						qs.giveItems(element[1], 2);
					}
					else
					{
						qs.giveItems(element[1], 1);
					}
					
					if ((qs.getQuestItemsCount(SHINING_GEM) + qs.getQuestItemsCount(SHINING_RED_GEM)) > 99)
					{
						qs.setCond(3);
						qs.playSound(SOUND_MIDDLE);
					}
					else
					{
						qs.playSound(SOUND_ITEMGET);
					}
				}
			}
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
