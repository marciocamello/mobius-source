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

public class Q00623_TheFinestFood extends Quest implements ScriptFile
{
	// Npc
	public static final int JEREMY = 31521;
	// Monsters
	public static final int HOT_SPRINGS_BUFFALO = 21315;
	public static final int HOT_SPRINGS_FLAVA = 21316;
	public static final int HOT_SPRINGS_ANTELOPE = 21318;
	// Items
	public static final int LEAF_OF_FLAVA = 7199;
	public static final int BUFFALO_MEAT = 7200;
	public static final int ANTELOPE_HORN = 7201;
	
	public Q00623_TheFinestFood()
	{
		super(true);
		addStartNpc(JEREMY);
		addTalkId(JEREMY);
		addKillId(HOT_SPRINGS_BUFFALO, HOT_SPRINGS_FLAVA, HOT_SPRINGS_ANTELOPE);
		addQuestItem(BUFFALO_MEAT, LEAF_OF_FLAVA, ANTELOPE_HORN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accept":
				htmltext = "jeremy_q0623_0104.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "623_3":
				htmltext = "jeremy_q0623_0201.htm";
				qs.takeItems(LEAF_OF_FLAVA, -1);
				qs.takeItems(BUFFALO_MEAT, -1);
				qs.takeItems(ANTELOPE_HORN, -1);
				qs.giveItems(ADENA_ID, 73000);
				qs.addExpAndSp(230000, 18250);
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
		int id = qs.getState();
		if (id == CREATED)
		{
			qs.setCond(0);
		}
		
		if (summ(qs) >= 300)
		{
			qs.setCond(2);
		}
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 71)
				{
					htmltext = "jeremy_q0623_0101.htm";
				}
				else
				{
					htmltext = "jeremy_q0623_0103.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (summ(qs) < 300)
				{
					htmltext = "jeremy_q0623_0106.htm";
				}
				break;
			
			case 2:
				if (summ(qs) >= 300)
				{
					htmltext = "jeremy_q0623_0105.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			switch (npc.getId())
			{
				case HOT_SPRINGS_BUFFALO:
					if (qs.getQuestItemsCount(BUFFALO_MEAT) < 100)
					{
						qs.giveItems(BUFFALO_MEAT, 1);
						
						if (qs.getQuestItemsCount(BUFFALO_MEAT) == 100)
						{
							if (summ(qs) >= 300)
							{
								qs.setCond(2);
							}
							
							qs.playSound(SOUND_MIDDLE);
						}
						else
						{
							qs.playSound(SOUND_ITEMGET);
						}
					}
					break;
				
				case HOT_SPRINGS_FLAVA:
					if (qs.getQuestItemsCount(LEAF_OF_FLAVA) < 100)
					{
						qs.giveItems(LEAF_OF_FLAVA, 1);
						
						if (qs.getQuestItemsCount(LEAF_OF_FLAVA) == 100)
						{
							if (summ(qs) >= 300)
							{
								qs.setCond(2);
							}
							
							qs.playSound(SOUND_MIDDLE);
						}
						else
						{
							qs.playSound(SOUND_ITEMGET);
						}
					}
					break;
				
				case HOT_SPRINGS_ANTELOPE:
					if (qs.getQuestItemsCount(ANTELOPE_HORN) < 100)
					{
						qs.giveItems(ANTELOPE_HORN, 1);
						
						if (qs.getQuestItemsCount(ANTELOPE_HORN) == 100)
						{
							if (summ(qs) >= 300)
							{
								qs.setCond(2);
							}
							
							qs.playSound(SOUND_MIDDLE);
						}
						else
						{
							qs.playSound(SOUND_ITEMGET);
						}
					}
					break;
			}
		}
		
		return null;
	}
	
	private long summ(QuestState qs)
	{
		return qs.getQuestItemsCount(LEAF_OF_FLAVA) + qs.getQuestItemsCount(BUFFALO_MEAT) + qs.getQuestItemsCount(ANTELOPE_HORN);
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
