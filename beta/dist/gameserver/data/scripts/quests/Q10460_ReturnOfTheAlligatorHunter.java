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
 * @author Krash
 */
public class Q10460_ReturnOfTheAlligatorHunter extends Quest implements ScriptFile
{
	// Npcs
	private static final int Enron = 33860;
	// Monsters
	public static final int[] Monsters =
	{
		20135,
		20804,
		20805,
		20806,
		20807,
		20808
	};
	// Drops
	private static final int Alligator_Leather = 4337;
	private static final int Blue_Alligator_Leather = 4338;
	private static final int Bejeweled_Alligator_Leather = 4339;
	// Rewards
	private static final int Steel_Door_Guild = 37045;
	private static final int Enchant_Armor_C = 952;
	
	public Q10460_ReturnOfTheAlligatorHunter()
	{
		super(false);
		addStartNpc(Enron);
		addTalkId(Enron);
		addQuestItem(Alligator_Leather, Blue_Alligator_Leather, Bejeweled_Alligator_Leather);
		addKillId(Monsters);
		addLevelCheck(40, 46);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33860-2.htm";
				break;
			
			case "quest_finish":
				htmltext = "33860-5.htm";
				qs.takeItems(Alligator_Leather, 30);
				qs.takeItems(Blue_Alligator_Leather, 20);
				qs.takeItems(Bejeweled_Alligator_Leather, 10);
				qs.giveItems(Steel_Door_Guild, 26);
				qs.giveItems(Enchant_Armor_C, 7);
				qs.getPlayer().addExpAndSp(2795688, 670);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Enron:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33860-1.htm";
						}
						break;
					
					case 1:
						htmltext = "33860-3.htm";
						break;
					
					case 2:
						htmltext = "33860-4.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() != 1) || (npc == null))
		{
			return null;
		}
		
		if (Util.contains(Monsters, npc.getId()))
		{
			if (Rnd.chance(50))
			{
				if (qs.getQuestItemsCount(Alligator_Leather) < 30)
				{
					qs.giveItems(Alligator_Leather, 1);
				}
			}
			else if (qs.getQuestItemsCount(Blue_Alligator_Leather) < 20)
			{
				qs.giveItems(Blue_Alligator_Leather, 1);
			}
			else if (qs.getQuestItemsCount(Bejeweled_Alligator_Leather) < 10)
			{
				qs.giveItems(Bejeweled_Alligator_Leather, 1);
			}
			
			if ((qs.getQuestItemsCount(Alligator_Leather) >= 30) && (qs.getQuestItemsCount(Blue_Alligator_Leather) >= 20) && (qs.getQuestItemsCount(Bejeweled_Alligator_Leather) >= 10))
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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