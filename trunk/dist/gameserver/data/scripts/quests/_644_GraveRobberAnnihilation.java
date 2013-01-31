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

public class _644_GraveRobberAnnihilation extends Quest implements ScriptFile
{
	private static final int KARUDA = 32017;
	private static int ORC_GOODS = 8088;
	
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
	
	public _644_GraveRobberAnnihilation()
	{
		super(true);
		addStartNpc(KARUDA);
		addKillId(22003);
		addKillId(22004);
		addKillId(22005);
		addKillId(22006);
		addKillId(22008);
		addQuestItem(ORC_GOODS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("karuda_q0644_0103.htm"))
		{
			st.takeItems(ORC_GOODS, -1);
			if (st.getPlayer().getLevel() < 20)
			{
				htmltext = "karuda_q0644_0102.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
			}
		}
		if ((st.getCond() == 2) && (st.getQuestItemsCount(ORC_GOODS) >= 120))
		{
			if (event.equalsIgnoreCase("varn"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1865, 30, true);
				htmltext = null;
			}
			else if (event.equalsIgnoreCase("an_s"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1867, 40, true);
				htmltext = null;
			}
			else if (event.equalsIgnoreCase("an_b"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1872, 40, true);
				htmltext = null;
			}
			else if (event.equalsIgnoreCase("char"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1871, 30, true);
				htmltext = null;
			}
			else if (event.equalsIgnoreCase("coal"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1870, 30, true);
				htmltext = null;
			}
			else if (event.equalsIgnoreCase("i_o"))
			{
				st.takeItems(ORC_GOODS, -1);
				st.giveItems(1869, 30, true);
				htmltext = null;
			}
			if (htmltext == null)
			{
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
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
			htmltext = "karuda_q0644_0101.htm";
		}
		else if (cond == 1)
		{
			htmltext = "karuda_q0644_0106.htm";
		}
		else if (cond == 2)
		{
			if (st.getQuestItemsCount(ORC_GOODS) >= 120)
			{
				htmltext = "karuda_q0644_0105.htm";
			}
			else
			{
				st.setCond(1);
				htmltext = "karuda_q0644_0106.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && Rnd.chance(90))
		{
			st.giveItems(ORC_GOODS, 1, false);
			if (st.getQuestItemsCount(ORC_GOODS) >= 120)
			{
				st.setCond(2);
				st.setState(STARTED);
			}
		}
		return null;
	}
}
