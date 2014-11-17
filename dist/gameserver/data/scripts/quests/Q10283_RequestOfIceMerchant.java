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

public class Q10283_RequestOfIceMerchant extends Quest implements ScriptFile
{
	// Npcs
	private static final int _rafforty = 32020;
	private static final int _kier = 32022;
	private static final int _jinia = 32760;
	
	public Q10283_RequestOfIceMerchant()
	{
		super(false);
		addStartNpc(_rafforty);
		addTalkId(_rafforty, _kier, _jinia);
		addFirstTalkId(_jinia);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (npc == null)
		{
			return null;
		}
		
		switch (npc.getId())
		{
			case _rafforty:
				if (event.equals("32020-03.htm"))
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				else if (event.equals("32020-07.htm"))
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case _kier:
				if (event.equals("spawn"))
				{
					addSpawn(_jinia, 104322, -107669, -3680, 44954, 0, 60000);
					return null;
				}
				break;
			
			case _jinia:
				if (event.equals("32760-04.htm"))
				{
					qs.giveItems(57, 190000);
					qs.addExpAndSp(627000, 50300);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
					npc.deleteMe();
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
			case _rafforty:
				switch (qs.getState())
				{
					case CREATED:
						final QuestState _prev = qs.getPlayer().getQuestState(Q00115_TheOtherSideOfTruth.class);
						
						if ((_prev != null) && _prev.isCompleted() && (qs.getPlayer().getLevel() >= 82))
						{
							htmltext = "32020-01.htm";
						}
						else
						{
							htmltext = "32020-00.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case STARTED:
						if (qs.getCond() == 1)
						{
							htmltext = "32020-04.htm";
						}
						else if (qs.getCond() == 2)
						{
							htmltext = "32020-08.htm";
						}
						break;
					
					case COMPLETED:
						htmltext = "31350-08.htm";
						break;
				}
				break;
			
			case _kier:
				if (qs.getCond() == 2)
				{
					htmltext = "32022-01.htm";
				}
				break;
			
			case _jinia:
				if (qs.getCond() == 2)
				{
					htmltext = "32760-02.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getClass());
		
		if (qs == null)
		{
			return null;
		}
		
		if ((npc.getId() == _jinia) && (qs.getCond() == 2))
		{
			return "32760-01.htm";
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
