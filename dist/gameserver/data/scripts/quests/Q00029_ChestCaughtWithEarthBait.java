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

public class Q00029_ChestCaughtWithEarthBait extends Quest implements ScriptFile
{
	// Npcs
	private final static int Willie = 31574;
	private final static int Anabel = 30909;
	// Items
	private final static int SmallPurpleTreasureChest = 6507;
	private final static int SmallGlassBox = 7627;
	private final static int PlatedLeatherGloves = 2455;
	
	public Q00029_ChestCaughtWithEarthBait()
	{
		super(false);
		addStartNpc(Willie);
		addTalkId(Anabel);
		addQuestItem(SmallGlassBox);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_willeri_q0029_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_willeri_q0029_0201.htm":
				if (qs.getQuestItemsCount(SmallPurpleTreasureChest) > 0)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
					qs.takeItems(SmallPurpleTreasureChest, 1);
					qs.giveItems(SmallGlassBox, 1);
				}
				else
				{
					htmltext = "fisher_willeri_q0029_0202.htm";
				}
				break;
			
			case "29_GiveGlassBox":
				if (qs.getQuestItemsCount(SmallGlassBox) == 1)
				{
					htmltext = "magister_anabel_q0029_0301.htm";
					qs.takeItems(SmallGlassBox, -1);
					qs.giveItems(PlatedLeatherGloves, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "magister_anabel_q0029_0302.htm";
					qs.exitCurrentQuest(true);
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
			case Willie:
				if (qs.getState() == CREATED)
				{
					if (qs.getPlayer().getLevel() < 48)
					{
						htmltext = "fisher_willeri_q0029_0102.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						QuestState WilliesSpecialBait = qs.getPlayer().getQuestState(Q00052_WilliesSpecialBait.class);
						
						if (WilliesSpecialBait != null)
						{
							if (WilliesSpecialBait.isCompleted())
							{
								htmltext = "fisher_willeri_q0029_0101.htm";
							}
							else
							{
								htmltext = "fisher_willeri_q0029_0102.htm";
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							htmltext = "fisher_willeri_q0029_0103.htm";
							qs.exitCurrentQuest(true);
						}
					}
				}
				else if (cond == 1)
				{
					htmltext = "fisher_willeri_q0029_0105.htm";
					
					if (qs.getQuestItemsCount(SmallPurpleTreasureChest) == 0)
					{
						htmltext = "fisher_willeri_q0029_0106.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "fisher_willeri_q0029_0203.htm";
				}
				break;
			
			case Anabel:
				if (cond == 2)
				{
					htmltext = "magister_anabel_q0029_0201.htm";
				}
				else
				{
					htmltext = "magister_anabel_q0029_0302.htm";
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
