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
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author blacksmoke
 */
public class Q10732_AForeignLand extends Quest implements ScriptFile
{
	private static final int NAVARI = 33931;
	private static final int GERETH = 33932;
	
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
	
	public Q10732_AForeignLand()
	{
		super(false);
		addStartNpc(NAVARI);
		addTalkId(NAVARI, GERETH);
		addLevelCheck(1, 20);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q014));
			htmltext = "0-3.htm";
		}
		
		if (event.equalsIgnoreCase("qet_rev"))
		{
			st.showTutorialHTML(TutorialShowHtml.QT_001, TutorialShowHtml.TYPE_WINDOW);
			htmltext = "1-2.htm";
			st.giveItems(57, 3000);
			st.getPlayer().addExpAndSp(75, 2);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		
		if (npcId == NAVARI)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "0-1.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-4.htm";
			}
		}
		else if (npcId == GERETH)
		{
			if (st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = "1-t.htm";
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
		}
		
		return htmltext;
	}
}