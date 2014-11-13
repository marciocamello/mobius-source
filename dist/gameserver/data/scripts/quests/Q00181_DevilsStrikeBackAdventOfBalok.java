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

public class Q00181_DevilsStrikeBackAdventOfBalok extends Quest implements ScriptFile
{
	// Npc
	private static final int FIOREN = 33044;
	// Monster
	private static final int BALOK = 29218;
	// Items
	private static final int CONTRACT = 17592;
	private static final int EAR = 17527;
	private static final int EWR = 17526;
	private static final int POUCH = 34861;
	
	public Q00181_DevilsStrikeBackAdventOfBalok()
	{
		super(false);
		addStartNpc(FIOREN);
		addTalkId(FIOREN);
		addKillId(BALOK);
		addQuestItem(CONTRACT);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "33044-06.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "reward":
				qs.addExpAndSp(886750000, 414855000);
				qs.giveItems(57, 37128000L);
				qs.playSound("SOUND_FINISH");
				qs.exitCurrentQuest(false);
				final int rnd = Rnd.get(2);
				switch (rnd)
				{
					case 0:
						qs.giveItems(EWR, 2);
						return "33044-09.htm";
						
					case 1:
						qs.giveItems(EAR, 2);
						return "33044-10.htm";
						
					case 2:
						qs.giveItems(POUCH, 2);
						return "33044-11.htm";
				}
				break;
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
		final Player player = qs.getPlayer();
		
		switch (qs.getState())
		{
			case CREATED:
				if (player.getLevel() < 97)
				{
					htmltext = "33044-02.htm";
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "33044-01.htm";
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "33044-07.htm";
				}
				else
				{
					if (cond == 2)
					{
						htmltext = "33044-08.htm";
					}
				}
				break;
			
			case COMPLETED:
				htmltext = "33044-03.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (qs.getCond() == 1)
		{
			if (player.getParty() == null)
			{
				qs.setCond(2);
				qs.giveItems(CONTRACT, 1);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				for (Player pmember : player.getParty().getPartyMembers())
				{
					final QuestState pst = pmember.getQuestState("Q00181_DevilsStrikeBackAdventOfBalok");
					
					if ((pst != null) && (pst.getCond() == 1))
					{
						pst.setCond(2);
						pst.giveItems(CONTRACT, 1);
						pst.playSound(SOUND_MIDDLE);
					}
				}
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
