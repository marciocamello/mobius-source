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

public class Q00032_AnObviousLie extends Quest implements ScriptFile
{
	// Npcs
	private final static int MAXIMILIAN = 30120;
	private final static int GENTLER = 30094;
	private final static int MIKI_THE_CAT = 31706;
	// Monster
	private final static int ALLIGATOR = 20135;
	// Items
	private final static int MAP = 7165;
	private final static int MEDICINAL_HERB = 7166;
	private final static int SPIRIT_ORES = 3031;
	private final static int THREAD = 1868;
	private final static int SUEDE = 1866;
	private final static int RACCOON_EAR = 7680;
	private final static int CAT_EAR = 6843;
	private final static int RABBIT_EAR = 7683;
	// Other
	private final static int CHANCE_FOR_DROP = 30;
	
	public Q00032_AnObviousLie()
	{
		super(false);
		addStartNpc(MAXIMILIAN);
		addTalkId(MAXIMILIAN, GENTLER, MIKI_THE_CAT);
		addKillId(ALLIGATOR);
		addQuestItem(MEDICINAL_HERB, MAP);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30120-1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30094-1.htm":
				qs.giveItems(MAP, 1);
				qs.setCond(2);
				break;
			
			case "31706-1.htm":
				qs.takeItems(MAP, 1);
				qs.setCond(3);
				break;
			
			case "30094-4.htm":
				if (qs.getQuestItemsCount(MEDICINAL_HERB) > 19)
				{
					qs.takeItems(MEDICINAL_HERB, 20);
					qs.setCond(5);
				}
				else
				{
					htmltext = "You don't have enough materials";
					qs.setCond(3);
				}
				break;
			
			case "30094-7.htm":
				if (qs.getQuestItemsCount(SPIRIT_ORES) >= 500)
				{
					qs.takeItems(SPIRIT_ORES, 500);
					qs.setCond(6);
				}
				else
				{
					htmltext = "You don't have enough materials";
				}
				break;
			
			case "31706-4.htm":
				if (qs.getCond() == 6)
				{
					qs.setCond(7);
				}
				break;
			
			case "30094-10.htm":
				if (qs.getCond() == 7)
				{
					qs.setCond(8);
				}
				break;
			
			case "30094-13.htm":
				if ((qs.getQuestItemsCount(THREAD) < 1000) || (qs.getQuestItemsCount(SUEDE) < 500))
				{
					htmltext = "You don't have enough materials";
				}
				break;
			
			case "cat":
			case "racoon":
				if ((qs.getCond() == 8) && (qs.getQuestItemsCount(THREAD) >= 1000) && (qs.getQuestItemsCount(SUEDE) >= 500))
				{
					qs.takeItems(THREAD, 1000);
					qs.takeItems(SUEDE, 500);
					
					if (event.equals("cat"))
					{
						qs.giveItems(CAT_EAR, 1);
					}
					else if (event.equals("racoon"))
					{
						qs.giveItems(RACCOON_EAR, 1);
					}
					else if (event.equals("rabbit"))
					{
						qs.giveItems(RABBIT_EAR, 1);
					}
					
					qs.unset("cond");
					qs.playSound(SOUND_FINISH);
					htmltext = "30094-14.htm";
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "You don't have enough materials";
				}
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
			case MAXIMILIAN:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 45)
					{
						htmltext = "30120-0.htm";
					}
					else
					{
						htmltext = "30120-0a.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "30120-2.htm";
				}
				break;
			
			case GENTLER:
				switch (cond)
				{
					case 1:
						htmltext = "30094-0.htm";
						break;
					
					case 2:
						htmltext = "30094-2.htm";
						break;
					
					case 3:
						htmltext = "30094-forgot.htm";
						break;
					
					case 4:
						htmltext = "30094-3.htm";
						break;
					
					case 5:
						if (qs.getQuestItemsCount(SPIRIT_ORES) < 500)
						{
							htmltext = "30094-5.htm";
						}
						else if (qs.getQuestItemsCount(SPIRIT_ORES) >= 500)
						{
							htmltext = "30094-6.htm";
						}
						break;
					
					case 6:
						htmltext = "30094-8.htm";
						break;
					
					case 7:
						htmltext = "30094-9.htm";
						break;
					
					case 8:
						if ((qs.getQuestItemsCount(THREAD) < 1000) || (qs.getQuestItemsCount(SUEDE) < 500))
						{
							htmltext = "30094-11.htm";
						}
						else if ((qs.getQuestItemsCount(THREAD) >= 1000) && (qs.getQuestItemsCount(SUEDE) >= 500))
						{
							htmltext = "30094-12.htm";
						}
						break;
				}
				break;
			
			case MIKI_THE_CAT:
				switch (cond)
				{
					case 2:
						htmltext = "31706-0.htm";
						break;
					
					case 3:
						htmltext = "31706-2.htm";
						break;
					
					case 6:
						htmltext = "31706-3.htm";
						break;
					
					case 7:
						htmltext = "31706-5.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		final long count = st.getQuestItemsCount(MEDICINAL_HERB);
		
		if (Rnd.chance(CHANCE_FOR_DROP) && (st.getCond() == 3) && (count < 20))
		{
			st.giveItems(MEDICINAL_HERB, 1);
			
			if (count == 19)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(4);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
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
