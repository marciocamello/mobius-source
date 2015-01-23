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
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10330_ToTheRuinsOfYeSagira extends Quest implements ScriptFile
{
	// Npcs
	private static final int ATRAN = 33448;
	private static final int RAXIS = 32977;
	
	public Q10330_ToTheRuinsOfYeSagira()
	{
		super(false);
		addStartNpc(ATRAN);
		addTalkId(RAXIS);
		addLevelCheck(8, 25);
		addQuestCompletedCheck(Q10329_BackupSeekers.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "3.htm":
				qs.set("cond", "1", true);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "6.htm":
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcStringId.ARMOR_HAS_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
				qs.giveItems(57, 619);
				qs.giveItems(29, 1);
				qs.giveItems(22, 1);
				qs.addExpAndSp(23000, 2500);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getInt("cond");
		
		switch (npc.getId())
		{
			case ATRAN:
				if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "3.htm";
				}
				break;
			
			case RAXIS:
				if (cond == 1)
				{
					htmltext = "4.htm";
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
