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

public class Q10316_UndecayingMemoryOfThePast extends Quest implements ScriptFile
{
	// Npc
	private static final int OPERA = 32946;
	// Monster
	private static final int SPEZION = 25779;
	// Items
	private static final int GIANTS_WARMISHT_HOLDER = 19305;
	private static final int GIANTS_REONIRS_MOLD = 19306;
	private static final int GIANTS_ARCSMITH_ANVIL = 19307;
	private static final int GIANTS_WARSMITH_MOLD = 19308;
	private static final int ENCHANT_ARMOR_R = 17527;
	private static final int HARDENER_POUNCH_R = 34861;
	
	public Q10316_UndecayingMemoryOfThePast()
	{
		super(PARTY_ALL);
		addStartNpc(OPERA);
		addKillId(SPEZION);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10315_ToThePrisonOfDarkness.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		if (event.equals("32946-06.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("32946-12.htm")) // Wtf??
		{
			qs.addExpAndSp(54093924, 23947602);
			qs.giveItems(GIANTS_WARMISHT_HOLDER, 1);
			qs.giveItems(GIANTS_REONIRS_MOLD, 1);
			qs.giveItems(GIANTS_ARCSMITH_ANVIL, 1);
			qs.giveItems(GIANTS_WARSMITH_MOLD, 1);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
		}
		else if (event.equals("32946-12.htm")) // Wtf??
		{
			qs.addExpAndSp(54093924, 23947602);
			qs.giveItems(ENCHANT_ARMOR_R, 2);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
		}
		else if (event.equals("32946-12.htm")) // Wtf??
		{
			qs.addExpAndSp(54093924, 23947602);
			qs.giveItems(HARDENER_POUNCH_R, 2);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
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
		final QuestState previous = player.getQuestState(Q10315_ToThePrisonOfDarkness.class);
		
		if ((previous == null) || (!previous.isCompleted()))
		{
			if (player.getLevel() < 90)
			{
				qs.exitCurrentQuest(true);
				return "32946-02.htm";
			}
		}
		else
		{
			qs.exitCurrentQuest(true);
			return "32946-09.htm";
		}
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "32946-10.htm";
				break;
			
			case CREATED:
				htmltext = "32946-01.htm";
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "32946-07.htm";
				}
				else
				{
					if (qs.getCond() != 2)
					{
						break;
					}
					
					htmltext = "32946-08.htm";
				}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (npc.getId() == SPEZION))
		{
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(2);
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10315_ToThePrisonOfDarkness.class);
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
