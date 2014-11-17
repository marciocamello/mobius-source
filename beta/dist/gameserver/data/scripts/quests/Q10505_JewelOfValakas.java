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
public class Q10505_JewelOfValakas extends Quest implements ScriptFile
{
	// Npc
	private static final int KLEIN = 31540;
	// Monster
	private static final int VALAKAS = 29028;
	// Items
	private static final int EMPTY_CRYSTAL = 21906;
	private static final int FILLED_CRYSTAL_VALAKAS = 21908;
	private static final int VACUALITE_FLOATING_STONE = 7267;
	private static final int JEWEL_OF_VALAKAS = 21896;
	
	public Q10505_JewelOfValakas()
	{
		super(PARTY_ALL);
		addStartNpc(KLEIN);
		addQuestItem(EMPTY_CRYSTAL, FILLED_CRYSTAL_VALAKAS);
		addKillId(VALAKAS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("valakas_watchman_klein_q10505_04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			qs.giveItems(EMPTY_CRYSTAL, 1);
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
					htmltext = "valakas_watchman_klein_q10505_00.htm";
				}
				else if (qs.getQuestItemsCount(VACUALITE_FLOATING_STONE) < 1)
				{
					htmltext = "valakas_watchman_klein_q10505_00a.htm";
				}
				else if (qs.isNowAvailable())
				{
					htmltext = "valakas_watchman_klein_q10505_01.htm";
				}
				else
				{
					htmltext = "valakas_watchman_klein_q10505_09.htm";
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(EMPTY_CRYSTAL) < 1)
				{
					htmltext = "valakas_watchman_klein_q10505_08.htm";
					qs.giveItems(EMPTY_CRYSTAL, 1);
				}
				else
				{
					htmltext = "valakas_watchman_klein_q10505_05.htm";
				}
				break;
			
			case 2:
				if (qs.getQuestItemsCount(FILLED_CRYSTAL_VALAKAS) >= 1)
				{
					htmltext = "valakas_watchman_klein_q10505_07.htm";
					qs.takeAllItems(FILLED_CRYSTAL_VALAKAS);
					qs.giveItems(JEWEL_OF_VALAKAS, 1);
					qs.playSound(SOUND_FINISH);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "valakas_watchman_klein_q10505_06.htm";
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
			qs.takeAllItems(EMPTY_CRYSTAL);
			qs.giveItems(FILLED_CRYSTAL_VALAKAS, 1);
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