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

public class Q00113_StatusOfTheBeaconTower extends Quest implements ScriptFile
{
	// Npcs
	private static final int MOIRA = 31979;
	private static final int TORRANT = 32016;
	// Item
	private static final int BOX = 8086;
	
	public Q00113_StatusOfTheBeaconTower()
	{
		super(false);
		addStartNpc(MOIRA);
		addTalkId(TORRANT);
		addQuestItem(BOX);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "seer_moirase_q0113_0104.htm":
				qs.setCond(1);
				qs.giveItems(BOX, 1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "torant_q0113_0201.htm":
				qs.giveItems(ADENA_ID, 247600);
				qs.addExpAndSp(1147830, 1352735);
				qs.takeItems(BOX, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (qs.getCond() == COMPLETED)
		{
			htmltext = "completed";
		}
		else if (npcId == MOIRA)
		{
			if (qs.getCond() == CREATED)
			{
				if (qs.getPlayer().getLevel() >= 40)
				{
					htmltext = "seer_moirase_q0113_0101.htm";
				}
				else
				{
					htmltext = "seer_moirase_q0113_0103.htm";
					qs.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "seer_moirase_q0113_0105.htm";
			}
		}
		else if ((npcId == TORRANT) && (qs.getQuestItemsCount(BOX) == 1))
		{
			htmltext = "torant_q0113_0101.htm";
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
