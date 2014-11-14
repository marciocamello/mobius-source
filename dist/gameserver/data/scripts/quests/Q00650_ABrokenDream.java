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

public class Q00650_ABrokenDream extends Quest implements ScriptFile
{
	// Npcs
	private static final int RailroadEngineer = 32054;
	// Monsters
	private static final int ForgottenCrewman = 22027;
	private static final int VagabondOfTheRuins = 22028;
	// Items
	private static final int RemnantsOfOldDwarvesDreams = 8514;
	
	public Q00650_ABrokenDream()
	{
		super(false);
		addStartNpc(RailroadEngineer);
		addKillId(ForgottenCrewman, VagabondOfTheRuins);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accept":
				htmltext = "ghost_of_railroadman_q0650_0103.htm";
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				break;
			
			case "650_4":
				htmltext = "ghost_of_railroadman_q0650_0205.htm";
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				qs.unset("cond");
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				final QuestState OceanOfDistantStar = qs.getPlayer().getQuestState(Q00117_TheOceanOfDistantStars.class);
				if (OceanOfDistantStar != null)
				{
					if (OceanOfDistantStar.isCompleted())
					{
						if (qs.getPlayer().getLevel() < 39)
						{
							qs.exitCurrentQuest(true);
							htmltext = "ghost_of_railroadman_q0650_0102.htm";
						}
						else
						{
							htmltext = "ghost_of_railroadman_q0650_0101.htm";
						}
					}
					else
					{
						htmltext = "ghost_of_railroadman_q0650_0104.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "ghost_of_railroadman_q0650_0104.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "ghost_of_railroadman_q0650_0202.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		qs.rollAndGive(RemnantsOfOldDwarvesDreams, 1, 1, 68);
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
