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

public class Q00033_MakeAPairOfDressShoes extends Quest implements ScriptFile
{
	// Npcs
	private final static int WOODLEY = 30838;
	private final static int IAN = 30164;
	private final static int LEIKAR = 31520;
	// Items
	private final static int LEATHER = 1882;
	private final static int THREAD = 1868;
	private final static int DRESS_SHOES_BOX = 7113;
	
	public Q00033_MakeAPairOfDressShoes()
	{
		super(false);
		addStartNpc(WOODLEY);
		addTalkId(WOODLEY, IAN, LEIKAR);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30838-1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31520-1.htm":
				qs.setCond(2);
				break;
			
			case "30838-3.htm":
				qs.setCond(3);
				break;
			
			case "30838-5.htm":
				if ((qs.getQuestItemsCount(LEATHER) >= 200) && (qs.getQuestItemsCount(THREAD) >= 600) && (qs.getQuestItemsCount(ADENA_ID) >= 200000))
				{
					qs.takeItems(LEATHER, 200);
					qs.takeItems(THREAD, 600);
					qs.takeItems(ADENA_ID, 200000);
					qs.setCond(4);
				}
				else
				{
					htmltext = "You don't have enough materials";
				}
				break;
			
			case "30164-1.htm":
				if (qs.getQuestItemsCount(ADENA_ID) >= 300000)
				{
					qs.takeItems(ADENA_ID, 300000);
					qs.setCond(5);
				}
				else
				{
					htmltext = "30164-havent.htm";
				}
				break;
			
			case "30838-7.htm":
				qs.giveItems(DRESS_SHOES_BOX, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
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
			case WOODLEY:
				switch (cond)
				{
					case 0:
						if (qs.getQuestItemsCount(DRESS_SHOES_BOX) == 0)
						{
							if (qs.getPlayer().getLevel() >= 60)
							{
								QuestState fwear = qs.getPlayer().getQuestState(Q00037_MakeFormalWear.class);
								
								if ((fwear != null) && (fwear.getCond() == 7))
								{
									htmltext = "30838-0.htm";
								}
								else
								{
									qs.exitCurrentQuest(true);
								}
							}
							else
							{
								htmltext = "30838-00.htm";
							}
						}
						break;
					
					case 1:
						htmltext = "30838-1.htm";
						break;
					
					case 2:
						htmltext = "30838-2.htm";
						break;
					
					case 3:
						if ((qs.getQuestItemsCount(LEATHER) >= 200) && (qs.getQuestItemsCount(THREAD) >= 600) && (qs.getQuestItemsCount(ADENA_ID) >= 200000))
						{
							htmltext = "30838-4.htm";
						}
						else if ((qs.getQuestItemsCount(LEATHER) < 200) || (qs.getQuestItemsCount(THREAD) < 600) || (qs.getQuestItemsCount(ADENA_ID) < 200000))
						{
							htmltext = "30838-4r.htm";
						}
						break;
					
					case 4:
						htmltext = "30838-5r.htm";
						break;
					
					case 5:
						htmltext = "30838-6.htm";
						break;
				}
				break;
			
			case LEIKAR:
				if (cond == 1)
				{
					htmltext = "31520-0.htm";
				}
				else if (cond == 2)
				{
					htmltext = "31520-1r.htm";
				}
				break;
			
			case IAN:
				if (cond == 4)
				{
					htmltext = "30164-0.htm";
				}
				else if (cond == 5)
				{
					htmltext = "30164-2.htm";
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
