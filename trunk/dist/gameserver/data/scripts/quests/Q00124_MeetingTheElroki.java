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

public class Q00124_MeetingTheElroki extends Quest implements ScriptFile
{
	// Npcs
	private static final int Marquez = 32113;
	private static final int Mushika = 32114;
	private static final int Asamah = 32115;
	private static final int Karakawei = 32117;
	private static final int Mantarasa = 32118;
	// Item
	private static final int Mushika_egg = 8778;
	
	public Q00124_MeetingTheElroki()
	{
		super(false);
		addStartNpc(Marquez);
		addTalkId(Mushika, Asamah, Karakawei, Mantarasa);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int cond = qs.getCond();
		String htmltext = event;
		
		switch (event)
		{
			case "marquez_q0124_03.htm":
				qs.setState(STARTED);
				break;
			
			case "marquez_q0124_04.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "marquez_q0124_06.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "mushika_q0124_03.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "asama_q0124_06.htm":
				if (cond == 3)
				{
					qs.setCond(4);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "shaman_caracawe_q0124_03.htm":
				if (cond == 4)
				{
					qs.set("id", "1");
				}
				break;
			
			case "shaman_caracawe_q0124_05.htm":
				if (cond == 4)
				{
					qs.setCond(5);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "egg_of_mantarasa_q0124_02.htm":
				if (cond == 5)
				{
					qs.giveItems(Mushika_egg, 1);
					qs.setCond(6);
					qs.playSound(SOUND_MIDDLE);
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
			case Marquez:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() < 75)
						{
							htmltext = "marquez_q0124_02.htm";
							qs.exitCurrentQuest(true);
						}
						else
						{
							htmltext = "marquez_q0124_01.htm";
						}
						break;
					
					case 1:
						htmltext = "marquez_q0124_04.htm";
						break;
					
					case 2:
						htmltext = "marquez_q0124_07.htm";
						break;
				}
				break;
			
			case Mushika:
				if (cond == 2)
				{
					htmltext = "mushika_q0124_01.htm";
				}
				break;
			
			case Asamah:
				if (cond == 3)
				{
					htmltext = "asama_q0124_03.htm";
				}
				else if (cond == 6)
				{
					htmltext = "asama_q0124_08.htm";
					qs.takeItems(Mushika_egg, 1);
					qs.addExpAndSp(1109665, 1229015);
					qs.giveItems(ADENA_ID, 236510);
					qs.playSound(SOUND_FINISH);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case Karakawei:
				if (cond == 4)
				{
					htmltext = "shaman_caracawe_q0124_01.htm";
					
					if (qs.getInt("id") == 1)
					{
						htmltext = "shaman_caracawe_q0124_03.htm";
					}
					else if (cond == 5)
					{
						htmltext = "shaman_caracawe_q0124_07.htm";
					}
				}
				break;
			
			case Mantarasa:
				if (cond == 5)
				{
					htmltext = "egg_of_mantarasa_q0124_01.htm";
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
