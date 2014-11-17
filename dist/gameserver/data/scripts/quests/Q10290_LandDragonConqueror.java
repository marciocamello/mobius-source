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

public class Q10290_LandDragonConqueror extends Quest implements ScriptFile
{
	// Npc
	private static final int Theodric = 30755;
	// Items
	private static final int ShabbyNecklace = 15522;
	private static final int MiracleNecklace = 15523;
	// Monster
	private static final int UltimateAntharas = 29068;
	
	public Q10290_LandDragonConqueror()
	{
		super(PARTY_ALL);
		addStartNpc(Theodric);
		addQuestItem(ShabbyNecklace, MiracleNecklace);
		addKillId(UltimateAntharas);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("theodric_q10290_04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			qs.giveItems(ShabbyNecklace, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				if ((qs.getPlayer().getLevel() >= 83) && (qs.getQuestItemsCount(3865) >= 1))
				{
					htmltext = "theodric_q10290_01.htm";
				}
				else if (qs.getQuestItemsCount(3865) < 1)
				{
					htmltext = "theodric_q10290_00a.htm";
				}
				else
				{
					htmltext = "theodric_q10290_00.htm";
				}
				break;
			
			case 1:
				htmltext = "theodric_q10290_05.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(MiracleNecklace) >= 1)
				{
					htmltext = "theodric_q10290_07.htm";
					qs.takeAllItems(MiracleNecklace);
					qs.giveItems(8568, 1);
					qs.giveItems(ADENA_ID, 131236);
					qs.addExpAndSp(702557, 76334);
					qs.playSound(SOUND_FINISH);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "theodric_q10290_06.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.takeAllItems(ShabbyNecklace);
			qs.giveItems(MiracleNecklace, 1);
			qs.setCond(2);
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
