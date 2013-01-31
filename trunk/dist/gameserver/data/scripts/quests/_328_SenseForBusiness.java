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

public class _328_SenseForBusiness extends Quest implements ScriptFile
{
	private final int SARIEN = 30436;
	private final int MONSTER_EYE_CARCASS = 1347;
	private final int MONSTER_EYE_LENS = 1366;
	private final int BASILISK_GIZZARD = 1348;
	
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
	
	public _328_SenseForBusiness()
	{
		super(false);
		addStartNpc(SARIEN);
		addKillId(20055);
		addKillId(20059);
		addKillId(20067);
		addKillId(20068);
		addKillId(20070);
		addKillId(20072);
		addQuestItem(MONSTER_EYE_CARCASS);
		addQuestItem(MONSTER_EYE_LENS);
		addQuestItem(BASILISK_GIZZARD);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("trader_salient_q0328_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("trader_salient_q0328_06.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext;
		int id = st.getState();
		if (id == CREATED)
		{
			st.setCond(0);
		}
		if (st.getCond() == 0)
		{
			if (st.getPlayer().getLevel() >= 36)
			{
				htmltext = "trader_salient_q0328_02.htm";
				return htmltext;
			}
			htmltext = "trader_salient_q0328_01.htm";
			st.exitCurrentQuest(true);
		}
		else
		{
			long carcass = st.getQuestItemsCount(MONSTER_EYE_CARCASS);
			long lenses = st.getQuestItemsCount(MONSTER_EYE_LENS);
			long gizzard = st.getQuestItemsCount(BASILISK_GIZZARD);
			if ((carcass + lenses + gizzard) > 0)
			{
				st.giveItems(ADENA_ID, (30 * carcass) + (2000 * lenses) + (75 * gizzard));
				st.takeItems(MONSTER_EYE_CARCASS, -1);
				st.takeItems(MONSTER_EYE_LENS, -1);
				st.takeItems(BASILISK_GIZZARD, -1);
				htmltext = "trader_salient_q0328_05.htm";
			}
			else
			{
				htmltext = "trader_salient_q0328_04.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int n = Rnd.get(1, 100);
		if (npcId == 20055)
		{
			if (n < 47)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 49)
			{
				st.giveItems(MONSTER_EYE_LENS, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20059)
		{
			if (n < 51)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 53)
			{
				st.giveItems(MONSTER_EYE_LENS, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20067)
		{
			if (n < 67)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 69)
			{
				st.giveItems(MONSTER_EYE_LENS, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20068)
		{
			if (n < 75)
			{
				st.giveItems(MONSTER_EYE_CARCASS, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (n < 77)
			{
				st.giveItems(MONSTER_EYE_LENS, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20070)
		{
			if (n < 50)
			{
				st.giveItems(BASILISK_GIZZARD, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		else if (npcId == 20072)
		{
			if (n < 51)
			{
				st.giveItems(BASILISK_GIZZARD, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
