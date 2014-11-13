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

public class Q00119_LastImperialPrince extends Quest implements ScriptFile
{
	// Npcs
	private static final int SPIRIT = 31453;
	private static final int DEVORIN = 32009;
	// Item
	private static final int BROOCH = 7262;
	// Other
	private static final int AMOUNT = 407970;
	
	public Q00119_LastImperialPrince()
	{
		super(false);
		addStartNpc(SPIRIT);
		addTalkId(DEVORIN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "31453-4.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32009-2.htm":
				if (qs.getQuestItemsCount(BROOCH) < 1)
				{
					htmltext = "noquest";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "32009-3.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "31453-7.htm":
				qs.giveItems(ADENA_ID, AMOUNT, true);
				qs.addExpAndSp(1919448, 2100933);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (qs.getPlayer().getLevel() < 74)
		{
			htmltext = "<html><body>Quest for characters level 74 and above.</body></html>";
			qs.exitCurrentQuest(true);
			return htmltext;
		}
		else if (qs.getQuestItemsCount(BROOCH) < 1)
		{
			qs.exitCurrentQuest(true);
			return htmltext;
		}
		
		if (npcId != SPIRIT)
		{
			if ((npcId == DEVORIN) && (cond == 1))
			{
				htmltext = "32009-1.htm";
			}
		}
		else
		{
			if (cond == 0)
			{
				return "31453-1.htm";
			}
			else if (cond == 2)
			{
				return "31453-5.htm";
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
