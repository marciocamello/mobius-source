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

public class Q00903_TheCallOfAntharas extends Quest implements ScriptFile
{
	// Npc
	private static final int Theodric = 30755;
	// Items
	private static final int BehemothDragonLeather = 21992;
	private static final int TaraskDragonsLeatherFragment = 21991;
	// Monsters
	private static final int TaraskDragon = 29190;
	private static final int BehemothDragon = 29069;
	
	public Q00903_TheCallOfAntharas()
	{
		super(PARTY_ALL);
		addStartNpc(Theodric);
		addKillId(TaraskDragon, BehemothDragon);
		addQuestItem(BehemothDragonLeather, TaraskDragonsLeatherFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "theodric_q903_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "theodric_q903_06.htm":
				qs.takeAllItems(BehemothDragonLeather);
				qs.takeAllItems(TaraskDragonsLeatherFragment);
				qs.giveItems(21897, 1);
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.isNowAvailableByTime())
				{
					if (qs.getPlayer().getLevel() >= 83)
					{
						if (qs.getQuestItemsCount(3865) > 0)
						{
							htmltext = "theodric_q903_01.htm";
						}
						else
						{
							htmltext = "theodric_q903_00b.htm";
						}
					}
					else
					{
						htmltext = "theodric_q903_00.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "theodric_q903_00a.htm";
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "theodric_q903_04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "theodric_q903_05.htm";
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
			switch (npc.getId())
			{
				case TaraskDragon:
					if (qs.getQuestItemsCount(TaraskDragonsLeatherFragment) < 1)
					{
						qs.giveItems(TaraskDragonsLeatherFragment, 1);
					}
					break;
				
				case BehemothDragon:
					if (qs.getQuestItemsCount(BehemothDragonLeather) < 1)
					{
						qs.giveItems(BehemothDragonLeather, 1);
					}
					break;
				
				default:
					break;
			}
			
			if ((qs.getQuestItemsCount(BehemothDragonLeather) > 0) && (qs.getQuestItemsCount(TaraskDragonsLeatherFragment) > 0))
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
