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

public class _10351_OwnerOfHall extends Quest implements ScriptFile
{
	// npc
	private static final int TIPIA_NORMAL = 32892;
	
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
	
	public _10351_OwnerOfHall()
	{
		super(true);
		addStartNpc(TIPIA_NORMAL);
		addTalkId(TIPIA_NORMAL);
		addKillId(29212); // octavius
		
		addQuestCompletedCheck(_10318_DecayingDarkness.class);
		
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32892-7.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("32892-10.htm"))
		{
			st.giveItems(57, 23655000);
			st.giveItems(19461, 1);
			st.addExpAndSp(897850000, 416175000);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (player.getLevel() < 95)
		{
			return "32892-lvl.htm";
		}
		QuestState qs = st.getPlayer().getQuestState(_10318_DecayingDarkness.class);
		if ((qs == null) || !qs.isCompleted())
		{
			return "32892-lvl.htm";
		}
		if (npcId == TIPIA_NORMAL)
		{
			if (cond == 0)
			{
				return "32892.htm";
			}
			if (cond == 1)
			{
				return "32892-8.htm";
			}
			if (cond == 2)
			{
				return "32892-9.htm";
			}
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() != 1)
		{
			return null;
		}
		st.setCond(2);
		return null;
	}
}