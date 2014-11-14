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

import java.util.StringTokenizer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00905_RefinedDragonBlood extends Quest implements ScriptFile
{
	private static final int[] SeparatedSoul =
	{
		32864,
		32865,
		32866,
		32867,
		32868,
		32869,
		32870
	};
	private static final int[] AntharasDragonsBlue =
	{
		22852,
		22853,
		22844,
		22845
	};
	private static final int[] AntharasDragonsRed =
	{
		22848,
		22849,
		22850,
		22851
	};
	private static final int UnrefinedRedDragonBlood = 21913;
	private static final int UnrefinedBlueDragonBlood = 21914;
	
	public Q00905_RefinedDragonBlood()
	{
		super(PARTY_ALL);
		addStartNpc(SeparatedSoul);
		addKillId(AntharasDragonsBlue);
		addKillId(AntharasDragonsRed);
		addQuestItem(UnrefinedRedDragonBlood, UnrefinedBlueDragonBlood);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("sepsoul_q905_05.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.startsWith("sepsoul_q905_08.htm"))
		{
			qs.takeAllItems(AntharasDragonsBlue);
			qs.takeAllItems(AntharasDragonsRed);
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1:
					qs.giveItems(21903, 1);
					break;
				
				case 2:
					qs.giveItems(21904, 1);
					break;
				
				default:
					break;
			}
			
			htmltext = "sepsoul_q905_08.htm";
			qs.setState(COMPLETED);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(this);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (Util.contains(SeparatedSoul, npc.getId()))
		{
			switch (qs.getState())
			{
				case CREATED:
					if (qs.isNowAvailableByTime())
					{
						if (qs.getPlayer().getLevel() >= 83)
						{
							htmltext = "sepsoul_q905_01.htm";
						}
						else
						{
							htmltext = "sepsoul_q905_00.htm";
							qs.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "sepsoul_q905_00a.htm";
					}
					break;
				
				case STARTED:
					if (cond == 1)
					{
						htmltext = "sepsoul_q905_06.htm";
					}
					else if (cond == 2)
					{
						htmltext = "sepsoul_q905_07.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			if (Util.contains(AntharasDragonsBlue, npc.getId()))
			{
				if ((qs.getQuestItemsCount(UnrefinedBlueDragonBlood) < 10) && Rnd.chance(70))
				{
					qs.giveItems(UnrefinedBlueDragonBlood, 1);
				}
			}
			else if (Util.contains(AntharasDragonsRed, npc.getId()))
			{
				if ((qs.getQuestItemsCount(UnrefinedRedDragonBlood) < 10) && Rnd.chance(70))
				{
					qs.giveItems(UnrefinedRedDragonBlood, 1);
				}
			}
			
			if ((qs.getQuestItemsCount(UnrefinedBlueDragonBlood) >= 10) && (qs.getQuestItemsCount(UnrefinedRedDragonBlood) >= 10))
			{
				qs.setCond(2);
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
