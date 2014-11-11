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
	private static final int Navari = 33931;
	private static final int Gereth = 33932;
	
	public Q10732_AForeignLand()
	{
		super(false);
		addStartNpc(Navari);
		addTalkId(Navari);
		addTalkId(Gereth);
		addLevelCheck(1, 20);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
				player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q014));
				htmltext = "33931-3.htm";
				break;
			
			case "qet_rev":
				st.showTutorialHTML(TutorialShowHtml.QT_001, TutorialShowHtml.TYPE_WINDOW);
				htmltext = "33932-2.htm";
				st.giveItems(57, 3000);
				st.getPlayer().addExpAndSp(75, 2);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		
		switch (npcId)
		{
			case Navari:
				if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "33931-1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "33931-4.htm";
				}
				break;
			
			case Gereth:
				if (cond == 0)
				{
					htmltext = "33932-3.htm";
				}
				else if (cond == 1)
				{
					htmltext = "33932-1.htm";
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