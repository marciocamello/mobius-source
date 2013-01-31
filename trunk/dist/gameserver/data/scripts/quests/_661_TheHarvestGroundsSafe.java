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

public class _661_TheHarvestGroundsSafe extends Quest implements ScriptFile
{
	private static int NORMAN = 30210;
	private static int GIANT_POISON_BEE = 21095;
	private static int CLOYDY_BEAST = 21096;
	private static int YOUNG_ARANEID = 21097;
	private static int STING_OF_GIANT_POISON = 8283;
	private static int TALON_OF_YOUNG_ARANEID = 8285;
	private static int CLOUDY_GEM = 8284;
	
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
	
	public _661_TheHarvestGroundsSafe()
	{
		super(false);
		addStartNpc(NORMAN);
		addKillId(GIANT_POISON_BEE);
		addKillId(CLOYDY_BEAST);
		addKillId(YOUNG_ARANEID);
		addQuestItem(STING_OF_GIANT_POISON);
		addQuestItem(TALON_OF_YOUNG_ARANEID);
		addQuestItem(CLOUDY_GEM);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0103.htm") || event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0201.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0205.htm"))
		{
			long STING = st.getQuestItemsCount(STING_OF_GIANT_POISON);
			long TALON = st.getQuestItemsCount(TALON_OF_YOUNG_ARANEID);
			long GEM = st.getQuestItemsCount(CLOUDY_GEM);
			if ((STING + GEM + TALON) >= 10)
			{
				st.giveItems(ADENA_ID, (STING * 50) + (GEM * 60) + (TALON * 70) + 2800);
				st.takeItems(STING_OF_GIANT_POISON, -1);
				st.takeItems(TALON_OF_YOUNG_ARANEID, -1);
				st.takeItems(CLOUDY_GEM, -1);
			}
			else
			{
				st.giveItems(ADENA_ID, (STING * 50) + (GEM * 60) + (TALON * 70));
				st.takeItems(STING_OF_GIANT_POISON, -1);
				st.takeItems(TALON_OF_YOUNG_ARANEID, -1);
				st.takeItems(CLOUDY_GEM, -1);
			}
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("warehouse_keeper_norman_q0661_0204.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
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
			if (st.getPlayer().getLevel() >= 21)
			{
				htmltext = "warehouse_keeper_norman_q0661_0101.htm";
			}
			else
			{
				htmltext = "warehouse_keeper_norman_q0661_0102.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			if ((st.getQuestItemsCount(STING_OF_GIANT_POISON) + st.getQuestItemsCount(TALON_OF_YOUNG_ARANEID) + st.getQuestItemsCount(CLOUDY_GEM)) > 0)
			{
				htmltext = "warehouse_keeper_norman_q0661_0105.htm";
			}
			else
			{
				htmltext = "warehouse_keeper_norman_q0661_0206.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() != STARTED)
		{
			return null;
		}
		int npcId = npc.getNpcId();
		if (st.getCond() == 1)
		{
			if ((npcId == GIANT_POISON_BEE) && Rnd.chance(75))
			{
				st.giveItems(STING_OF_GIANT_POISON, 1);
				st.playSound(SOUND_ITEMGET);
			}
			if ((npcId == CLOYDY_BEAST) && Rnd.chance(71))
			{
				st.giveItems(CLOUDY_GEM, 1);
				st.playSound(SOUND_ITEMGET);
			}
			if ((npcId == YOUNG_ARANEID) && Rnd.chance(67))
			{
				st.giveItems(TALON_OF_YOUNG_ARANEID, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
