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

import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00183_RelicExploration extends Quest implements ScriptFile
{
	// Npcs
	private static final int Kusto = 30512;
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	
	public Q00183_RelicExploration()
	{
		super(false);
		addStartNpc(Kusto);
		addStartNpc(Nikola);
		addTalkId(Lorain);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "30512-03.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.setState(STARTED);
				break;
			
			case "30673-04.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "Contract":
				Quest q1 = QuestManager.getQuest(Q00184_ArtOfPersuasion.class);
				if (q1 != null)
				{
					qs.giveItems(ADENA_ID, 26866);
					QuestState qs1 = q1.newQuestState(player, STARTED);
					q1.notifyEvent("30621-01.htm", qs1, npc);
					qs.playSound(SOUND_MIDDLE);
					qs.exitCurrentQuest(false);
				}
				return null;
				
			case "Consideration":
				Quest q2 = QuestManager.getQuest(Q00185_NikolasCooperation.class);
				if (q2 != null)
				{
					qs.giveItems(ADENA_ID, 18100);
					QuestState qs2 = q2.newQuestState(qs.getPlayer(), STARTED);
					q2.notifyEvent("30621-01.htm", qs2, npc);
					qs.playSound(SOUND_MIDDLE);
					qs.exitCurrentQuest(false);
				}
				return null;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Kusto:
				if (qs.getState() == CREATED)
				{
					if (qs.getPlayer().getLevel() < 40)
					{
						htmltext = "30512-00.htm";
					}
					else
					{
						htmltext = "30512-01.htm";
					}
				}
				else
				{
					htmltext = "30512-04.htm";
				}
				break;
			
			case Lorain:
				if (cond == 1)
				{
					htmltext = "30673-01.htm";
				}
				else
				{
					htmltext = "30673-05.htm";
				}
				break;
			
			case Nikola:
				if (cond == 2)
				{
					htmltext = "30621-01.htm";
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
