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

public class _368_TrespassingIntoTheSacredArea extends Quest implements ScriptFile
{
	private static int RESTINA = 30926;
	private static int BLADE_STAKATO_FANG = 5881;
	private static int BLADE_STAKATO_FANG_BASECHANCE = 10;
	
	public _368_TrespassingIntoTheSacredArea()
	{
		super(false);
		addStartNpc(RESTINA);
		for (int Blade_Stakato_id = 20794; Blade_Stakato_id <= 20797; Blade_Stakato_id++)
		{
			addKillId(Blade_Stakato_id);
		}
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (npc.getNpcId() != RESTINA)
		{
			return htmltext;
		}
		if (st.getState() == CREATED)
		{
			if (st.getPlayer().getLevel() < 36)
			{
				htmltext = "30926-00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30926-01.htm";
				st.setCond(0);
			}
		}
		else
		{
			long _count = st.getQuestItemsCount(BLADE_STAKATO_FANG);
			if (_count > 0)
			{
				htmltext = "30926-04.htm";
				st.takeItems(BLADE_STAKATO_FANG, -1);
				st.giveItems(ADENA_ID, _count * 2250);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				htmltext = "30926-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int _state = st.getState();
		if (event.equalsIgnoreCase("30926-02.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30926-05.htm") && (_state == STARTED))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		if (Rnd.chance((npc.getNpcId() - 20794) + BLADE_STAKATO_FANG_BASECHANCE))
		{
			qs.giveItems(BLADE_STAKATO_FANG, 1);
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
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
