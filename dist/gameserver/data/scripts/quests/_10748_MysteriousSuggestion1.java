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

/**
 * @author GodWorld & Bonux
 */
public class _10748_MysteriousSuggestion1 extends Quest implements ScriptFile
{
	// NPC'S
	private static final int MUSTERIOUS_BUTLER = 33685;
	// Item's
	private static final int TOURNAMENT_REMNANTS_I = 35544;
	private static final int MYSTERIOUS_MARK = 34900;
	
	public _10748_MysteriousSuggestion1()
	{
		super(false);
		addStartNpc(MUSTERIOUS_BUTLER);
		addTalkId(MUSTERIOUS_BUTLER);
		addQuestItem(TOURNAMENT_REMNANTS_I);
		addLevelCheck(76, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("grankain_lumiere_q10748_03.htm"))
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
		int cond = st.getCond();
		String htmltext = "noquest";
		
		if (npcId == MUSTERIOUS_BUTLER)
		{
			if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "grankain_lumiere_q10748_06.htm";
				}
				else if (cond == 2)
				{
					st.giveItems(MYSTERIOUS_MARK, 1);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					htmltext = "grankain_lumiere_q10748_07.htm";
				}
			}
			else
			{
				if (isAvailableFor(st.getPlayer()) && ((st.getPlayer().getClan() != null) || (st.getPlayer().getClan().getLevel() > 3)))
				{
					if (st.isNowAvailable())
					{
						htmltext = "grankain_lumiere_q10748_01.htm";
					}
					else
					{
						htmltext = "grankain_lumiere_q10748_05.htm";
					}
				}
				else
				{
					htmltext = "grankain_lumiere_q10748_04.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public void onLoad()
	{
		//
	}
	
	@Override
	public void onReload()
	{
		//
	}
	
	@Override
	public void onShutdown()
	{
		//
	}
}