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

public class Q10270_BirthOfTheSeed extends Quest implements ScriptFile
{
	// Npcs
	private static final int PLENOS = 32563;
	private static final int ARTIUS = 32559;
	private static final int LELIKIA = 32567;
	private static final int GINBY = 32566;
	// Items
	private static final int Yehan_Klodekus_Badge = 13868;
	private static final int Yehan_Klanikus_Badge = 13869;
	private static final int Lich_Crystal = 13870;
	// Monsters
	private static final int Yehan_Klodekus = 25665;
	private static final int Yehan_Klanikus = 25666;
	private static final int Cohemenes = 25634;
	
	public Q10270_BirthOfTheSeed()
	{
		super(true);
		addStartNpc(PLENOS);
		addTalkId(PLENOS, ARTIUS, LELIKIA, GINBY);
		addKillId(Yehan_Klodekus, Yehan_Klanikus, Cohemenes);
		addQuestItem(Yehan_Klodekus_Badge, Yehan_Klanikus_Badge, Lich_Crystal);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "take":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "plenos_q10270_2.htm";
				}
				break;
			
			case "took_mission":
				if (cond == 1)
				{
					qs.setCond(2);
					htmltext = "artius_q10270_3.htm";
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "hand_over":
				if (cond == 2)
				{
					qs.takeItems(Yehan_Klodekus_Badge, -1);
					qs.takeItems(Yehan_Klanikus_Badge, -1);
					qs.takeItems(Lich_Crystal, -1);
					htmltext = "artius_q10270_6.htm";
				}
				break;
			
			case "artius_q10270_7.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "lelika":
				if (cond == 3)
				{
					qs.setCond(4);
					htmltext = "artius_q10270_9.htm";
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "lelikia_q10270_2.htm":
				if (cond == 4)
				{
					qs.setCond(5);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "reward":
				if (cond == 5)
				{
					htmltext = "artius_q10270_11.htm";
					qs.giveItems(ADENA_ID, 236510);
					qs.addExpAndSp(1109665, 1229015);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case PLENOS:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 75)
					{
						htmltext = "plenos_q10270_1.htm";
					}
					else
					{
						htmltext = "plenos_q10270_1a.htm";
						qs.exitCurrentQuest(true);
					}
				}
				break;
			
			case ARTIUS:
				switch (cond)
				{
					case 1:
						htmltext = "artius_q10270_1.htm";
						break;
					
					case 2:
						if ((qs.getQuestItemsCount(Yehan_Klodekus_Badge) == 0) || (qs.getQuestItemsCount(Yehan_Klanikus_Badge) == 0) || (qs.getQuestItemsCount(Lich_Crystal) == 0))
						{
							htmltext = "artius_q10270_4.htm";
						}
						else if ((qs.getQuestItemsCount(Yehan_Klodekus_Badge) == 1) && (qs.getQuestItemsCount(Yehan_Klanikus_Badge) == 1) && (qs.getQuestItemsCount(Lich_Crystal) == 1))
						{
							htmltext = "artius_q10270_5.htm";
						}
						break;
					
					case 3:
						htmltext = "artius_q10270_8.htm";
						break;
					
					case 5:
						htmltext = "artius_q10270_10.htm";
						break;
				}
				break;
			
			case GINBY:
				if (cond == 4)
				{
					htmltext = "ginby_q10270_1.htm";
				}
				break;
			
			case LELIKIA:
				if (cond == 4)
				{
					htmltext = "lelikia_q10270_1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			switch (npc.getId())
			{
				case Yehan_Klodekus:
					if (qs.getQuestItemsCount(Yehan_Klodekus_Badge) < 1)
					{
						qs.giveItems(Yehan_Klodekus_Badge, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
				
				case Yehan_Klanikus:
					if (qs.getQuestItemsCount(Yehan_Klanikus_Badge) < 1)
					{
						qs.giveItems(Yehan_Klanikus_Badge, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
				
				case Cohemenes:
					if (qs.getQuestItemsCount(Lich_Crystal) < 1)
					{
						qs.giveItems(Lich_Crystal, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
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
