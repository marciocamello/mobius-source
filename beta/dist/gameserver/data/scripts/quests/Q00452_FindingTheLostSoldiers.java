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
import lineage2.gameserver.utils.Util;

public class Q00452_FindingTheLostSoldiers extends Quest implements ScriptFile
{
	// Npcs
	private static final int JAKAN = 32773;
	private static final int[] SOLDIER_CORPSES =
	{
		32769,
		32770,
		32771,
		32772
	};
	// Item
	private static final int TAG_ID = 15513;
	
	public Q00452_FindingTheLostSoldiers()
	{
		super(false);
		addStartNpc(JAKAN);
		addTalkId(JAKAN);
		addTalkId(SOLDIER_CORPSES);
		addQuestItem(TAG_ID);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (npc == null)
		{
			return event;
		}
		
		if (npc.getId() == JAKAN)
		{
			if (event.equals("32773-3.htm"))
			{
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
			}
		}
		else if (Util.contains(SOLDIER_CORPSES, npc.getId()) && (qs.getCond() == 1))
		{
			qs.giveItems(TAG_ID, 1);
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
			npc.deleteMe();
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		if (npc == null)
		{
			return htmltext;
		}
		
		if (npc.getId() == JAKAN)
		{
			switch (qs.getState())
			{
				case CREATED:
					if (qs.getPlayer().getLevel() >= 84)
					{
						if (qs.isNowAvailableByTime())
						{
							htmltext = "32773-1.htm";
						}
						else
						{
							htmltext = "32773-6.htm";
						}
					}
					else
					{
						htmltext = "32773-0.htm";
					}
					
					break;
				
				case STARTED:
					if (qs.getCond() == 1)
					{
						htmltext = "32773-4.htm";
					}
					else if (qs.getCond() == 2)
					{
						htmltext = "32773-5.htm";
						qs.unset("cond");
						qs.takeItems(TAG_ID, 1);
						qs.giveItems(57, 95200);
						qs.addExpAndSp(435024, 50366);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(this);
					}
					
					break;
			}
		}
		else if (Util.contains(SOLDIER_CORPSES, npc.getId()))
		{
			if (qs.getCond() == 1)
			{
				htmltext = "corpse-1.htm";
			}
		}
		
		return htmltext;
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
