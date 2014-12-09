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
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10334_WindmillHillStatusReport extends Quest implements ScriptFile
{
	// Npcs
	private static final int Batis = 30332;
	private static final int Shnain = 33508;
	// Items
	private static final int Sword = 2499;
	private static final int Atuba = 190;
	private static final int Dagg = 225;
	
	public Q10334_WindmillHillStatusReport()
	{
		super(false);
		addStartNpc(Shnain);
		addTalkId(Shnain, Batis);
		addLevelCheck(22, 40);
		addQuestCompletedCheck(Q10333_DisappearedSakum.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
				player.sendPacket(new ExShowScreenMessage(NpcString.WEAPONS_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				qs.getPlayer().addExpAndSp(200000, 6000);
				qs.giveItems(57, 849);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				if (player.isMageClass())
				{
					qs.giveItems(Atuba, 1, false);
				}
				else if ((player.getClassId().getId() == 7) || (player.getClassId().getId() == 35) || (player.getClassId().getId() == 7) || (player.getClassId().getId() == 125) || (player.getClassId().getId() == 126))
				{
					qs.giveItems(Dagg, 1, false);
				}
				else
				{
					qs.giveItems(Sword, 1, false);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Shnain:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-3.htm";
				}
				break;
			
			case Batis:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
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
