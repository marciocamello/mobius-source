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

public class Q00035_FindGlitteringJewelry extends Quest implements ScriptFile
{
	// Npcs
	private final static int ELLIE = 30091;
	private final static int FELTON = 30879;
	// Monster
	private final static int ALIGATOR = 20135;
	// Items
	private final static int ROUGH_JEWEL = 7162;
	private final static int ORIHARUKON = 1893;
	private final static int SILVER_NUGGET = 1873;
	private final static int THONS = 4044;
	private final static int JEWEL_BOX = 7077;
	
	public Q00035_FindGlitteringJewelry()
	{
		super(false);
		addStartNpc(ELLIE);
		addTalkId(ELLIE, FELTON);
		addKillId(ALIGATOR);
		addQuestItem(ROUGH_JEWEL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "30091-1.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30879-1.htm":
				if (cond == 1)
				{
					qs.setCond(2);
				}
				break;
			
			case "30091-3.htm":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(ROUGH_JEWEL) == 10)
					{
						qs.takeItems(ROUGH_JEWEL, -1);
						qs.setCond(4);
					}
					else
					{
						htmltext = "30091-hvnore.htm";
					}
				}
				break;
			
			case "30091-5.htm":
				if (cond == 4)
				{
					if ((qs.getQuestItemsCount(ORIHARUKON) >= 5) && (qs.getQuestItemsCount(SILVER_NUGGET) >= 500) && (qs.getQuestItemsCount(THONS) >= 150))
					{
						qs.takeItems(ORIHARUKON, 5);
						qs.takeItems(SILVER_NUGGET, 500);
						qs.takeItems(THONS, 150);
						qs.giveItems(JEWEL_BOX, 1);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "30091-hvnmat-bug.htm";
					}
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
			case ELLIE:
				switch (cond)
				{
					case 0:
						if (qs.getQuestItemsCount(JEWEL_BOX) == 0)
						{
							if (qs.getPlayer().getLevel() >= 60)
							{
								QuestState fwear = qs.getPlayer().getQuestState(Q00037_MakeFormalWear.class);
								
								if ((fwear != null) && (fwear.getCond() == 6))
								{
									htmltext = "30091-0.htm";
								}
								else
								{
									qs.exitCurrentQuest(true);
								}
							}
							else
							{
								htmltext = "30091-6.htm";
								qs.exitCurrentQuest(true);
							}
						}
						break;
					
					case 1:
						htmltext = "30091-1r.htm";
						break;
					
					case 2:
						htmltext = "30091-1r2.htm";
						break;
					
					case 3:
						if (qs.getQuestItemsCount(ROUGH_JEWEL) == 10)
						{
							htmltext = "30091-2.htm";
						}
						break;
					
					case 4:
						if ((qs.getQuestItemsCount(ORIHARUKON) < 5) || (qs.getQuestItemsCount(SILVER_NUGGET) < 500) || (qs.getQuestItemsCount(THONS) < 150))
						{
							htmltext = "30091-hvnmat.htm";
						}
						else if ((qs.getQuestItemsCount(ORIHARUKON) >= 5) && (qs.getQuestItemsCount(SILVER_NUGGET) >= 500) && (qs.getQuestItemsCount(THONS) >= 150))
						{
							htmltext = "30091-4.htm";
						}
						break;
				}
				break;
			
			case FELTON:
				if (cond == 1)
				{
					htmltext = "30879-0.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30879-1r.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final long count = qs.getQuestItemsCount(ROUGH_JEWEL);
		
		if ((qs.getCond() == 2) && (count < 10))
		{
			qs.giveItems(ROUGH_JEWEL, 1);
			
			if (qs.getQuestItemsCount(ROUGH_JEWEL) == 10)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
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
