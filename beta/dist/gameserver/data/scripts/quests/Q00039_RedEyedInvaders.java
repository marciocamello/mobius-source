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

public class Q00039_RedEyedInvaders extends Quest implements ScriptFile
{
	// Npcs
	private static final int BABENCO = 30334;
	private static final int BATHIS = 30332;
	// Monsters
	private static final int LIZARDMAN = 20919;
	private static final int LIZARDMAN_SCOUT = 20920;
	private static final int LIZARDMAN_GUARD = 20921;
	private static final int GIANT_ARANEID = 20925;
	// Items
	private static final int BBN = 7178;
	private static final int RBN = 7179;
	private static final int IP = 7180;
	private static final int GML = 7181;
	private static final int[] REWARDS =
	{
		6521,
		6529,
		6535
	};
	
	public Q00039_RedEyedInvaders()
	{
		super(false);
		addStartNpc(BABENCO);
		addTalkId(BABENCO, BATHIS);
		addKillId(LIZARDMAN, LIZARDMAN_SCOUT, LIZARDMAN_GUARD, GIANT_ARANEID);
		addQuestItem(new int[]
		{
			BBN,
			IP,
			RBN,
			GML
		});
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "guard_babenco_q0039_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "captain_bathia_q0039_0201.htm":
				qs.setCond(2);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "captain_bathia_q0039_0301.htm":
				if ((qs.getQuestItemsCount(BBN) == 100) && (qs.getQuestItemsCount(RBN) == 100))
				{
					qs.setCond(4);
					qs.takeItems(BBN, -1);
					qs.takeItems(RBN, -1);
					qs.playSound(SOUND_ACCEPT);
				}
				else
				{
					htmltext = "captain_bathia_q0039_0203.htm";
				}
				break;
			
			case "captain_bathia_q0039_0401.htm":
				if ((qs.getQuestItemsCount(IP) == 30) && (qs.getQuestItemsCount(GML) == 30))
				{
					qs.takeItems(IP, -1);
					qs.takeItems(GML, -1);
					qs.giveItems(REWARDS[0], 60);
					qs.giveItems(REWARDS[1], 1);
					qs.giveItems(REWARDS[2], 500);
					qs.addExpAndSp(62366, 2783);
					qs.setCond(0);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "captain_bathia_q0039_0304.htm";
				}
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
			case BABENCO:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() < 20)
					{
						htmltext = "guard_babenco_q0039_0102.htm";
						qs.exitCurrentQuest(true);
					}
					else if (qs.getPlayer().getLevel() >= 20)
					{
						htmltext = "guard_babenco_q0039_0101.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "guard_babenco_q0039_0105.htm";
				}
				break;
			
			case BATHIS:
				switch (cond)
				{
					case 1:
						htmltext = "captain_bathia_q0039_0101.htm";
						break;
					
					case 2:
						if ((qs.getQuestItemsCount(BBN) < 100) || (qs.getQuestItemsCount(RBN) < 100))
						{
							htmltext = "captain_bathia_q0039_0203.htm";
						}
						break;
					
					case 3:
						if ((qs.getQuestItemsCount(BBN) == 100) && (qs.getQuestItemsCount(RBN) == 100))
						{
							htmltext = "captain_bathia_q0039_0202.htm";
						}
						break;
					
					case 4:
						if ((qs.getQuestItemsCount(IP) < 30) || (qs.getQuestItemsCount(GML) < 30))
						{
							htmltext = "captain_bathia_q0039_0304.htm";
						}
						break;
					
					case 5:
						if ((qs.getQuestItemsCount(IP) == 30) && (qs.getQuestItemsCount(GML) == 30))
						{
							htmltext = "captain_bathia_q0039_0303.htm";
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
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if (cond == 2)
		{
			switch (npcId)
			{
				case LIZARDMAN:
				case LIZARDMAN_SCOUT:
					if (qs.getQuestItemsCount(BBN) <= 99)
					{
						qs.giveItems(BBN, 1);
					}
					break;
				
				case LIZARDMAN_GUARD:
					if (qs.getQuestItemsCount(RBN) <= 99)
					{
						qs.giveItems(RBN, 1);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(BBN) + qs.getQuestItemsCount(RBN)) == 200)
			{
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
			}
		}
		else if (cond == 4)
		{
			switch (npcId)
			{
				case LIZARDMAN_SCOUT:
				case LIZARDMAN_GUARD:
					if (qs.getQuestItemsCount(IP) <= 29)
					{
						qs.giveItems(IP, 1);
					}
					break;
				
				case GIANT_ARANEID:
					if (qs.getQuestItemsCount(GML) <= 29)
					{
						qs.giveItems(GML, 1);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(IP) + qs.getQuestItemsCount(GML)) == 60)
			{
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
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
