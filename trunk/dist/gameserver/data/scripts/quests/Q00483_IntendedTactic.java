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

public class Q00483_IntendedTactic extends Quest implements ScriptFile
{
	// Npc
	private static final int ENDE = 33357;
	// Items
	private static final int LOYAL_SERVANTS_BLOOD = 17736;
	private static final int TRUTHFUL_ONES_BLOOD = 17737;
	private static final int TOKEN_OF_INSOLENCE = 17624;
	// Monsters
	private static final int[] MONSTERS1 =
	{
		23069,
		23070,
		23071,
		23072,
		23073,
		23074,
		23075
	};
	private static final int[] MONSTERS2 =
	{
		25809,
		25811,
		25812,
		25815
	};
	
	public Q00483_IntendedTactic()
	{
		super(false);
		addStartNpc(ENDE);
		addTalkId(ENDE);
		addKillId(MONSTERS1);
		addKillId(MONSTERS2);
		addQuestItem(TRUTHFUL_ONES_BLOOD, LOYAL_SERVANTS_BLOOD);
		addLevelCheck(48, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("33357-08.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
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
				if (qs.getPlayer().getLevel() >= 48)
				{
					htmltext = "33357-01.htm";
				}
				else
				{
					htmltext = "33357-02.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "33357-09.htm";
				}
				else
				{
					if (cond == 2)
					{
						if ((qs.getQuestItemsCount(LOYAL_SERVANTS_BLOOD) >= 10) && (qs.getQuestItemsCount(TRUTHFUL_ONES_BLOOD) >= 1))
						{
							htmltext = "33357-12.htm";
							qs.addExpAndSp(1500000, 1250000);
							qs.giveItems(TOKEN_OF_INSOLENCE, 1);
							qs.exitCurrentQuest(false);
						}
						else
						{
							if (qs.getQuestItemsCount(LOYAL_SERVANTS_BLOOD) >= 10)
							{
								htmltext = "33357-11.htm";
								qs.addExpAndSp(1500000, 1250000);
								qs.exitCurrentQuest(false);
							}
						}
					}
				}
				break;
			
			case COMPLETED:
				htmltext = "33357-03.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		switch (qs.getCond())
		{
			case 1:
				if ((Util.contains(MONSTERS1, npc.getId())) && (Rnd.chance(25)))
				{
					qs.giveItems(LOYAL_SERVANTS_BLOOD, 1);
					qs.playSound("SOUND_ITEMGET");
					
					if (qs.getQuestItemsCount(LOYAL_SERVANTS_BLOOD) >= 10L)
					{
						qs.setCond(2);
						qs.playSound("SOUND_MIDDLE");
					}
				}
				else if (Util.contains(MONSTERS2, npc.getId()))
				{
					if (qs.getQuestItemsCount(TRUTHFUL_ONES_BLOOD) <= 0)
					{
						qs.giveItems(TRUTHFUL_ONES_BLOOD, 1);
						qs.playSound("SOUND_ITEMGET");
					}
				}
				break;
			
			case 2:
				if (Util.contains(MONSTERS2, npc.getId()))
				{
					if (qs.getQuestItemsCount(TRUTHFUL_ONES_BLOOD) <= 0)
					{
						qs.giveItems(TRUTHFUL_ONES_BLOOD, 1);
						qs.playSound("SOUND_ITEMGET");
					}
				}
				break;
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
