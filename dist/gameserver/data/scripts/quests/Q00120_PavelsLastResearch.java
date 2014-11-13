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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00120_PavelsLastResearch extends Quest implements ScriptFile
{
	// Npcs
	private static final int Yumi = 32041;
	private static final int Weather1 = 32042;
	private static final int Weather2 = 32043;
	private static final int Weather3 = 32044;
	private static final int BookShelf = 32045;
	private static final int Stones = 32046;
	private static final int Wendy = 32047;
	// Items
	private static final int EarPhoenix = 6324;
	private static final int Report = 8058;
	private static final int Report2 = 8059;
	private static final int Enigma = 8060;
	private static final int Flower = 8290;
	private static final int Heart = 8291;
	private static final int Necklace = 8292;
	
	public Q00120_PavelsLastResearch()
	{
		super(false);
		addStartNpc(Stones);
		addTalkId(BookShelf, Stones, Weather1, Weather2, Weather3, Wendy, Yumi);
		addQuestItem(Report, Report2, Enigma, Flower, Heart, Necklace);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32041-03.htm":
				qs.setCond(3);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32041-04.htm":
				qs.setCond(4);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32041-12.htm":
				qs.setCond(8);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32041-16.htm":
				qs.setCond(16);
				qs.giveItems(Enigma, 1);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32041-22.htm":
				qs.setCond(17);
				qs.takeItems(Enigma, 1);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32041-32.htm":
				qs.takeItems(Necklace, 1);
				qs.giveItems(EarPhoenix, 1);
				qs.giveItems(ADENA_ID, 1030825);
				qs.addExpAndSp(4898650, 5099850);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "32042-06.htm":
				if (qs.getCond() == 10)
				{
					if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
					{
						qs.setCond(11);
						qs.set("talk", "0");
						qs.set("talk1", "0");
						qs.playSound("ItemSound.quest_middle");
					}
					else
					{
						htmltext = "32042-03.htm";
					}
				}
				break;
			
			case "32042-10.htm":
				if ((qs.getInt("talk") + qs.getInt("talk1") + qs.getInt("talk2")) == 3)
				{
					htmltext = "32042-14.htm";
				}
				break;
			
			case "32042-11.htm":
				if (qs.getInt("talk") == 0)
				{
					qs.set("talk", "1");
				}
				break;
			
			case "32042-12.htm":
				if (qs.getInt("talk1") == 0)
				{
					qs.set("talk1", "1");
				}
				break;
			
			case "32042-13.htm":
				if (qs.getInt("talk2") == 0)
				{
					qs.set("talk2", "1");
				}
				break;
			
			case "32042-15.htm":
				qs.setCond(12);
				qs.set("talk", "0");
				qs.set("talk1", "0");
				qs.set("talk2", "0");
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32043-06.htm":
				if (qs.getCond() == 17)
				{
					if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
					{
						qs.setCond(18);
						qs.set("talk", "0");
						qs.set("talk1", "0");
						qs.playSound("ItemSound.quest_middle");
					}
					else
					{
						htmltext = "32043-03.htm";
					}
				}
				break;
			
			case "32043-15.htm":
				if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
				{
					htmltext = "32043-29.htm";
				}
				break;
			
			case "32043-18.htm":
				if (qs.getInt("talk") == 1)
				{
					htmltext = "32043-21.htm";
				}
				break;
			
			case "32043-20.htm":
				qs.set("talk", "1");
				qs.playSound("AmbSound.ed_drone_02");
				break;
			
			case "32043-28.htm":
				qs.set("talk1", "1");
				break;
			
			case "32043-30.htm":
				qs.setCond(19);
				qs.set("talk", "0");
				qs.set("talk1", "0");
				break;
			
			case "32044-06.htm":
				if (qs.getCond() == 20)
				{
					if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
					{
						qs.setCond(21);
						qs.set("talk", "0");
						qs.set("talk1", "0");
						qs.playSound("ItemSound.quest_middle");
					}
					else
					{
						htmltext = "32044-03.htm";
					}
				}
				break;
			
			case "32044-08.htm":
				if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
				{
					htmltext = "32044-11.htm";
				}
				break;
			
			case "32044-09.htm":
				if (qs.getInt("talk") == 0)
				{
					qs.set("talk", "1");
				}
				break;
			
			case "32044-10.htm":
				if (qs.getInt("talk1") == 0)
				{
					qs.set("talk1", "1");
				}
				break;
			
			case "32044-17.htm":
				qs.setCond(22);
				qs.set("talk", "0");
				qs.set("talk1", "0");
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32045-02.htm":
				qs.setCond(15);
				qs.playSound("ItemSound.quest_middle");
				qs.giveItems(Report, 1);
				Player player = qs.getPlayer();
				if (player != null)
				{
					npc.broadcastPacket(new MagicSkillUse(npc, player, 5073, 5, 1500, 0));
				}
				break;
			
			case "32046-04.htm":
			case "32046-05.htm":
				qs.exitCurrentQuest(true);
				break;
			
			case "32046-06.htm":
				if (qs.getPlayer().getLevel() >= 50)
				{
					qs.playSound("ItemSound.quest_accept");
					qs.setCond(1);
					qs.setState(STARTED);
				}
				else
				{
					htmltext = "32046-00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "32046-08.htm":
				qs.setCond(2);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32046-12.htm":
				qs.setCond(6);
				qs.playSound("ItemSound.quest_middle");
				qs.giveItems(Flower, 1);
				break;
			
			case "32046-22.htm":
				qs.setCond(10);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32046-29.htm":
				qs.setCond(13);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32046-35.htm":
				qs.setCond(20);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32046-38.htm":
				qs.setCond(23);
				qs.playSound("ItemSound.quest_middle");
				qs.giveItems(Heart, 1);
				break;
			
			case "32047-06.htm":
				qs.setCond(5);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32047-10.htm":
				qs.setCond(7);
				qs.playSound("ItemSound.quest_middle");
				qs.takeItems(Flower, 1);
				break;
			
			case "32047-15.htm":
				qs.setCond(9);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32047-18.htm":
				qs.setCond(14);
				qs.playSound("ItemSound.quest_middle");
				break;
			
			case "32047-26.htm":
				qs.setCond(24);
				qs.playSound("ItemSound.quest_middle");
				qs.takeItems(Heart, 1);
				break;
			
			case "32047-32.htm":
				qs.setCond(25);
				qs.playSound("ItemSound.quest_middle");
				qs.giveItems(Necklace, 1);
				break;
			
			case "w1_1":
				qs.set("talk", "1");
				htmltext = "32042-04.htm";
				break;
			
			case "w1_2":
				qs.set("talk1", "1");
				htmltext = "32042-05.htm";
				break;
			
			case "w2_1":
				qs.set("talk", "1");
				htmltext = "32043-04.htm";
				break;
			
			case "w2_2":
				qs.set("talk1", "1");
				htmltext = "32043-05.htm";
				break;
			
			case "w3_1":
				qs.set("talk", "1");
				htmltext = "32044-04.htm";
				break;
			
			case "w3_2":
				qs.set("talk1", "1");
				htmltext = "32044-05.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Stones:
				final QuestState q = qs.getPlayer().getQuestState(Q00114_ResurrectionOfAnOldManager.class);
				if (q == null)
				{
					return htmltext;
				}
				
				switch (cond)
				{
					case 0:
						if ((qs.getPlayer().getLevel() >= 70) && q.isCompleted())
						{
							htmltext = "32046-01.htm";
						}
						else
						{
							htmltext = "32046-00.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
						htmltext = "32046-06.htm";
						break;
					
					case 2:
						htmltext = "32046-09.htm";
						break;
					
					case 5:
						htmltext = "32046-10.htm";
						break;
					
					case 6:
						htmltext = "32046-13.htm";
						break;
					
					case 9:
						htmltext = "32046-14.htm";
						break;
					
					case 10:
						htmltext = "32046-23.htm";
						break;
					
					case 12:
						htmltext = "32046-26.htm";
						break;
					
					case 13:
						htmltext = "32046-30.htm";
						break;
					
					case 19:
						htmltext = "32046-31.htm";
						break;
					
					case 20:
						htmltext = "32046-36.htm";
						break;
					
					case 22:
						htmltext = "32046-37.htm";
						break;
					
					case 23:
						htmltext = "32046-39.htm";
						break;
				}
				break;
			
			case Wendy:
				switch (cond)
				{
					case 2:
					case 3:
					case 4:
						htmltext = "32047-01.htm";
						break;
					
					case 5:
						htmltext = "32047-07.htm";
						break;
					
					case 6:
						htmltext = "32047-08.htm";
						break;
					
					case 7:
						htmltext = "32047-11.htm";
						break;
					
					case 8:
						htmltext = "32047-12.htm";
						break;
					
					case 9:
						htmltext = "32047-15.htm";
						break;
					
					case 13:
						htmltext = "32047-16.htm";
						break;
					
					case 14:
						htmltext = "32047-19.htm";
						break;
					
					case 15:
						htmltext = "32047-20.htm";
						break;
					
					case 23:
						htmltext = "32047-21.htm";
						break;
					
					case 24:
						htmltext = "32047-26.htm";
						break;
					
					case 25:
						htmltext = "32047-33.htm";
						break;
				}
				break;
			
			case Yumi:
				switch (cond)
				{
					case 2:
						htmltext = "32041-01.htm";
						break;
					
					case 3:
						htmltext = "32041-05.htm";
						break;
					
					case 4:
						htmltext = "32041-06.htm";
						break;
					
					case 7:
						htmltext = "32041-07.htm";
						break;
					
					case 8:
						htmltext = "32041-13.htm";
						break;
					
					case 15:
						htmltext = "32041-14.htm";
						break;
					
					case 16:
						if (qs.getQuestItemsCount(Report2) == 0)
						{
							htmltext = "32041-17.htm";
						}
						else
						{
							htmltext = "32041-18.htm";
						}
						break;
					
					case 17:
						htmltext = "32041-22.htm";
						break;
					
					case 25:
						htmltext = "32041-26.htm";
						break;
				}
				break;
			
			case Weather1:
				switch (cond)
				{
					case 10:
						htmltext = "32042-01.htm";
						break;
					
					case 11:
						if ((qs.getInt("talk") + qs.getInt("talk1") + qs.getInt("talk2")) == 3)
						{
							htmltext = "32042-14.htm";
						}
						else
						{
							htmltext = "32042-06.htm";
						}
						break;
					
					case 12:
						htmltext = "32042-15.htm";
						break;
				}
				break;
			
			case Weather2:
				switch (cond)
				{
					case 17:
						htmltext = "32043-01.htm";
						break;
					
					case 18:
						if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
						{
							htmltext = "32043-29.htm";
						}
						else
						{
							htmltext = "32043-06.htm";
						}
						break;
					
					case 19:
						htmltext = "32043-30.htm";
						break;
				}
				break;
			
			case Weather3:
				switch (cond)
				{
					case 20:
						htmltext = "32044-01.htm";
						break;
					
					case 21:
						htmltext = "32044-06.htm";
						break;
					
					case 22:
						htmltext = "32044-18.htm";
						break;
				}
				break;
			
			case BookShelf:
				if (cond == 14)
				{
					htmltext = "32045-01.htm";
				}
				else if (cond == 15)
				{
					htmltext = "32045-03.htm";
				}
				break;
		}
		
		return htmltext;
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
