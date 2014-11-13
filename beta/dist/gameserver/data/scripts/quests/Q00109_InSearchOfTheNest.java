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

public class Q00109_InSearchOfTheNest extends Quest implements ScriptFile
{
	// Npcs
	private static final int PIERCE = 31553;
	private static final int CORPSE = 32015;
	private static final int KAHMAN = 31554;
	// Items
	private static final int MEMO = 8083;
	private static final int GOLDEN_BADGE_RECRUIT = 7246;
	private static final int GOLDEN_BADGE_SOLDIER = 7247;
	
	public Q00109_InSearchOfTheNest()
	{
		super(false);
		addStartNpc(PIERCE);
		addTalkId(CORPSE, KAHMAN);
		addQuestItem(MEMO);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "Memo":
				if (cond == 1)
				{
					qs.giveItems(MEMO, 1);
					qs.setCond(2);
					qs.playSound(SOUND_ITEMGET);
					htmltext = "You've find something...";
				}
				break;
			
			case "merc_cap_peace_q0109_0301.htm":
				if (cond == 2)
				{
					qs.takeItems(MEMO, -1);
					qs.setCond(3);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (qs.getState())
		{
			case CREATED:
				if ((qs.getPlayer().getLevel() >= 66) && (npcId == PIERCE) && ((qs.getQuestItemsCount(GOLDEN_BADGE_RECRUIT) > 0) || (qs.getQuestItemsCount(GOLDEN_BADGE_SOLDIER) > 0)))
				{
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					qs.setCond(1);
					htmltext = "merc_cap_peace_q0109_0105.htm";
				}
				else
				{
					htmltext = "merc_cap_peace_q0109_0103.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				switch (npcId)
				{
					case CORPSE:
						if (cond == 1)
						{
							htmltext = "corpse_of_scout_q0109_0101.htm";
						}
						else if (cond == 2)
						{
							htmltext = "corpse_of_scout_q0109_0203.htm";
						}
						break;
					
					case PIERCE:
						if (cond == 1)
						{
							htmltext = "merc_cap_peace_q0109_0304.htm";
						}
						else if (cond == 2)
						{
							htmltext = "merc_cap_peace_q0109_0201.htm";
						}
						else if (cond == 3)
						{
							htmltext = "merc_cap_peace_q0109_0303.htm";
						}
						break;
					
					case KAHMAN:
						if (cond == 3)
						{
							htmltext = "merc_kahmun_q0109_0401.htm";
							qs.addExpAndSp(8550000, 9950000);
							qs.giveItems(ADENA_ID, 1800000);
							qs.exitCurrentQuest(false);
							qs.playSound(SOUND_FINISH);
						}
						break;
				}
				break;
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
