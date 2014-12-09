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
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class Q10365_SeekerEscort extends Quest implements ScriptFile
{
	// Npcs
	private static final int dep = 33453;
	private static final int sebian = 32978;
	// Others
	private static final int seeker = 32988;
	private NpcInstance seek = null;
	private static final int[] SOLDER_START_POINT =
	{
		-110616,
		238376,
		-2950
	};
	
	public Q10365_SeekerEscort()
	{
		super(false);
		addStartNpc(dep);
		addTalkId(dep);
		addTalkId(sebian);
		addLevelCheck(16, 25);
		addQuestCompletedCheck(Q10364_ObligationsOfTheSeeker.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(2); // Autocomplete tempfix {normal is setCond(1)}
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				spawnseeker(qs);
				break;
			
			case "king":
				htmltext = "";
				spawnseeker(qs);
				break;
			
			case "qet_rev":
				htmltext = "1-2.htm";
				qs.getPlayer().addExpAndSp(120000, 2000);
				qs.giveItems(57, 649);
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
			case dep:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if ((cond == 1) && (qs.getInt("seeksp") == 0))
				{
					htmltext = "0-5.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case sebian:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = "1-nc.htm";
				}
				else if (cond == 1)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "1-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private void spawnseeker(QuestState qs)
	{
		seek = NpcUtils.spawnSingle(seeker, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, qs.getPlayer().getGeoIndex()));
		seek.setFollowTarget(qs.getPlayer());
		qs.set("seeksp", 1);
		qs.set("zone", 1);
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
