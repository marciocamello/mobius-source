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

public class Q00696_ConquerTheHallOfErosion extends Quest implements ScriptFile
{
	// Npc
	private static final int TEPIOS = 32603;
	// Monster
	private static final int COHEMENES = 25634;
	// Items
	private static final int MARK_OF_KEUCEREUS_STAGE_1 = 13691;
	private static final int MARK_OF_KEUCEREUS_STAGE_2 = 13692;
	
	public Q00696_ConquerTheHallOfErosion()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
		addKillId(COHEMENES);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("tepios_q696_3.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getCond())
		{
			case 0:
				if (player.getLevel() >= 75)
				{
					if ((qs.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_1) > 0) || (qs.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) > 0))
					{
						htmltext = "tepios_q696_1.htm";
					}
					else
					{
						htmltext = "tepios_q696_6.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "tepios_q696_0.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (qs.getInt("cohemenesDone") != 0)
				{
					if (qs.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) < 1)
					{
						qs.takeAllItems(MARK_OF_KEUCEREUS_STAGE_1);
						qs.giveItems(MARK_OF_KEUCEREUS_STAGE_2, 1);
					}
					
					htmltext = "tepios_q696_5.htm";
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "tepios_q696_1a.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (npc.getId() == COHEMENES)
		{
			qs.set("cohemenesDone", 1);
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
