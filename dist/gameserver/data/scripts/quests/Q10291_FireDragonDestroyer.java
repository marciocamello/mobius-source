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

public class Q10291_FireDragonDestroyer extends Quest implements ScriptFile
{
	// Npc
	private static final int Klein = 31540;
	// Items
	private static final int PoorNecklace = 15524;
	private static final int ValorNecklace = 15525;
	// Monster
	private static final int Valakas = 29028;
	
	public Q10291_FireDragonDestroyer()
	{
		super(PARTY_ALL);
		addStartNpc(Klein);
		addQuestItem(PoorNecklace, ValorNecklace);
		addKillId(Valakas);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("klein_q10291_04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			qs.giveItems(PoorNecklace, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				if ((qs.getPlayer().getLevel() >= 83) && (qs.getQuestItemsCount(7267) >= 1))
				{
					htmltext = "klein_q10291_01.htm";
				}
				else if (qs.getQuestItemsCount(7267) < 1)
				{
					htmltext = "klein_q10291_00a.htm";
				}
				else
				{
					htmltext = "klein_q10291_00.htm";
				}
				break;
			
			case 1:
				htmltext = "klein_q10291_05.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(ValorNecklace) >= 1)
				{
					htmltext = "klein_q10291_07.htm";
					qs.takeAllItems(ValorNecklace);
					qs.giveItems(8567, 1);
					qs.giveItems(ADENA_ID, 126549);
					qs.addExpAndSp(717291, 77397);
					qs.playSound(SOUND_FINISH);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "klein_q10291_06.htm";
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
			qs.takeAllItems(PoorNecklace);
			qs.giveItems(ValorNecklace, 1);
			qs.setCond(2);
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
