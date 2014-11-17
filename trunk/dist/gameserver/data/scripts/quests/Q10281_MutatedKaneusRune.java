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

public class Q10281_MutatedKaneusRune extends Quest implements ScriptFile
{
	// Npcs
	private static final int Mathias = 31340;
	private static final int Kayan = 31335;
	// Monster
	private static final int WhiteAllosce = 18577;
	// Item
	private static final int Tissue = 13840;
	
	public Q10281_MutatedKaneusRune()
	{
		super(true);
		addStartNpc(Mathias);
		addTalkId(Kayan);
		addKillId(WhiteAllosce);
		addQuestItem(Tissue);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "31340-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31335-02.htm":
				qs.giveItems(57, 720000);
				qs.addExpAndSp(3500000, 3500000);
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
		final int id = qs.getState();
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (id == COMPLETED)
		{
			if (npcId == Mathias)
			{
				htmltext = "31340-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Mathias))
		{
			if (qs.getPlayer().getLevel() >= 68)
			{
				htmltext = "31340-01.htm";
			}
			else
			{
				htmltext = "31340-00.htm";
			}
		}
		else if (npcId == Mathias)
		{
			if (cond == 1)
			{
				htmltext = "31340-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31340-05.htm";
			}
		}
		else if (npcId == Kayan)
		{
			if (cond == 1)
			{
				htmltext = "31335-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31335-01.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() == STARTED) && (qs.getCond() == 1))
		{
			qs.giveItems(Tissue, 1);
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
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
