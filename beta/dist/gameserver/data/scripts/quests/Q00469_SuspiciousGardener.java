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

/**
 * @author KilRoy
 * @name 469 - Suspicious Gardener
 * @category Daily quest. Party
 */
public class Q00469_SuspiciousGardener extends Quest implements ScriptFile
{
	private static final int HORPINA = 33031;
	private static final int APHERUS_WATCHMAN = 22964;
	private static final int CERTIFICATE_OF_LIFE = 30385;
	private static final String APHERUS_WATCHMAN_KILL = "watchman";
	
	public Q00469_SuspiciousGardener()
	{
		super(2);
		addTalkId(HORPINA);
		addKillNpcWithLog(1, APHERUS_WATCHMAN_KILL, 30, APHERUS_WATCHMAN);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accpted":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33031-03.htm";
				break;
			
			case "quest_done":
				qs.giveItems(CERTIFICATE_OF_LIFE, 2);
				qs.exitCurrentQuest(this);
				qs.playSound(SOUND_FINISH);
				htmltext = "33031-06.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isCreated() && !qs.isNowAvailableByTime())
		{
			htmltext = "33031-04.htm";
		}
		else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
		{
			htmltext = "33031-01.htm";
		}
		else if (cond == 1)
		{
			htmltext = "33031-03.htm";
		}
		else if (cond == 2)
		{
			htmltext = "33031-05.htm";
		}
		else
		{
			htmltext = "33032-07.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(APHERUS_WATCHMAN_KILL);
			qs.playSound(SOUND_MIDDLE);
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