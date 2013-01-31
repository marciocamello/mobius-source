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

public class _300_HuntingLetoLizardman extends Quest implements ScriptFile
{
	private static int RATH = 30126;
	private static int BRACELET_OF_LIZARDMAN = 7139;
	private static int ANIMAL_BONE = 1872;
	private static int ANIMAL_SKIN = 1867;
	private static int BRACELET_OF_LIZARDMAN_CHANCE = 70;
	
	public _300_HuntingLetoLizardman()
	{
		super(false);
		addStartNpc(RATH);
		for (int lizardman_id = 20577; lizardman_id <= 20582; lizardman_id++)
		{
			addKillId(lizardman_id);
		}
		addQuestItem(BRACELET_OF_LIZARDMAN);
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (npc.getNpcId() != RATH)
		{
			return htmltext;
		}
		if (st.getState() == CREATED)
		{
			if (st.getPlayer().getLevel() < 34)
			{
				htmltext = "rarshints_q0300_0103.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "rarshints_q0300_0101.htm";
				st.setCond(0);
			}
		}
		else if (st.getQuestItemsCount(BRACELET_OF_LIZARDMAN) < 60)
		{
			htmltext = "rarshints_q0300_0106.htm";
			st.setCond(1);
		}
		else
		{
			htmltext = "rarshints_q0300_0105.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int _state = st.getState();
		if (event.equalsIgnoreCase("rarshints_q0300_0104.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("rarshints_q0300_0201.htm") && (_state == STARTED))
		{
			if (st.getQuestItemsCount(BRACELET_OF_LIZARDMAN) < 60)
			{
				htmltext = "rarshints_q0300_0202.htm";
				st.setCond(1);
			}
			else
			{
				st.takeItems(BRACELET_OF_LIZARDMAN, -1);
				switch (Rnd.get(3))
				{
					case 0:
						st.giveItems(ADENA_ID, 30000, true);
						break;
					case 1:
						st.giveItems(ANIMAL_BONE, 50, true);
						break;
					case 2:
						st.giveItems(ANIMAL_SKIN, 50, true);
						break;
				}
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
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
		long _count = qs.getQuestItemsCount(BRACELET_OF_LIZARDMAN);
		if ((_count < 60) && Rnd.chance(BRACELET_OF_LIZARDMAN_CHANCE))
		{
			qs.giveItems(BRACELET_OF_LIZARDMAN, 1);
			if (_count == 59)
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
