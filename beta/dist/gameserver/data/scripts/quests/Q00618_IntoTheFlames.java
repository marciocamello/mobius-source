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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00618_IntoTheFlames extends Quest implements ScriptFile
{
	// Npcs
	private static final int KLEIN = 31540;
	private static final int HILDA = 31271;
	// Items
	private static final int VACUALITE_ORE = 7265;
	private static final int VACUALITE = 7266;
	private static final int FLOATING_STONE = 7267;
	// Other
	private static final int CHANCE_FOR_QUEST_ITEMS = 50;
	
	public Q00618_IntoTheFlames()
	{
		super(false);
		addStartNpc(KLEIN);
		addTalkId(HILDA);
		addKillId(21274, 21275, 21276, 21278, 21282, 21283, 21284, 21286, 21290, 21291, 21292, 21294);
		addQuestItem(VACUALITE_ORE, VACUALITE, FLOATING_STONE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "watcher_valakas_klein_q0618_0104.htm":
				if (cond == 0)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "watcher_valakas_klein_q0618_0401.htm":
				if ((qs.getQuestItemsCount(VACUALITE) > 0))
				{
					if (cond == 4)
					{
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(true);
						qs.giveItems(FLOATING_STONE, 1);
					}
				}
				else
				{
					htmltext = "watcher_valakas_klein_q0618_0104.htm";
				}
				break;
			
			case "blacksmith_hilda_q0618_0201.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "blacksmith_hilda_q0618_0301.htm":
				if ((cond == 3) && (qs.getQuestItemsCount(VACUALITE_ORE) == 50))
				{
					qs.takeItems(VACUALITE_ORE, -1);
					qs.giveItems(VACUALITE, 1);
					qs.setCond(4);
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					htmltext = "blacksmith_hilda_q0618_0203.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case KLEIN:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() < 60)
					{
						htmltext = "watcher_valakas_klein_q0618_0103.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "watcher_valakas_klein_q0618_0101.htm";
					}
				}
				else if ((cond == 4) && (qs.getQuestItemsCount(VACUALITE) > 0))
				{
					htmltext = "watcher_valakas_klein_q0618_0301.htm";
				}
				else
				{
					htmltext = "watcher_valakas_klein_q0618_0104.htm";
				}
				break;
			
			case HILDA:
				if (cond == 1)
				{
					htmltext = "blacksmith_hilda_q0618_0101.htm";
				}
				else if ((cond == 3) && (qs.getQuestItemsCount(VACUALITE_ORE) >= 50))
				{
					htmltext = "blacksmith_hilda_q0618_0202.htm";
				}
				else if (cond == 4)
				{
					htmltext = "blacksmith_hilda_q0618_0303.htm";
				}
				else
				{
					htmltext = "blacksmith_hilda_q0618_0203.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final long count = qs.getQuestItemsCount(VACUALITE_ORE);
		
		if (Rnd.chance(CHANCE_FOR_QUEST_ITEMS) && (count < 50))
		{
			qs.giveItems(VACUALITE_ORE, 1);
			
			if (count == 49)
			{
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
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
