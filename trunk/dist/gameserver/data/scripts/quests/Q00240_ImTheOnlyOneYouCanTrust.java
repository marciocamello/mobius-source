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

public class Q00240_ImTheOnlyOneYouCanTrust extends Quest implements ScriptFile
{
	// Npc
	private static final int KINTAIJIN = 32640;
	// Monsters
	private static final int SpikedStakato = 22617;
	private static final int CannibalisticStakatoFollower = 22624;
	private static final int CannibalisticStakatoLeader1 = 22625;
	private static final int CannibalisticStakatoLeader2 = 22626;
	// Item
	private static final int STAKATOFANGS = 14879;
	
	public Q00240_ImTheOnlyOneYouCanTrust()
	{
		super(false);
		addStartNpc(KINTAIJIN);
		addKillId(SpikedStakato, CannibalisticStakatoFollower, CannibalisticStakatoLeader1, CannibalisticStakatoLeader2);
		addQuestItem(STAKATOFANGS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("32640-3.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int id = qs.getState();
		
		if (id == COMPLETED)
		{
			htmltext = "32640-10.htm";
		}
		else if (id == CREATED)
		{
			if (qs.getPlayer().getLevel() >= 81)
			{
				htmltext = "32640-1.htm";
			}
			else
			{
				htmltext = "32640-0.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			htmltext = "32640-8.htm";
		}
		else if (cond == 2)
		{
			qs.giveItems(ADENA_ID, 1351512);
			qs.addExpAndSp(6411717, 7456914);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FINISH);
			htmltext = "32640-9.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.giveItems(STAKATOFANGS, 1);
			
			if (qs.getQuestItemsCount(STAKATOFANGS) >= 25)
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
