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

public class Q10267_JourneyToGracia extends Quest implements ScriptFile
{
	// Npcs
	private final static int Orven = 30857;
	private final static int Keucereus = 32548;
	private final static int Papiku = 32564;
	// Item
	private final static int Letter = 13810;
	
	public Q10267_JourneyToGracia()
	{
		super(false);
		addStartNpc(Orven);
		addTalkId(Keucereus, Papiku);
		addQuestItem(Letter);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "30857-06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(Letter, 1);
				break;
			
			case "32564-02.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32548-02.htm":
				qs.giveItems(ADENA_ID, 1135000);
				qs.takeItems(Letter, -1);
				qs.addExpAndSp(5326400, 6000000);
				qs.unset("cond");
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case COMPLETED:
				if (npcId == Keucereus)
				{
					htmltext = "32548-03.htm";
				}
				else if (npcId == Orven)
				{
					htmltext = "30857-0a.htm";
				}
				break;
			
			case CREATED:
				if (npcId == Orven)
				{
					if (qs.getPlayer().getLevel() < 75)
					{
						htmltext = "30857-00.htm";
					}
					else
					{
						htmltext = "30857-01.htm";
					}
				}
				break;
			
			case STARTED:
				if (npcId == Orven)
				{
					htmltext = "30857-07.htm";
				}
				else if (npcId == Papiku)
				{
					if (cond == 1)
					{
						htmltext = "32564-01.htm";
					}
					else
					{
						htmltext = "32564-03.htm";
					}
				}
				else if ((npcId == Keucereus) && (cond == 2))
				{
					htmltext = "32548-01.htm";
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
