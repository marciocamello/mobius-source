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

public class Q00431_WeddingMarch extends Quest implements ScriptFile
{
	// Npc
	private static final int MELODY_MAESTRO_KANTABILON = 31042;
	// Monsters
	private static final int LIENRIK = 20786;
	private static final int LIENRIK_LAD = 20787;
	// Items
	private static final int SILVER_CRYSTAL = 7540;
	private static final int WEDDING_ECHO_CRYSTAL = 7062;
	
	public Q00431_WeddingMarch()
	{
		super(false);
		addStartNpc(MELODY_MAESTRO_KANTABILON);
		addKillId(LIENRIK, LIENRIK_LAD);
		addQuestItem(SILVER_CRYSTAL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accept":
				htmltext = "muzyk_q0431_0104.htm";
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "431_3":
				if (qs.getQuestItemsCount(SILVER_CRYSTAL) == 50)
				{
					htmltext = "muzyk_q0431_0201.htm";
					qs.takeItems(SILVER_CRYSTAL, -1);
					qs.giveItems(WEDDING_ECHO_CRYSTAL, 25);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "muzyk_q0431_0202.htm";
				}
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
		final int id = qs.getState();
		
		if (id != STARTED)
		{
			if (qs.getPlayer().getLevel() < 38)
			{
				htmltext = "muzyk_q0431_0103.htm";
				qs.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "muzyk_q0431_0101.htm";
			}
		}
		else if (cond == 1)
		{
			htmltext = "muzyk_q0431_0106.htm";
		}
		else if ((cond == 2) && (qs.getQuestItemsCount(SILVER_CRYSTAL) == 50))
		{
			htmltext = "muzyk_q0431_0105.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if ((qs.getCond() == 1) && (qs.getQuestItemsCount(SILVER_CRYSTAL) < 50))
		{
			qs.giveItems(SILVER_CRYSTAL, 1);
			
			if (qs.getQuestItemsCount(SILVER_CRYSTAL) == 50)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
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
