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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00453_NotStrongEnoughAlone extends Quest implements ScriptFile
{
	private static final int Klemis = 32734;
	public static final String A_MOBS = "a_mobs";
	public static final String B_MOBS = "b_mobs";
	public static final String C_MOBS = "c_mobs";
	public static final String E_MOBS = "e_mobs";
	private static final int[] Rewards =
	{
		// Requiem weapon
		18103,
		18104,
		18105,
		18106,
		18107,
		18108,
		18109,
		18110,
		18111,
		18112,
		18113,
		// Apocalypse weapon
		18137,
		18138,
		18139,
		18140,
		18141,
		18142,
		18143,
		18144,
		18145,
		18146,
		18147,
		// Enchant Scrolls
		17526,
		17527,
		// Attribute
		9546,
		9547,
		9548,
		9549,
		9550,
		9551,
		4343,
		4342,
		4345,
		4344,
		4346,
		4347
	};
	
	public Q00453_NotStrongEnoughAlone()
	{
		super(true);
		addStartNpc(Klemis);
		addKillNpcWithLog(2, A_MOBS, 15, 22746, 22750);
		addKillNpcWithLog(2, B_MOBS, 15, 22747, 22751);
		addKillNpcWithLog(2, C_MOBS, 15, 22748, 22752);
		addKillNpcWithLog(2, E_MOBS, 15, 22749, 22753);
		addKillNpcWithLog(3, A_MOBS, 20, 22754, 22757);
		addKillNpcWithLog(3, B_MOBS, 20, 22755, 22758);
		addKillNpcWithLog(3, C_MOBS, 20, 22756, 22759);
		addKillNpcWithLog(4, A_MOBS, 20, 22760, 22763);
		addKillNpcWithLog(4, B_MOBS, 20, 22761, 22764);
		addKillNpcWithLog(4, C_MOBS, 20, 22762, 22765);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "klemis_q453_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "bistakon":
				htmltext = "klemis_q453_05.htm";
				qs.setCond(2);
				break;
			
			case "reptilicon":
				htmltext = "klemis_q453_06.htm";
				qs.setCond(3);
				break;
			
			case "cokrakon":
				htmltext = "klemis_q453_07.htm";
				qs.setCond(4);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Klemis:
				switch (qs.getState())
				{
					case CREATED:
					{
						QuestState state = qs.getPlayer().getQuestState(Q10282_ToTheSeedOfAnnihilation.class);
						
						if ((qs.getPlayer().getLevel() >= 84) && (state != null) && state.isCompleted())
						{
							if (qs.isNowAvailableByTime())
							{
								htmltext = "klemis_q453_01.htm";
							}
							else
							{
								htmltext = "klemis_q453_00a.htm";
							}
						}
						else
						{
							htmltext = "klemis_q453_00.htm";
						}
						
						break;
					}
					case STARTED:
					{
						switch (cond)
						{
							case 1:
								htmltext = "klemis_q453_03.htm";
								break;
							
							case 2:
								htmltext = "klemis_q453_09.htm";
								break;
							
							case 3:
								htmltext = "klemis_q453_10.htm";
								break;
							
							case 4:
								htmltext = "klemis_q453_11.htm";
								break;
							
							case 5:
								htmltext = "klemis_q453_12.htm";
								qs.giveItems(Rewards[Rnd.get(Rewards.length)], 1, false);
								qs.setState(COMPLETED);
								qs.playSound(SOUND_FINISH);
								qs.exitCurrentQuest(this);
								break;
						}
						
						break;
					}
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
			qs.unset(A_MOBS);
			qs.unset(B_MOBS);
			qs.unset(C_MOBS);
			qs.unset(E_MOBS);
			qs.setCond(5);
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
