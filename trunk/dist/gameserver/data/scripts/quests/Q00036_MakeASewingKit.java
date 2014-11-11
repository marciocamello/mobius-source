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

public class Q00036_MakeASewingKit extends Quest implements ScriptFile
{
	// Npc
	private final static int FERRIS = 30847;
	// Monster
	private final static int REINFORCED_STEEL_GOLEM = 20566;
	// Items
	private final static int REINFORCED_STEEL = 7163;
	private final static int ARTISANS_FRAME = 1891;
	private final static int ORIHARUKON = 1893;
	private final static int SEWING_KIT = 7078;
	
	public Q00036_MakeASewingKit()
	{
		super(false);
		addStartNpc(FERRIS);
		addTalkId(FERRIS);
		addKillId(REINFORCED_STEEL_GOLEM);
		addQuestItem(REINFORCED_STEEL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "head_blacksmith_ferris_q0036_0104.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "head_blacksmith_ferris_q0036_0201.htm":
				if (cond == 2)
				{
					qs.takeItems(REINFORCED_STEEL, 5);
					qs.setCond(3);
				}
				break;
			
			case "head_blacksmith_ferris_q0036_0301.htm":
				if ((qs.getQuestItemsCount(ORIHARUKON) >= 10) && (qs.getQuestItemsCount(ARTISANS_FRAME) >= 10))
				{
					qs.takeItems(ORIHARUKON, 10);
					qs.takeItems(ARTISANS_FRAME, 10);
					qs.giveItems(SEWING_KIT, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "head_blacksmith_ferris_q0036_0203.htm";
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
		
		switch (cond)
		{
			case 0:
				if (qs.getQuestItemsCount(SEWING_KIT) == 0)
				{
					if (qs.getPlayer().getLevel() >= 60)
					{
						final QuestState fwear = qs.getPlayer().getQuestState(Q00037_MakeFormalWear.class);
						
						if ((fwear != null) && (fwear.getState() == STARTED))
						{
							if (fwear.getCond() == 6)
							{
								htmltext = "head_blacksmith_ferris_q0036_0101.htm";
							}
							else
							{
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							qs.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "head_blacksmith_ferris_q0036_0103.htm";
					}
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(REINFORCED_STEEL) < 5)
				{
					htmltext = "head_blacksmith_ferris_q0036_0106.htm";
				}
				break;
			
			case 2:
				if (qs.getQuestItemsCount(REINFORCED_STEEL) == 5)
				{
					htmltext = "head_blacksmith_ferris_q0036_0105.htm";
				}
				break;
			
			case 3:
				if ((qs.getQuestItemsCount(ORIHARUKON) < 10) || (qs.getQuestItemsCount(ARTISANS_FRAME) < 10))
				{
					htmltext = "head_blacksmith_ferris_q0036_0204.htm";
				}
				else if ((qs.getQuestItemsCount(ORIHARUKON) >= 10) && (qs.getQuestItemsCount(ARTISANS_FRAME) >= 10))
				{
					htmltext = "head_blacksmith_ferris_q0036_0203.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (qs.getQuestItemsCount(REINFORCED_STEEL) < 5))
		{
			qs.giveItems(REINFORCED_STEEL, 1);
			
			if (qs.getQuestItemsCount(REINFORCED_STEEL) == 5)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
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
