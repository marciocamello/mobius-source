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

public class _365_DevilsLegacy extends Quest implements ScriptFile
{
	private static final int RANDOLF = 30095;
	int[] MOBS = new int[]
	{
		20836,
		29027,
		20845,
		21629,
		21630,
		29026
	};
	private static final int CHANCE_OF_DROP = 25;
	private static final int REWARD_PER_ONE = 5070;
	private static final int TREASURE_CHEST = 5873;
	
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
	
	public _365_DevilsLegacy()
	{
		super(false);
		addStartNpc(RANDOLF);
		addKillId(MOBS);
		addQuestItem(TREASURE_CHEST);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30095-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30095-5.htm"))
		{
			long count = st.getQuestItemsCount(TREASURE_CHEST);
			if (count > 0)
			{
				long reward = count * REWARD_PER_ONE;
				st.takeItems(TREASURE_CHEST, -1);
				st.giveItems(ADENA_ID, reward);
			}
			else
			{
				htmltext = "You don't have required items";
			}
		}
		else if (event.equalsIgnoreCase("30095-6.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 39)
			{
				htmltext = "30095-0.htm";
			}
			else
			{
				htmltext = "30095-0a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			if (st.getQuestItemsCount(TREASURE_CHEST) == 0)
			{
				htmltext = "30095-2.htm";
			}
			else
			{
				htmltext = "30095-4.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (Rnd.chance(CHANCE_OF_DROP))
		{
			st.giveItems(TREASURE_CHEST, 1);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
