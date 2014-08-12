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

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author GodWorld & Bonux
 **/
public class Q00755_InNeedOfPetras extends Quest implements ScriptFile
{
	// NPC's
	private static final int AKU = 33671;
	// Monster's
	private static final int[] MONSTERS =
	{
		23213,
		23214,
		23227,
		23228,
		23229,
		23230,
		23215,
		23216,
		23217,
		23218,
		23231,
		23232,
		23233,
		23234,
		23237,
		23219
	};
	// Item's
	private static final int AKUS_SUPPLY_BOX = 35550;
	private static final int ENERGY_OF_DESTRUCTION = 35562;
	private static final int PETRA = 34959;
	// Other
	private static final double PETRA_DROP_CHANCE = 75.0;
	
	public Q00755_InNeedOfPetras()
	{
		super(true);
		addStartNpc(AKU);
		addTalkId(AKU);
		addKillId(MONSTERS);
		addQuestItem(PETRA);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("sofa_aku_q0755_04.htm"))
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
		
		if (npcId == AKU)
		{
			if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "sofa_aku_q0755_07.htm";
				}
				else if (cond == 2)
				{
					st.takeItems(PETRA, -1L);
					st.giveItems(AKUS_SUPPLY_BOX, 1);
					st.giveItems(ENERGY_OF_DESTRUCTION, 1);
					st.exitCurrentQuest(this);
					htmltext = "sofa_aku_q0755_08.htm";
				}
			}
			else
			{
				if (isAvailableFor(st.getPlayer()))
				{
					if (st.isNowAvailable())
					{
						htmltext = "sofa_aku_q0755_01.htm";
					}
					else
					{
						htmltext = "sofa_aku_q0755_06.htm";
					}
				}
				else
				{
					htmltext = "sofa_aku_q0755_05.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (ArrayUtils.contains(MONSTERS, npcId))
		{
			if (cond == 1)
			{
				if (Rnd.chance(PETRA_DROP_CHANCE))
				{
					st.giveItems(PETRA, 1);
					
					if (st.getQuestItemsCount(PETRA) >= 50)
					{
						st.setCond(2);
						st.playSound(SOUND_MIDDLE);
					}
					else
					{
						st.playSound(SOUND_ITEMGET);
					}
				}
			}
		}
		
		return null;
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