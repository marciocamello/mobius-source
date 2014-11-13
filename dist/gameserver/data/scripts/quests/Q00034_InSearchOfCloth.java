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

public class Q00034_InSearchOfCloth extends Quest implements ScriptFile
{
	// Npcs
	private final static int RADIA = 30088;
	private final static int RALFORD = 30165;
	private final static int VARAN = 30294;
	// Monster
	private final static int TRISALIM = 20560;
	// Items
	private final static int SPINNERET = 7528;
	private final static int SUEDE = 1866;
	private final static int THREAD = 1868;
	private final static int SPIDERSILK = 1493;
	private final static int MYSTERIOUS_CLOTH = 7076;
	
	public Q00034_InSearchOfCloth()
	{
		super(false);
		addStartNpc(RADIA);
		addTalkId(RADIA, RALFORD, VARAN, TRISALIM);
		addKillId(TRISALIM);
		addQuestItem(SPINNERET);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "30088-1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30294-1.htm":
				if (cond == 1)
				{
					qs.setCond(2);
				}
				break;
			
			case "30088-3.htm":
				if (cond == 2)
				{
					qs.setCond(3);
				}
				break;
			
			case "30165-1.htm":
				if (cond == 3)
				{
					qs.setCond(4);
				}
				break;
			
			case "30165-3.htm":
				if (cond == 5)
				{
					if (qs.getQuestItemsCount(SPINNERET) == 10)
					{
						qs.takeItems(SPINNERET, 10);
						qs.giveItems(SPIDERSILK, 1);
						qs.setCond(6);
					}
					else
					{
						htmltext = "30165-1r.htm";
					}
				}
				break;
			
			case "30088-5.htm":
				if (cond == 6)
				{
					if ((qs.getQuestItemsCount(SUEDE) >= 3000) && (qs.getQuestItemsCount(THREAD) >= 5000) && (qs.getQuestItemsCount(SPIDERSILK) == 1))
					{
						qs.takeItems(SUEDE, 3000);
						qs.takeItems(THREAD, 5000);
						qs.takeItems(SPIDERSILK, 1);
						qs.giveItems(MYSTERIOUS_CLOTH, 1);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "30088-havent.htm";
					}
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
			case RADIA:
				switch (cond)
				{
					case 0:
						if (qs.getQuestItemsCount(MYSTERIOUS_CLOTH) == 0)
						{
							if (qs.getPlayer().getLevel() >= 60)
							{
								QuestState fwear = qs.getPlayer().getQuestState(Q00037_MakeFormalWear.class);
								
								if ((fwear != null) && (fwear.getCond() == 6))
								{
									htmltext = "30088-0.htm";
								}
								else
								{
									qs.exitCurrentQuest(true);
								}
							}
							else
							{
								htmltext = "30088-6.htm";
							}
						}
						break;
					
					case 1:
						htmltext = "30088-1r.htm";
						break;
					
					case 2:
						htmltext = "30088-2.htm";
						break;
					
					case 3:
						htmltext = "30088-3r.htm";
						break;
					
					case 6:
						if ((qs.getQuestItemsCount(SUEDE) < 3000) || (qs.getQuestItemsCount(THREAD) < 5000) || (qs.getQuestItemsCount(SPIDERSILK) < 1))
						{
							htmltext = "30088-havent.htm";
						}
						else
						{
							htmltext = "30088-4.htm";
						}
						break;
				}
				break;
			
			case VARAN:
				if (cond == 1)
				{
					htmltext = "30294-0.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30294-1r.htm";
				}
				break;
			
			case RALFORD:
				switch (cond)
				{
					case 3:
						htmltext = "30165-0.htm";
						break;
					
					case 4:
						if (qs.getQuestItemsCount(SPINNERET) < 10)
						{
							htmltext = "30165-1r.htm";
						}
						break;
					
					case 5:
						htmltext = "30165-2.htm";
						break;
					
					case 6:
						if ((qs.getQuestItemsCount(SUEDE) < 3000) || (qs.getQuestItemsCount(THREAD) < 5000) || (qs.getQuestItemsCount(SPIDERSILK) < 1))
						{
							htmltext = "30165-3r.htm";
						}
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getQuestItemsCount(SPINNERET) < 10)
		{
			qs.giveItems(SPINNERET, 1);
			
			if (qs.getQuestItemsCount(SPINNERET) == 10)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(5);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
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
