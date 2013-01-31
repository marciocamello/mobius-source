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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _637_ThroughOnceMore extends Quest implements ScriptFile
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
	
	public final int CHANCE = 40;
	public final int FLAURON = 32010;
	public final int VISITORSMARK = 8064;
	public final int NECROHEART = 8066;
	public final int MARK = 8067;
	
	public _637_ThroughOnceMore()
	{
		super(false);
		addStartNpc(FLAURON);
		addKillId(21565, 21566, 21567, 21568);
		addQuestItem(NECROHEART);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equals("falsepriest_flauron_q0637_04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.takeItems(VISITORSMARK, 1);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext;
		int cond = st.getCond();
		if (cond == 0)
		{
			if ((st.getPlayer().getLevel() > 72) && (st.getQuestItemsCount(VISITORSMARK) > 0) && (st.getQuestItemsCount(MARK) == 0))
			{
				htmltext = "falsepriest_flauron_q0637_02.htm";
			}
			else
			{
				htmltext = "falsepriest_flauron_q0637_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((cond == 2) && (st.getQuestItemsCount(NECROHEART) == 10))
		{
			htmltext = "falsepriest_flauron_q0637_05.htm";
			st.takeItems(NECROHEART, 10);
			st.giveItems(MARK, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else
		{
			htmltext = "falsepriest_flauron_q0637_04.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(NECROHEART);
		if ((st.getCond() == 1) && Rnd.chance(CHANCE) && (count < 10))
		{
			st.giveItems(NECROHEART, 1);
			if (count == 9)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
	
	@Override
	public void onAbort(QuestState st)
	{
		if (st.getQuestItemsCount(VISITORSMARK) == 0)
		{
			st.giveItems(VISITORSMARK, 1);
		}
	}
}
