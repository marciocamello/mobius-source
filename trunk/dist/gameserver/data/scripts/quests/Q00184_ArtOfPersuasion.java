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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00184_ArtOfPersuasion extends Quest implements ScriptFile
{
	// Npcs
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	private static final int Device = 32366;
	private static final int Alarm = 32367;
	// Items
	private static final int Certificate = 10362;
	private static final int Metal = 10359;
	private static final int BrokenMetal = 10360;
	private static final int NicolasMap = 10361;
	
	public Q00184_ArtOfPersuasion()
	{
		super(false);
		addTalkId(Lorain, Nikola, Device, Alarm);
		addQuestItem(NicolasMap, BrokenMetal, Metal);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		if (event.startsWith("correct"))
		{
			qs.set("pass", str(qs.getInt("pass") + 1));
			htmltext = event.substring(8);
			
			if (htmltext.equals("32367-07.htm"))
			{
				if (qs.getInt("pass") == 4)
				{
					qs.set("step", "3");
					qs.cancelQuestTimer("1");
					qs.cancelQuestTimer("2");
					qs.cancelQuestTimer("3");
					qs.cancelQuestTimer("4");
					qs.unset("pass");
					npc.deleteMe();
				}
				else
				{
					htmltext = "32367-06.htm";
				}
			}
		}
		
		switch (event)
		{
			case "30621-01.htm":
				if (player.getLevel() < 40)
				{
					htmltext = "30621-00.htm";
				}
				break;
			
			case "30621-04.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.giveItems(NicolasMap, 1);
				break;
			
			case "30673-03.htm":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
				qs.takeItems(NicolasMap, -1);
				break;
			
			case "30673-05.htm":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
				break;
			
			case "30673-09.htm":
				if (qs.getQuestItemsCount(BrokenMetal) > 0)
				{
					htmltext = "30673-10.htm";
				}
				else if (qs.getQuestItemsCount(Metal) > 0)
				{
					qs.giveItems(Certificate, 1);
				}
				qs.giveItems(ADENA_ID, 80598);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "32366-02.htm":
				NpcInstance alarm = qs.addSpawn(Alarm, 16491, 113563, -9064);
				qs.set("step", "1");
				qs.playSound("ItemSound3.sys_siren");
				qs.startQuestTimer("1", 60000, alarm);
				Functions.npcSay(alarm, "Intruder Alert! The alarm will self-destruct in 1 minutes.");
				break;
			
			case "32366-05.htm":
				qs.unset("step");
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(5);
				qs.giveItems(BrokenMetal, 1);
				break;
			
			case "32366-06.htm":
				qs.unset("step");
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(4);
				qs.giveItems(Metal, 1);
				break;
			
			case "32367-02.htm":
				qs.set("pass", "0");
				break;
			
			case "1":
				Functions.npcSay(npc, "The alarm will self-destruct in 60 seconds. Enter passcode to override.");
				qs.startQuestTimer("2", 30000, npc);
				return null;
				
			case "2":
				Functions.npcSay(npc, "The alarm will self-destruct in 30 seconds. Enter passcode to override.");
				qs.startQuestTimer("3", 20000, npc);
				return null;
				
			case "3":
				Functions.npcSay(npc, "The alarm will self-destruct in 10 seconds. Enter passcode to override.");
				qs.startQuestTimer("4", 10000, npc);
				return null;
				
			case "4":
				Functions.npcSay(npc, "Recorder crushed.");
				npc.deleteMe();
				qs.set("step", "2");
				return null;
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
		
		if (qs.isStarted())
		{
			switch (npcId)
			{
				case Nikola:
					if (cond == 0)
					{
						if (qs.getPlayer().getLevel() < 40)
						{
							htmltext = "30621-00.htm";
						}
						else
						{
							htmltext = "30621-01.htm";
						}
					}
					else if (cond == 1)
					{
						htmltext = "30621-05.htm";
					}
					break;
				
				case Lorain:
					switch (cond)
					{
						case 1:
							htmltext = "30673-01.htm";
							break;
						
						case 2:
							htmltext = "30673-04.htm";
							break;
						
						case 3:
							htmltext = "30673-06.htm";
							break;
						
						case 4:
						case 5:
							htmltext = "30673-07.htm";
							break;
					}
					break;
				
				case Device:
					final int step = qs.getInt("step");
					if (cond == 3)
					{
						switch (step)
						{
							case 0:
								htmltext = "32366-01.htm";
								break;
							
							case 1:
								htmltext = "32366-02.htm";
								break;
							
							case 2:
								htmltext = "32366-04.htm";
								break;
							
							case 3:
								htmltext = "32366-03.htm";
								break;
						}
					}
					break;
				
				case Alarm:
					htmltext = "32367-01.htm";
					break;
			}
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
