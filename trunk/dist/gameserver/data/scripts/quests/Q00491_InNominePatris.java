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

public class Q00491_InNominePatris extends Quest implements ScriptFile
{
	// Npc
	private static final int Sirik = 33649;
	// Item
	private static final int Fragment = 34768;
	// Monsters
	private static final int[] Monsters =
	{
		23181,
		23182,
		23183,
		23184
	};
	// Others
	private static final int Chance = 50;
	private static final int[] Classes =
	{
		88,
		89,
		90,
		91,
		92,
		93,
		94,
		95,
		96,
		97,
		98,
		99,
		100,
		101,
		102,
		103,
		104,
		105,
		106,
		107,
		108,
		109,
		110,
		111,
		112,
		113,
		114,
		115,
		116,
		117,
		118,
		136,
		135,
		134,
		132,
		133
	};
	
	public Q00491_InNominePatris()
	{
		super(PARTY_ONE);
		addStartNpc(Sirik);
		addTalkId(Sirik);
		addKillId(Monsters);
		addQuestItem(Fragment);
		addLevelCheck(76, 81);
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
				htmltext = "0-4.htm";
				break;
			
			case "qet_rev":
				htmltext = "0-7.htm";
				qs.takeAllItems(Fragment);
				qs.exitCurrentQuest(this);
				qs.playSound(SOUND_FINISH);
				if (Rnd.chance(50))
				{
					qs.getPlayer().addExpAndSp(19000000, 21328000);
				}
				else
				{
					qs.getPlayer().addExpAndSp(14000000, 15171500);
				}
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
				if (Util.contains(Classes, qs.getPlayer().getClassId().getId()))
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						if (qs.isNowAvailableByTime())
						{
							htmltext = "start.htm";
						}
						else
						{
							htmltext = "0-c.htm";
						}
					}
					else
					{
						htmltext = "0-nc.htm";
					}
				}
				break;
			
			case 1:
				htmltext = "0-5.htm";
				break;
			
			case 2:
				htmltext = "0-6.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(Monsters, npc.getId()) && (qs.getQuestItemsCount(Fragment) < 50))
		{
			qs.rollAndGive(Fragment, 1, Chance);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(Fragment) >= 50)
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
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
