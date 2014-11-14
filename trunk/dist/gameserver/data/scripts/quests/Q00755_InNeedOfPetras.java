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
import lineage2.gameserver.utils.Util;

/**
 * @author GodWorld & Bonux
 **/
public class Q00755_InNeedOfPetras extends Quest implements ScriptFile
{
	// NPCs
	private static final int AKU = 33671;
	// Monsters
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
	// Items
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
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("sofa_aku_q0755_04.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isStarted())
		{
			if (cond == 1)
			{
				htmltext = "sofa_aku_q0755_07.htm";
			}
			else if (cond == 2)
			{
				qs.takeItems(PETRA, -1L);
				qs.giveItems(AKUS_SUPPLY_BOX, 1);
				qs.giveItems(ENERGY_OF_DESTRUCTION, 1);
				qs.exitCurrentQuest(this);
				htmltext = "sofa_aku_q0755_08.htm";
			}
		}
		else
		{
			if (isAvailableFor(qs.getPlayer()))
			{
				if (qs.isNowAvailable())
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
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(MONSTERS, npc.getId()) && Rnd.chance(PETRA_DROP_CHANCE))
		{
			qs.giveItems(PETRA, 1);
			
			if (qs.getQuestItemsCount(PETRA) >= 50)
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