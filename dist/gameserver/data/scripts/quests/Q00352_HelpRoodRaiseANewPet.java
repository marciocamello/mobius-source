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

public class Q00352_HelpRoodRaiseANewPet extends Quest implements ScriptFile
{
	// NPCs
	private static int Rood = 31067;
	// Monsters
	private static int Lienrik = 20786;
	private static int Lienrik_Lad = 20787;
	// Items
	private static int LIENRIK_EGG1 = 5860;
	private static int LIENRIK_EGG2 = 5861;
	// Chances
	private static int LIENRIK_EGG1_Chance = 30;
	private static int LIENRIK_EGG2_Chance = 7;
	
	public Q00352_HelpRoodRaiseANewPet()
	{
		super(false);
		addStartNpc(Rood);
		addKillId(Lienrik, Lienrik_Lad);
		addQuestItem(LIENRIK_EGG1, LIENRIK_EGG2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		
		if (event.equals("31067-04.htm") && (_state == CREATED))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("31067-09.htm") && (_state == STARTED))
		{
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(true);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int _state = qs.getState();
		
		if (_state == CREATED)
		{
			if (qs.getPlayer().getLevel() < 39)
			{
				htmltext = "31067-00.htm";
				qs.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "31067-01.htm";
				qs.setCond(0);
			}
		}
		else if (_state == STARTED)
		{
			long reward = (qs.getQuestItemsCount(LIENRIK_EGG1) * 209) + (qs.getQuestItemsCount(LIENRIK_EGG2) * 2050);
			if (reward > 0)
			{
				htmltext = "31067-08.htm";
				qs.takeItems(LIENRIK_EGG1, -1);
				qs.takeItems(LIENRIK_EGG2, -1);
				qs.giveItems(ADENA_ID, reward);
				qs.playSound(SOUND_MIDDLE);
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