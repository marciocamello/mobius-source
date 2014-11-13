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

public class Q00027_ChestCaughtWithWindBait extends Quest implements ScriptFile
{
	// Npcs
	private static final int Lanosco = 31570;
	private static final int Shaling = 31434;
	// Items
	private static final int StrangeGolemBlueprint = 7625;
	private static final int BigBlueTreasureChest = 6500;
	private static final int BlackPearlRing = 880;
	
	public Q00027_ChestCaughtWithWindBait()
	{
		super(false);
		addStartNpc(Lanosco);
		addTalkId(Shaling);
		addQuestItem(StrangeGolemBlueprint);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_lanosco_q0027_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_lanosco_q0027_0201.htm":
				if (qs.getQuestItemsCount(BigBlueTreasureChest) > 0)
				{
					qs.takeItems(BigBlueTreasureChest, 1);
					qs.giveItems(StrangeGolemBlueprint, 1);
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					htmltext = "fisher_lanosco_q0027_0202.htm";
				}
				break;
			
			case "blueprint_seller_shaling_q0027_0301.htm":
				if (qs.getQuestItemsCount(StrangeGolemBlueprint) == 1)
				{
					qs.takeItems(StrangeGolemBlueprint, -1);
					qs.giveItems(BlackPearlRing, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "blueprint_seller_shaling_q0027_0302.htm";
					qs.exitCurrentQuest(true);
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
			case Lanosco:
				if (qs.getState() == CREATED)
				{
					if (qs.getPlayer().getLevel() < 27)
					{
						htmltext = "fisher_lanosco_q0027_0101.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						QuestState LanoscosSpecialBait = qs.getPlayer().getQuestState(Q00050_LanoscosSpecialBait.class);
						
						if (LanoscosSpecialBait != null)
						{
							if (LanoscosSpecialBait.isCompleted())
							{
								htmltext = "fisher_lanosco_q0027_0101.htm";
							}
							else
							{
								htmltext = "fisher_lanosco_q0027_0102.htm";
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							htmltext = "fisher_lanosco_q0027_0103.htm";
							qs.exitCurrentQuest(true);
						}
					}
				}
				else if (cond == 1)
				{
					htmltext = "fisher_lanosco_q0027_0105.htm";
					
					if (qs.getQuestItemsCount(BigBlueTreasureChest) == 0)
					{
						htmltext = "fisher_lanosco_q0027_0106.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "fisher_lanosco_q0027_0203.htm";
				}
				break;
			
			case Shaling:
				if (cond == 2)
				{
					htmltext = "blueprint_seller_shaling_q0027_0201.htm";
				}
				else
				{
					htmltext = "blueprint_seller_shaling_q0027_0302.htm";
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
