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
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

public class _10330_ToTheYeSagiraRuins extends Quest implements ScriptFile
{
	private static final int ATRAN = 33448;
	private static final int RAXIS = 32977;
	
	public _10330_ToTheYeSagiraRuins()
	{
		super(false);
		addStartNpc(ATRAN);
		addTalkId(RAXIS);
		addLevelCheck(8, 25);
		addQuestCompletedCheck(_10329_BackupSeekers.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		
		if (event.equalsIgnoreCase("3.htm"))
		{
			st.set("cond", "1", true);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("6.htm"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.ARMOR_HAS_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
			st.giveItems(57, 62000);
			st.giveItems(29, 1);
			st.giveItems(22, 1);
			st.addExpAndSp(23000, 25000);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		
		if (npcId == ATRAN)
		{
			if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "1.htm";
				// else TODO
			}
			else if (cond == 1)
			{
				htmltext = "3.htm";
			}
		}
		else if (npcId == RAXIS)
		{
			if (cond == 1)
			{
				htmltext = "4.htm";
			}
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
