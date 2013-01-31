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

public class _331_ArrowForVengeance extends Quest implements ScriptFile
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
	
	private static final int HARPY_FEATHER = 1452;
	private static final int MEDUSA_VENOM = 1453;
	private static final int WYRMS_TOOTH = 1454;
	
	public _331_ArrowForVengeance()
	{
		super(false);
		addStartNpc(30125);
		addKillId(new int[]
		{
			20145,
			20158,
			20176
		});
		addQuestItem(new int[]
		{
			HARPY_FEATHER,
			MEDUSA_VENOM,
			WYRMS_TOOTH
		});
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("beltkem_q0331_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("beltkem_q0331_06.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
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
			if (st.getPlayer().getLevel() >= 32)
			{
				htmltext = "beltkem_q0331_02.htm";
				return htmltext;
			}
			htmltext = "beltkem_q0331_01.htm";
			st.exitCurrentQuest(true);
		}
		else if (cond == 1)
		{
			if ((st.getQuestItemsCount(HARPY_FEATHER) + st.getQuestItemsCount(MEDUSA_VENOM) + st.getQuestItemsCount(WYRMS_TOOTH)) > 0)
			{
				st.giveItems(ADENA_ID, (80 * st.getQuestItemsCount(HARPY_FEATHER)) + (90 * st.getQuestItemsCount(MEDUSA_VENOM)) + (100 * st.getQuestItemsCount(WYRMS_TOOTH)), false);
				st.takeItems(HARPY_FEATHER, -1);
				st.takeItems(MEDUSA_VENOM, -1);
				st.takeItems(WYRMS_TOOTH, -1);
				htmltext = "beltkem_q0331_05.htm";
			}
			else
			{
				htmltext = "beltkem_q0331_04.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() > 0)
		{
			switch (npc.getNpcId())
			{
				case 20145:
					st.rollAndGive(HARPY_FEATHER, 1, 33);
					break;
				case 20158:
					st.rollAndGive(MEDUSA_VENOM, 1, 33);
					break;
				case 20176:
					st.rollAndGive(WYRMS_TOOTH, 1, 33);
					break;
			}
		}
		return null;
	}
}
