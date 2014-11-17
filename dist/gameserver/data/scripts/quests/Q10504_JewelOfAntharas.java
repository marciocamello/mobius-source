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
 * @author Bonux
 */
public class Q10504_JewelOfAntharas extends Quest implements ScriptFile
{
	// Npc
	private static final int THEODRIC = 30755;
	// Monster
	private static final int ULTIMATE_ANTHARAS = 29068;
	// Items
	private static final int CLEAR_CRYSTAL = 21905;
	private static final int FILLED_CRYSTAL_ANTHARAS = 21907;
	private static final int PORTAL_STONE = 3865;
	private static final int JEWEL_OF_ANTHARAS = 21898;
	
	public Q10504_JewelOfAntharas()
	{
		super(PARTY_ALL);
		addStartNpc(THEODRIC);
		addQuestItem(CLEAR_CRYSTAL, FILLED_CRYSTAL_ANTHARAS);
		addKillId(ULTIMATE_ANTHARAS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("antharas_watchman_theodric_q10504_04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			qs.giveItems(CLEAR_CRYSTAL, 1);
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
				if (qs.getPlayer().getLevel() < 84)
				{
					htmltext = "antharas_watchman_theodric_q10504_00.htm";
				}
				else if (qs.getQuestItemsCount(PORTAL_STONE) < 1)
				{
					htmltext = "antharas_watchman_theodric_q10504_00a.htm";
				}
				else if (qs.isNowAvailable())
				{
					htmltext = "antharas_watchman_theodric_q10504_01.htm";
				}
				else
				{
					htmltext = "antharas_watchman_theodric_q10504_09.htm";
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(CLEAR_CRYSTAL) < 1)
				{
					htmltext = "antharas_watchman_theodric_q10504_08.htm";
					qs.giveItems(CLEAR_CRYSTAL, 1);
				}
				else
				{
					htmltext = "antharas_watchman_theodric_q10504_05.htm";
				}
				break;
			
			case 2:
				if (qs.getQuestItemsCount(FILLED_CRYSTAL_ANTHARAS) >= 1)
				{
					htmltext = "antharas_watchman_theodric_q10504_07.htm";
					qs.takeAllItems(FILLED_CRYSTAL_ANTHARAS);
					qs.giveItems(JEWEL_OF_ANTHARAS, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				else
				{
					htmltext = "antharas_watchman_theodric_q10504_06.htm";
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
			qs.takeAllItems(CLEAR_CRYSTAL);
			qs.giveItems(FILLED_CRYSTAL_ANTHARAS, 1);
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