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

public class _352_HelpRoodRaiseANewPet extends Quest implements ScriptFile
{
	private static int Rood = 31067;
	private static int Lienrik = 20786;
	private static int Lienrik_Lad = 20787;
	private static int LIENRIK_EGG1 = 5860;
	private static int LIENRIK_EGG2 = 5861;
	private static int LIENRIK_EGG1_Chance = 30;
	private static int LIENRIK_EGG2_Chance = 7;
	
	public _352_HelpRoodRaiseANewPet()
	{
		super(false);
		addStartNpc(Rood);
		addKillId(Lienrik);
		addKillId(Lienrik_Lad);
		addQuestItem(LIENRIK_EGG1);
		addQuestItem(LIENRIK_EGG2);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int _state = st.getState();
		if (event.equalsIgnoreCase("31067-04.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31067-09.htm") && (_state == STARTED))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (npc.getNpcId() != Rood)
		{
			return htmltext;
		}
		int _state = st.getState();
		if (_state == CREATED)
		{
			if (st.getPlayer().getLevel() < 39)
			{
				htmltext = "31067-00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "31067-01.htm";
				st.setCond(0);
			}
		}
		else if (_state == STARTED)
		{
			long reward = (st.getQuestItemsCount(LIENRIK_EGG1) * 209) + (st.getQuestItemsCount(LIENRIK_EGG2) * 2050);
			if (reward > 0)
			{
				htmltext = "31067-08.htm";
				st.takeItems(LIENRIK_EGG1, -1);
				st.takeItems(LIENRIK_EGG2, -1);
				st.giveItems(ADENA_ID, reward);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				htmltext = "31067-05.htm";
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
		if (Rnd.chance(LIENRIK_EGG1_Chance))
		{
			qs.giveItems(LIENRIK_EGG1, 1);
			qs.playSound(SOUND_ITEMGET);
		}
		else if (Rnd.chance(LIENRIK_EGG2_Chance))
		{
			qs.giveItems(LIENRIK_EGG2, 1);
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
