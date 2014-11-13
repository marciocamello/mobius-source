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

public class Q00180_InfernalFlamesBurningInCrystalPrison extends Quest implements ScriptFile
{
	// Npc
	private static final int FIOREN = 33044;
	// Monster
	private static final int BAYLOR = 29213;
	// Items
	private static final int MARK = 17591;
	private static final int EAR = 17527;
	
	public Q00180_InfernalFlamesBurningInCrystalPrison()
	{
		super(false);
		addStartNpc(FIOREN);
		addTalkId(FIOREN);
		addKillId(BAYLOR);
		addQuestItem(MARK);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("33044-06.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
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
		final Player player = qs.getPlayer();
		
		if (qs.getState() == CREATED)
		{
			if (player.getLevel() < 97)
			{
				htmltext = "33044-02.htm";
				qs.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "33044-01.htm";
			}
		}
		else if (qs.getState() == STARTED)
		{
			if (cond == 1)
			{
				htmltext = "33044-07.htm";
			}
			else
			{
				if (cond == 2)
				{
					htmltext = "33044-08.htm";
					qs.addExpAndSp(14000000, 6400000);
					qs.giveItems(EAR, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
			}
		}
		else if (qs.getState() == COMPLETED)
		{
			htmltext = "33044-03.htm";
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
				qs.giveItems(MARK, 1);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				for (Player pmember : player.getParty().getPartyMembers())
				{
					final QuestState pst = pmember.getQuestState("Q00180_InfernalFlamesBurningInCrystalPrison");
					
					if ((pst != null) && (pst.getCond() == 1))
					{
						pst.setCond(2);
						pst.giveItems(MARK, 1);
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
