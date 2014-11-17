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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10306_TheCorruptedLeader extends Quest implements ScriptFile
{
	// Npc
	private static final int NAOMI_KASHERON = 32896;
	// Monster
	private static final int KIMERIAN = 32896;
	// Items
	private static final int[] CRYSTALS =
	{
		9552,
		9553,
		9554,
		9555,
		9556,
		9557
	};
	
	public Q10306_TheCorruptedLeader()
	{
		super(false);
		addStartNpc(NAOMI_KASHERON);
		addKillId(KIMERIAN);
		addQuestCompletedCheck(Q10305_UnstoppableFutileEfforts.class);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		switch (event)
		{
			case "32896-05.htm":
			{
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			}
			case "32896-08.htm":
			{
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(9479594, 4104484);
				qs.giveItems(CRYSTALS[Rnd.get(CRYSTALS.length)], 1);
				qs.exitCurrentQuest(false);
				break;
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		if (qs == null)
		{
			return htmltext;
		}
		
		final Player player = qs.getPlayer();
		final QuestState prevst = player.getQuestState(Q10305_UnstoppableFutileEfforts.class);
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "32896-02.htm";
				break;
			
			case CREATED:
				if (player.getLevel() >= 90)
				{
					if ((prevst != null) && (prevst.isCompleted()))
					{
						htmltext = "32896-01.htm";
					}
					else
					{
						qs.exitCurrentQuest(true);
						htmltext = "32896-03.htm";
					}
				}
				else
				{
					qs.exitCurrentQuest(true);
					htmltext = "32896-03.htm";
				}
				
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "32896-06.htm";
				}
				else
				{
					if (qs.getCond() != 2)
					{
						break;
					}
					
					htmltext = "32896-07.htm";
				}
		}
		
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10306_TheCorruptedLeader.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
