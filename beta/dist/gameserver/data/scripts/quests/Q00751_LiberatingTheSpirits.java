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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

/**
 * @author cruel
 * @name 751 - Exemption Ghosts
 * @category Daily quest. Party
 */
public class Q00751_LiberatingTheSpirits extends Quest implements ScriptFile
{
	private static final int Roderik = 30631;
	private static final int Deadmans_Flesh = 34971;
	private static final int[] Mobs =
	{
		23199,
		23201,
		23202,
		23200,
		23203,
		23204,
		23205,
		23206,
		23207,
		23208,
		23209,
		23242,
		23243,
		23244,
		23245
	};
	private final int Scaldisect = 23212;
	private static final String SCALDISECT_KILL = "Scaldisect";
	
	public Q00751_LiberatingTheSpirits()
	{
		super(PARTY_ALL);
		addStartNpc(Roderik);
		addKillId(Mobs);
		addKillId(Scaldisect);
		addQuestItem(Deadmans_Flesh);
		addKillNpcWithLog(1, SCALDISECT_KILL, 1, Scaldisect);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30631-3.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30631-5.htm":
				qs.getPlayer().addExpAndSp(600000000, 0);
				qs.takeItems(Deadmans_Flesh, 40);
				qs.unset(SCALDISECT_KILL);
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
				if (qs.getPlayer().getLevel() >= 95)
				{
					if (qs.isNowAvailable())
					{
						htmltext = "30631.htm";
					}
					else
					{
						htmltext = "30631-0.htm";
					}
				}
				else
				{
					htmltext = "lvl.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "30631-3.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30631-4.htm";
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
			if (Util.contains(Mobs, npc.getId()))
			{
				final Party party = qs.getPlayer().getParty();
				
				if (party != null)
				{
					for (Player member : party.getPartyMembers())
					{
						final QuestState state = member.getQuestState(getClass());
						
						if ((state != null) && state.isStarted())
						{
							if (qs.getQuestItemsCount(Deadmans_Flesh) < 40)
							{
								state.giveItems(Deadmans_Flesh, 1);
								state.playSound(SOUND_ITEMGET);
								
								if (updateKill(npc, qs) && (qs.getQuestItemsCount(Deadmans_Flesh) == 40))
								{
									qs.setCond(2);
								}
							}
						}
					}
				}
				else
				{
					if (qs.getQuestItemsCount(Deadmans_Flesh) < 40)
					{
						qs.giveItems(Deadmans_Flesh, 1);
						qs.playSound(SOUND_ITEMGET);
						
						if (updateKill(npc, qs) && (qs.getQuestItemsCount(Deadmans_Flesh) == 40))
						{
							qs.setCond(2);
						}
					}
				}
			}
			
			if (npc.getId() == Scaldisect)
			{
				final Party party = qs.getPlayer().getParty();
				
				if (party != null)
				{
					for (Player member : party.getPartyMembers())
					{
						final QuestState state = member.getQuestState(getClass());
						
						if ((state != null) && state.isStarted())
						{
							updateKill(npc, qs);
							
							if (qs.getQuestItemsCount(Deadmans_Flesh) == 40)
							{
								qs.setCond(2);
							}
						}
					}
				}
				else
				{
					updateKill(npc, qs);
					
					if (qs.getQuestItemsCount(Deadmans_Flesh) == 40)
					{
						qs.setCond(2);
					}
				}
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