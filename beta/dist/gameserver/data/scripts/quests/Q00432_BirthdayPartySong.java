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

public class Q00432_BirthdayPartySong extends Quest implements ScriptFile
{
	// Npc
	private static final int MELODY_MAESTRO_OCTAVIA = 31043;
	// Monster
	private static final int ROUGH_HEWN_ROCK_GOLEM = 21103;
	// Items
	private static final int RED_CRYSTALS = 7541;
	private static final int BIRTHDAY_ECHO_CRYSTAL = 7061;
	
	public Q00432_BirthdayPartySong()
	{
		super(false);
		addStartNpc(MELODY_MAESTRO_OCTAVIA);
		addKillId(ROUGH_HEWN_ROCK_GOLEM);
		addQuestItem(RED_CRYSTALS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "muzyko_q0432_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "muzyko_q0432_0201.htm":
				if (qs.getQuestItemsCount(RED_CRYSTALS) == 50)
				{
					qs.takeItems(RED_CRYSTALS, -1);
					qs.giveItems(BIRTHDAY_ECHO_CRYSTAL, 25);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "muzyko_q0432_0202.htm";
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
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 31)
				{
					htmltext = "muzyko_q0432_0101.htm";
				}
				else
				{
					htmltext = "muzyko_q0432_0103.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "muzyko_q0432_0106.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(RED_CRYSTALS) == 50)
				{
					htmltext = "muzyko_q0432_0105.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if ((qs.getCond() == 1) && (qs.getQuestItemsCount(RED_CRYSTALS) < 50))
		{
			qs.giveItems(RED_CRYSTALS, 1);
			
			if (qs.getQuestItemsCount(RED_CRYSTALS) == 50)
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
