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

import org.apache.commons.lang3.ArrayUtils;

public class _490_DutyOfTheSurvivor extends Quest implements ScriptFile
{
	private static final int VOLLODOS = 30137;
	private static final int EXTRACT_MOBS[] =
	{
		23162,
		23163,
		23164,
		23165,
		23166,
		23167
	};
	private static final int BLOOD_MOBS[] =
	{
		23168,
		23169,
		23170,
		23171,
		23172,
		23173
	};
	
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
	
	public _490_DutyOfTheSurvivor()
	{
		super(false);
		addStartNpc(VOLLODOS);
		addTalkId(VOLLODOS);
		addKillId(EXTRACT_MOBS);
		addKillId(BLOOD_MOBS);
		addQuestItem((new int[]
		{
			34059,
			34060
		}));
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		st.getCond();
		if (event.equalsIgnoreCase("30137-5.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == VOLLODOS)
		{
			if (st.getPlayer().getLevel() >= 85)
			{
				if (cond == 0)
				{
					if (st.isNowAvailableByTime())
					{
						htmltext = "30137.htm";
					}
					else
					{
						htmltext = "30137-comp.htm";
					}
				}
				else if (cond == 1)
				{
					if (st.haveQuestItem(34059) || st.haveQuestItem(34060))
					{
						htmltext = "30137-9.htm";
					}
					else
					{
						htmltext = "30137-6.htm";
					}
				}
				if (cond == 2)
				{
					st.takeItems(34059, -1);
					st.takeItems(34060, -1);
					st.addExpAndSp(145557000, 58119840);
					st.giveItems(57, 505062);
					st.unset("cond");
					st.exitCurrentQuest(this);
					st.playSound(SOUND_FINISH);
					htmltext = "30137-comp.htm";
				}
			}
			else
			{
				htmltext = "30137-lvl.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (ArrayUtils.contains(EXTRACT_MOBS, npc.getNpcId()) && (st.getQuestItemsCount(34059) < 20) && (Rnd.get(100) < 60))
		{
			st.giveItems(34059, 1);
			st.playSound(SOUND_ITEMGET);
		}
		if (ArrayUtils.contains(BLOOD_MOBS, npc.getNpcId()) && (st.getQuestItemsCount(34060) < 20) && (Rnd.get(100) < 60))
		{
			st.giveItems(34060, 1);
			st.playSound(SOUND_ITEMGET);
		}
		if ((st.getQuestItemsCount(34059) == 20) && (st.getQuestItemsCount(34060) == 20))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}