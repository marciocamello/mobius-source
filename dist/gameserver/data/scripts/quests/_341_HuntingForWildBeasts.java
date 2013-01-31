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

public class _341_HuntingForWildBeasts extends Quest implements ScriptFile
{
	private static int PANO = 30078;
	private static int Red_Bear = 20021;
	private static int Dion_Grizzly = 20203;
	private static int Brown_Bear = 20310;
	private static int Grizzly_Bear = 20335;
	private static int BEAR_SKIN = 4259;
	private static int BEAR_SKIN_CHANCE = 40;
	
	public _341_HuntingForWildBeasts()
	{
		super(false);
		addStartNpc(PANO);
		addKillId(Red_Bear);
		addKillId(Dion_Grizzly);
		addKillId(Brown_Bear);
		addKillId(Grizzly_Bear);
		addQuestItem(BEAR_SKIN);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept") && (st.getState() == CREATED))
		{
			htmltext = "pano_q0341_04.htm";
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
		if (npc.getNpcId() != PANO)
		{
			return htmltext;
		}
		int _state = st.getState();
		if (_state == CREATED)
		{
			if (st.getPlayer().getLevel() >= 20)
			{
				htmltext = "pano_q0341_01.htm";
				st.setCond(0);
			}
			else
			{
				htmltext = "pano_q0341_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (_state == STARTED)
		{
			if (st.getQuestItemsCount(BEAR_SKIN) >= 20)
			{
				htmltext = "pano_q0341_05.htm";
				st.takeItems(BEAR_SKIN, -1);
				st.giveItems(ADENA_ID, 3710);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "pano_q0341_06.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		long BEAR_SKIN_COUNT = qs.getQuestItemsCount(BEAR_SKIN);
		if ((BEAR_SKIN_COUNT < 20) && Rnd.chance(BEAR_SKIN_CHANCE))
		{
			qs.giveItems(BEAR_SKIN, 1);
			if (BEAR_SKIN_COUNT == 19)
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
			}
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
