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

public class Q10368_RebellionOfMonsters extends Quest implements ScriptFile
{
	// Npc
	private static final int fred = 33179;
	// Monsters
	private static final int jaga = 23024;
	private static final int jagav = 23025;
	private static final int incect = 23099;
	private static final int incectl = 23100;
	// Others
	private static final String jaga_item = "jaga";
	private static final String jagav_item = "jagav";
	private static final String incect_item = "incect";
	private static final String incectl_item = "incectl";
	
	public Q10368_RebellionOfMonsters()
	{
		super(false);
		addStartNpc(fred);
		addTalkId(fred);
		addKillNpcWithLog(1, jaga_item, 10, jaga);
		addKillNpcWithLog(1, jagav_item, 15, jagav);
		addKillNpcWithLog(1, incect_item, 15, incect);
		addKillNpcWithLog(1, incectl_item, 20, incectl);
		addLevelCheck(34, 40);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("quest_ac"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isCompleted())
		{
			htmltext = "0-c.htm";
		}
		else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
		{
			htmltext = "0-1.htm";
		}
		else if (cond == 1)
		{
			htmltext = "0-4.htm";
		}
		else if (cond == 2)
		{
			htmltext = "0-5.htm";
			qs.getPlayer().addExpAndSp(550000, 150000);
			qs.giveItems(57, 99000);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(jaga_item);
			qs.unset(jagav_item);
			qs.unset(incect_item);
			qs.unset(incectl_item);
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