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

public class Q00627_HeartInSearchOfPower extends Quest implements ScriptFile
{
	// Npcs
	private static final int M_NECROMANCER = 31518;
	private static final int ENFEUX = 31519;
	// Items
	private static final int SEAL_OF_LIGHT = 7170;
	private static final int GEM_OF_SUBMISSION = 7171;
	private static final int GEM_OF_SAINTS = 7172;
	private static final int MOLD_HARDENER = 4041;
	private static final int ENRIA = 4042;
	private static final int ASOFE = 4043;
	private static final int THONS = 4044;
	
	public Q00627_HeartInSearchOfPower()
	{
		super(true);
		addStartNpc(M_NECROMANCER);
		addTalkId(M_NECROMANCER, ENFEUX);
		addQuestItem(GEM_OF_SUBMISSION);
		
		for (int mobs = 21520; mobs <= 21541; mobs++)
		{
			addKillId(mobs);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "dark_necromancer_q0627_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "dark_necromancer_q0627_0201.htm":
				qs.takeItems(GEM_OF_SUBMISSION, 300);
				qs.giveItems(SEAL_OF_LIGHT, 1, false);
				qs.setCond(3);
				break;
			
			case "enfeux_q0627_0301.htm":
				qs.takeItems(SEAL_OF_LIGHT, 1);
				qs.giveItems(GEM_OF_SAINTS, 1, false);
				qs.setCond(4);
				break;
			
			case "dark_necromancer_q0627_0401.htm":
				qs.takeItems(GEM_OF_SAINTS, 1);
				break;
			
			default:
				switch (event)
				{
					case "627_11":
						htmltext = "dark_necromancer_q0627_0402.htm";
						qs.giveItems(ADENA_ID, 100000, true);
						break;
					
					case "627_12":
						htmltext = "dark_necromancer_q0627_0402.htm";
						qs.giveItems(ASOFE, 13, true);
						break;
					
					case "627_13":
						htmltext = "dark_necromancer_q0627_0402.htm";
						qs.giveItems(THONS, 13, true);
						break;
					
					case "627_14":
						htmltext = "dark_necromancer_q0627_0402.htm";
						qs.giveItems(ENRIA, 6, true);
						break;
					
					case "627_15":
						htmltext = "dark_necromancer_q0627_0402.htm";
						qs.giveItems(MOLD_HARDENER, 3, true);
						break;
				}
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		switch (npcId)
		{
			case M_NECROMANCER:
				if (cond == 0)
				{
					if ((qs.getPlayer().getLevel() >= 60) && (qs.getPlayer().getLevel() <= 71))
					{
						htmltext = "dark_necromancer_q0627_0101.htm";
					}
					else
					{
						htmltext = "dark_necromancer_q0627_0103.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "dark_necromancer_q0627_0106.htm";
				}
				else if (qs.getQuestItemsCount(GEM_OF_SUBMISSION) == 300)
				{
					htmltext = "dark_necromancer_q0627_0105.htm";
				}
				else if (qs.getQuestItemsCount(GEM_OF_SAINTS) > 0)
				{
					htmltext = "dark_necromancer_q0627_0301.htm";
				}
				break;
			
			case ENFEUX:
				if (qs.getQuestItemsCount(SEAL_OF_LIGHT) > 0)
				{
					htmltext = "enfeux_q0627_0201.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final long count = qs.getQuestItemsCount(GEM_OF_SUBMISSION);
		
		if ((qs.getCond() == 1) && (count < 300))
		{
			qs.giveItems(GEM_OF_SUBMISSION, 1);
			
			if (count == 299)
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
