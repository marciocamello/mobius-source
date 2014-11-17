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

public class Q10277_MutatedKaneusDion extends Quest implements ScriptFile
{
	// Npcs
	private static final int Lucas = 30071;
	private static final int Mirien = 30461;
	// Monsters
	private static final int CrimsonHatuOtis = 18558;
	private static final int SeerFlouros = 18559;
	// Items
	private static final int Tissue1 = 13832;
	private static final int Tissue2 = 13833;
	
	public Q10277_MutatedKaneusDion()
	{
		super(true);
		addStartNpc(Lucas);
		addTalkId(Mirien);
		addKillId(CrimsonHatuOtis, SeerFlouros);
		addQuestItem(Tissue1, Tissue2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30071-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30461-02.htm":
				qs.giveItems(57, 40000);
				qs.addExpAndSp(190000, 105000);
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
			if (npcId == Lucas)
			{
				htmltext = "30071-0a.htm";
			}
		}
		else if ((id == CREATED) && (npcId == Lucas))
		{
			if (qs.getPlayer().getLevel() >= 28)
			{
				htmltext = "30071-01.htm";
			}
			else
			{
				htmltext = "30071-00.htm";
			}
		}
		else if (npcId == Lucas)
		{
			if (cond == 1)
			{
				htmltext = "30071-04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30071-05.htm";
			}
		}
		else if (npcId == Mirien)
		{
			if (cond == 1)
			{
				htmltext = "30461-01a.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30461-01.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() == STARTED) && (qs.getCond() == 1))
		{
			qs.giveItems(Tissue1, 1);
			qs.giveItems(Tissue2, 1);
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
