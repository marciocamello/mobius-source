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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00190_LostDream extends Quest implements ScriptFile
{
	// Npcs
	private static final int Kusto = 30512;
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	private static final int Juris = 30113;
	
	public Q00190_LostDream()
	{
		super(false);
		addTalkId(Kusto, Nikola, Lorain, Juris);
		addFirstTalkId(Kusto);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "head_blacksmith_kusto_q0190_03.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				break;
			
			case "head_blacksmith_kusto_q0190_06.htm":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
				break;
			
			case "juria_q0190_03.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (qs.getState() == STARTED)
		{
			switch (npcId)
			{
				case Kusto:
					switch (cond)
					{
						case 0:
							if (qs.getPlayer().getLevel() < 42)
							{
								htmltext = "head_blacksmith_kusto_q0190_02.htm";
							}
							else
							{
								htmltext = "head_blacksmith_kusto_q0190_01.htm";
							}
							break;
						
						case 1:
							htmltext = "head_blacksmith_kusto_q0190_04.htm";
							break;
						
						case 2:
							htmltext = "head_blacksmith_kusto_q0190_05.htm";
							break;
						
						case 3:
							htmltext = "head_blacksmith_kusto_q0190_07.htm";
							break;
						
						case 5:
							htmltext = "head_blacksmith_kusto_q0190_08.htm";
							qs.giveItems(ADENA_ID, 127224);
							qs.exitCurrentQuest(false);
							qs.playSound(SOUND_FINISH);
							break;
					}
					break;
				
				case Juris:
					if (cond == 1)
					{
						htmltext = "juria_q0190_01.htm";
					}
					else if (cond == 2)
					{
						htmltext = "juria_q0190_04.htm";
					}
					break;
				
				case Lorain:
					if (cond == 3)
					{
						htmltext = "researcher_lorain_q0190_01.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(4);
					}
					else if (cond == 4)
					{
						htmltext = "researcher_lorain_q0190_02.htm";
					}
					break;
				
				case Nikola:
					if (cond == 4)
					{
						htmltext = "maestro_nikola_q0190_01.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(5);
					}
					else if (cond == 5)
					{
						htmltext = "maestro_nikola_q0190_02.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00187_NikolasHeart.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
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
