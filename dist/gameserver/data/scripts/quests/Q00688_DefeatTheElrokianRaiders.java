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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00688_DefeatTheElrokianRaiders extends Quest implements ScriptFile
{
	// Npc
	private static final int DINN = 32105;
	// Monster
	private static final int ELROKI = 22214;
	// Item
	private static final int DINOSAUR_FANG_NECKLACE = 8785;
	// Other
	private static final int DROP_CHANCE = 50;
	
	public Q00688_DefeatTheElrokianRaiders()
	{
		super(false);
		addStartNpc(DINN);
		addTalkId(DINN);
		addKillId(ELROKI);
		addQuestItem(DINOSAUR_FANG_NECKLACE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final long count = qs.getQuestItemsCount(DINOSAUR_FANG_NECKLACE);
		
		switch (event)
		{
			case "32105-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32105-08.htm":
				if (count > 0)
				{
					qs.takeItems(DINOSAUR_FANG_NECKLACE, -1);
					qs.giveItems(ADENA_ID, count * 3000);
				}
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
			
			case "32105-06.htm":
				qs.takeItems(DINOSAUR_FANG_NECKLACE, -1);
				qs.giveItems(ADENA_ID, count * 3000);
				break;
			
			case "32105-07.htm":
				if (count >= 100)
				{
					qs.takeItems(DINOSAUR_FANG_NECKLACE, 100);
					qs.giveItems(ADENA_ID, 450000);
				}
				else
				{
					htmltext = "32105-04.htm";
				}
				break;
			
			case "None":
				htmltext = null;
				break;
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
				if (qs.getPlayer().getLevel() >= 75)
				{
					htmltext = "32105-01.htm";
				}
				else
				{
					htmltext = "32105-00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(DINOSAUR_FANG_NECKLACE) == 0)
				{
					htmltext = "32105-04.htm";
				}
				else
				{
					htmltext = "32105-05.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final long count = qs.getQuestItemsCount(DINOSAUR_FANG_NECKLACE);
		
		if ((qs.getCond() == 1) && (count < 100) && Rnd.chance(DROP_CHANCE))
		{
			long numItems = (int) Config.RATE_QUESTS_REWARD;
			
			if ((count + numItems) > 100)
			{
				numItems = 100 - count;
			}
			
			if ((count + numItems) >= 100)
			{
				qs.playSound("ItemSound.quest_middle");
			}
			else
			{
				qs.playSound("ItemSound.quest_itemget");
			}
			
			qs.giveItems(DINOSAUR_FANG_NECKLACE, numItems);
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
