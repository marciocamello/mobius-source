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

public class Q00117_TheOceanOfDistantStars extends Quest implements ScriptFile
{
	// Npcs
	private static final int Abey = 32053;
	private static final int GhostEngineer = 32055;
	private static final int Obi = 32052;
	private static final int GhostEngineer2 = 32054;
	private static final int Box = 32076;
	// Monsters
	private static final int BanditWarrior = 22023;
	private static final int BanditInspector = 22024;
	// Items
	private static final int BookOfGreyStar = 8495;
	private static final int EngravedHammer = 8488;
	
	public Q00117_TheOceanOfDistantStars()
	{
		super(false);
		addStartNpc(Abey);
		addTalkId(GhostEngineer, Obi, Box, GhostEngineer2);
		addKillId(BanditWarrior, BanditInspector);
		addQuestItem(BookOfGreyStar, EngravedHammer);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "railman_abu_q0117_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "ghost_of_railroadman2_q0117_0201.htm":
				qs.setCond(2);
				break;
			
			case "railman_obi_q0117_0301.htm":
				qs.setCond(3);
				break;
			
			case "railman_abu_q0117_0401.htm":
				qs.setCond(4);
				break;
			
			case "q_box_of_railroad_q0117_0501.htm":
				qs.setCond(5);
				qs.giveItems(EngravedHammer, 1);
				break;
			
			case "railman_abu_q0117_0601.htm":
				qs.setCond(6);
				break;
			
			case "railman_obi_q0117_0701.htm":
				qs.setCond(7);
				break;
			
			case "railman_obi_q0117_0801.htm":
				qs.takeItems(BookOfGreyStar, -1);
				qs.setCond(9);
				break;
			
			case "ghost_of_railroadman2_q0117_0901.htm":
				qs.takeItems(EngravedHammer, -1);
				qs.setCond(10);
				break;
			
			case "ghost_of_railroadman_q0117_1002.htm":
				qs.giveItems(ADENA_ID, 17647, true);
				qs.addExpAndSp(107387, 7369);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Abey:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 39)
						{
							htmltext = "railman_abu_q0117_0101.htm";
						}
						else
						{
							htmltext = "railman_abu_q0117_0103.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 3:
						htmltext = "railman_abu_q0117_0301.htm";
						break;
					
					case 5:
						if (qs.getQuestItemsCount(EngravedHammer) > 0)
						{
							htmltext = "railman_abu_q0117_0501.htm";
						}
						break;
					
					case 6:
						if (qs.getQuestItemsCount(EngravedHammer) > 0)
						{
							htmltext = "railman_abu_q0117_0601.htm";
						}
						break;
				}
				break;
			
			case GhostEngineer:
				if (cond == 1)
				{
					htmltext = "ghost_of_railroadman2_q0117_0101.htm";
				}
				else if ((cond == 9) && (qs.getQuestItemsCount(EngravedHammer) > 0))
				{
					htmltext = "ghost_of_railroadman2_q0117_0801.htm";
				}
				break;
			
			case Obi:
				switch (cond)
				{
					case 2:
						htmltext = "railman_obi_q0117_0201.htm";
						break;
					
					case 6:
						if (qs.getQuestItemsCount(EngravedHammer) > 0)
						{
							htmltext = "railman_obi_q0117_0601.htm";
						}
						break;
					
					case 7:
						if (qs.getQuestItemsCount(EngravedHammer) > 0)
						{
							htmltext = "railman_obi_q0117_0701.htm";
						}
						break;
					
					case 8:
						if (qs.getQuestItemsCount(BookOfGreyStar) > 0)
						{
							htmltext = "railman_obi_q0117_0704.htm";
						}
						break;
				}
				break;
			
			case Box:
				if (cond == 4)
				{
					htmltext = "q_box_of_railroad_q0117_0401.htm";
				}
				break;
			
			case GhostEngineer2:
				if (cond == 10)
				{
					htmltext = "ghost_of_railroadman_q0117_0901.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 7) && Rnd.chance(30))
		{
			if (qs.getQuestItemsCount(BookOfGreyStar) < 1)
			{
				qs.giveItems(BookOfGreyStar, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			
			qs.setCond(8);
			qs.setState(STARTED);
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
