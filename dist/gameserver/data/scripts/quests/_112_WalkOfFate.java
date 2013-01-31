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

public class _112_WalkOfFate extends Quest implements ScriptFile
{
	private static final int Livina = 30572;
	private static final int Karuda = 32017;
	private static final int EnchantD = 956;
	
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
	
	public _112_WalkOfFate()
	{
		super(false);
		addStartNpc(Livina);
		addTalkId(Karuda);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("karuda_q0112_0201.htm"))
		{
			st.addExpAndSp(112876, 5774);
			st.giveItems(ADENA_ID, (long) (22308 + (6000 * (st.getRateQuestsReward() - 1))), true);
			st.giveItems(EnchantD, 1, false);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("seer_livina_q0112_0104.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npcId == Livina)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 20)
				{
					htmltext = "seer_livina_q0112_0101.htm";
				}
				else
				{
					htmltext = "seer_livina_q0112_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "seer_livina_q0112_0105.htm";
			}
		}
		else if (npcId == Karuda)
		{
			if (cond == 1)
			{
				htmltext = "karuda_q0112_0101.htm";
			}
		}
		return htmltext;
	}
}
