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
import lineage2.gameserver.scripts.ScriptFile;

public class _636_TruthBeyond extends Quest implements ScriptFile
{
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
	
	public final int ELIYAH = 31329;
	public final int FLAURON = 32010;
	public final int MARK = 8067;
	public final int VISITORSMARK = 8064;
	
	public _636_TruthBeyond()
	{
		super(false);
		addStartNpc(ELIYAH);
		addTalkId(FLAURON);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equals("priest_eliyah_q0636_05.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("falsepriest_flauron_q0636_02.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.giveItems(VISITORSMARK, 1);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if ((npcId == ELIYAH) && (cond == 0))
		{
			if ((st.getQuestItemsCount(VISITORSMARK) == 0) && (st.getQuestItemsCount(MARK) == 0))
			{
				if (st.getPlayer().getLevel() > 72)
				{
					htmltext = "priest_eliyah_q0636_01.htm";
				}
				else
				{
					htmltext = "priest_eliyah_q0636_03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "priest_eliyah_q0636_06.htm";
			}
		}
		else if (npcId == FLAURON)
		{
			if (cond == 1)
			{
				htmltext = "falsepriest_flauron_q0636_01.htm";
				st.setCond(2);
			}
			else
			{
				htmltext = "falsepriest_flauron_q0636_03.htm";
			}
		}
		return htmltext;
	}
}
