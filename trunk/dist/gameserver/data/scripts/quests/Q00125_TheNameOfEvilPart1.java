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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00125_TheNameOfEvilPart1 extends Quest implements ScriptFile
{
	// Npcs
	private static final int Mushika = 32114;
	private static final int Karakawei = 32117;
	private static final int UluKaimu = 32119;
	private static final int BaluKaimu = 32120;
	private static final int ChutaKaimu = 32121;
	// Monsters
	private static final int Ornithomimus = 22742;
	private static final int Deinonychus = 22743;
	private static final int Ornithomimus2 = 22744;
	private static final int Deinonychus2 = 22745;
	// Items
	private static final int OrClaw = 8779;
	private static final int DienBone = 8780;
	
	public Q00125_TheNameOfEvilPart1()
	{
		super(false);
		addStartNpc(Mushika);
		addTalkId(Karakawei, UluKaimu, BaluKaimu, ChutaKaimu);
		addQuestItem(OrClaw, DienBone);
		addKillId(Ornithomimus, Ornithomimus2, Deinonychus, Deinonychus2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32114-05.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32114-07.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32117-08.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32117-13.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "stat1false":
				htmltext = "32119-2.htm";
				break;
			
			case "stat1true":
				qs.setCond(6);
				htmltext = "32119-1.htm";
				break;
			
			case "stat2false":
				htmltext = "32120-2.htm";
				break;
			
			case "stat2true":
				qs.setCond(7);
				htmltext = "32120-1.htm";
				break;
			
			case "stat3false":
				htmltext = "32121-2.htm";
				break;
			
			case "stat3true":
				qs.giveItems(8781, 1);
				qs.setCond(8);
				htmltext = "32121-1.htm";
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
			case Mushika:
				if (cond == 0)
				{
					QuestState meetQuest = qs.getPlayer().getQuestState(Q00124_MeetingTheElroki.class);
					
					if ((qs.getPlayer().getLevel() > 76) && (meetQuest != null) && meetQuest.isCompleted())
					{
						htmltext = "32114.htm";
					}
					else
					{
						htmltext = "32114-0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "32114-05.htm";
				}
				else if (cond == 8)
				{
					htmltext = "32114-08.htm";
					qs.addExpAndSp(898056, 1008100);
					qs.playSound(SOUND_FINISH);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case Karakawei:
				if (cond == 2)
				{
					htmltext = "32117.htm";
				}
				else if (cond == 3)
				{
					htmltext = "32117-09.htm";
				}
				else if (cond == 4)
				{
					qs.takeAllItems(DienBone);
					qs.takeAllItems(OrClaw);
					htmltext = "32117-1.htm";
				}
				break;
			
			case UluKaimu:
				if (cond == 5)
				{
					htmltext = "32119.htm";
				}
				break;
			
			case BaluKaimu:
				if (cond == 6)
				{
					htmltext = "32120.htm";
				}
				break;
			
			case ChutaKaimu:
				if (cond == 7)
				{
					htmltext = "32121.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 3)
		{
			switch (npc.getId())
			{
				case Ornithomimus:
				case Ornithomimus2:
					if ((qs.getQuestItemsCount(OrClaw) < 2) && Rnd.chance(10 * Config.RATE_QUESTS_DROP))
					{
						qs.giveItems(OrClaw, 1);
						qs.playSound(SOUND_MIDDLE);
					}
					break;
				
				case Deinonychus:
				case Deinonychus2:
					if ((qs.getQuestItemsCount(DienBone) < 2) && Rnd.chance(10 * Config.RATE_QUESTS_DROP))
					{
						qs.giveItems(DienBone, 1);
						qs.playSound(SOUND_MIDDLE);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(DienBone) >= 2) && (qs.getQuestItemsCount(OrClaw) >= 2))
			{
				qs.setCond(4);
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
