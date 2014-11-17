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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10288_SecretMission extends Quest implements ScriptFile
{
	// NPCs
	private static final int _dominic = 31350;
	private static final int _aquilani = 32780;
	private static final int _greymore = 32757;
	// Items
	private static final int _letter = 15529;
	
	public Q10288_SecretMission()
	{
		super(false);
		addStartNpc(_dominic, _aquilani);
		addTalkId(_dominic, _greymore, _aquilani);
		addFirstTalkId(_aquilani);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (npc.getId())
		{
			case _dominic:
				if (event.equals("31350-05.htm"))
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.giveItems(_letter, 1);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case _greymore:
				if (event.equals("32757-03.htm"))
				{
					qs.unset("cond");
					qs.takeItems(_letter, -1);
					qs.giveItems(57, 106583);
					qs.addExpAndSp(417788, 46320);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case _aquilani:
				if (qs.getState() == STARTED)
				{
					if (event.equals("32780-05.htm"))
					{
						qs.setCond(2);
						qs.playSound(SOUND_MIDDLE);
					}
				}
				else if ((qs.getState() == COMPLETED) && event.equals("teleport"))
				{
					qs.getPlayer().teleToLocation(118833, -80589, -2688);
					return null;
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (npc.getId())
		{
			case _dominic:
				switch (qs.getState())
				{
					case CREATED:
						if (qs.getPlayer().getLevel() >= 82)
						{
							htmltext = "31350-01.htm";
						}
						else
						{
							htmltext = "31350-00.htm";
						}
						
						break;
					
					case STARTED:
						if (qs.getCond() == 1)
						{
							htmltext = "31350-06.htm";
						}
						else if (qs.getCond() == 2)
						{
							htmltext = "31350-07.htm";
						}
						
						break;
					
					case COMPLETED:
						htmltext = "31350-08.htm";
						break;
				}
				break;
			
			case _aquilani:
				if (qs.getCond() == 1)
				{
					htmltext = "32780-03.htm";
				}
				else if (qs.getCond() == 2)
				{
					htmltext = "32780-06.htm";
				}
				break;
			
			case _greymore:
				if (qs.getCond() == 2)
				{
					htmltext = "32757-01.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		QuestState qs = player.getQuestState(getClass());
		
		if (qs == null)
		{
			newQuestState(player, CREATED);
			qs = player.getQuestState(getClass());
		}
		
		if (npc.getId() == _aquilani)
		{
			if (qs.getState() == COMPLETED)
			{
				return "32780-01.htm";
			}
			
			return "32780-00.htm";
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