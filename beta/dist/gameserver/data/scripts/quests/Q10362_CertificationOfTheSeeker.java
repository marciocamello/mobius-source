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

public class Q10362_CertificationOfTheSeeker extends Quest implements ScriptFile
{
	// Npcs
	private static final int Chesha = 33449;
	private static final int Nagel = 33450;
	// Monsters
	private static final int Husk = 22991;
	private static final int Stalker = 22992;
	// Others
	private static final String stalker_item = "husk_stalker";
	private static final String husk_item = "husk";
	
	public Q10362_CertificationOfTheSeeker()
	{
		super(false);
		addStartNpc(Chesha);
		addTalkId(Chesha, Nagel);
		addKillNpcWithLog(1, stalker_item, 10, Stalker);
		addKillNpcWithLog(1, husk_item, 5, Husk);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10361_RolesOfTheSeeker.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-4.htm";
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
				qs.getPlayer().addExpAndSp(50000, 12);
				qs.giveItems(57, 430);
				qs.giveItems(1060, 50);
				qs.giveItems(49, 1);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Chesha:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else if (cond == 2)
				{
					htmltext = "0-5.htm";
					qs.setCond(3);
				}
				else if (cond == 3)
				{
					htmltext = "0-6.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Nagel:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 3)
				{
					htmltext = "1-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(stalker_item);
			qs.unset(husk_item);
			qs.setCond(2);
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
