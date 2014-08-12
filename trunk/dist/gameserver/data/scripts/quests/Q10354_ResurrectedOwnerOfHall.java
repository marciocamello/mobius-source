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

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author GodWorld & Bonux
 */
public class Q10354_ResurrectedOwnerOfHall extends Quest implements ScriptFile
{
	// NPC's
	private static final int LYDIA = 32892;
	private static final int OCTAVIS = 29212;
	// Item's
	private static final int BOTTLE_OF_OCTAVIS_SOUL = 34884;
	
	public Q10354_ResurrectedOwnerOfHall()
	{
		super(true);
		addStartNpc(LYDIA);
		addTalkId(LYDIA);
		addKillId(OCTAVIS);
		addKillNpcWithLog(1, "OCTAVIS", 1, OCTAVIS);
		addLevelCheck(95, 100);
		addQuestCompletedCheck(Q10351_OwnerOfHall.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("orbis_typia_q10354_07.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("orbis_typia_q10354_10.htm"))
		{
			Player player = st.getPlayer();
			htmltext = HtmCache.getInstance().getNotNull("quests/Q10354_ResurrectedOwnerOfHall/orbis_typia_q10354_10.htm", player);
			htmltext = htmltext.replace("<?name?>", player.getName());
			st.addExpAndSp(897850000, 416175000);
			st.giveItems(ADENA_ID, 23655000, true);
			st.giveItems(BOTTLE_OF_OCTAVIS_SOUL, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		String htmltext = "noquest";
		
		if (npcId == LYDIA)
		{
			if (st.isCompleted())
			{
				htmltext = "orbis_typia_q10354_03.htm";
			}
			else if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "orbis_typia_q10354_08.htm";
				}
				else if (cond == 2)
				{
					htmltext = "orbis_typia_q10354_09.htm";
				}
			}
			else
			{
				Player player = st.getPlayer();
				QuestState pst = player.getQuestState(Q10351_OwnerOfHall.class);
				
				if (player.getLevel() < 95)
				{
					htmltext = "orbis_typia_q10354_02.htm";
				}
				else if ((pst == null) || !pst.isCompleted())
				{
					htmltext = "orbis_typia_q10354_04.htm";
				}
				else
				{
					htmltext = "orbis_typia_q10354_01.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (npcId == OCTAVIS)
		{
			if (cond == 1)
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
		//
	}
	
	@Override
	public void onReload()
	{
		//
	}
	
	@Override
	public void onShutdown()
	{
		//
	}
}