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
import lineage2.gameserver.utils.Util;

public class Q00451_LuciensAltar extends Quest implements ScriptFile
{
	// Npcs
	private static final int DAICHIR = 30537;
	private static final int ALTAR_1 = 32706;
	private static final int ALTAR_2 = 32707;
	private static final int ALTAR_3 = 32708;
	private static final int ALTAR_4 = 32709;
	private static final int ALTAR_5 = 32710;
	private static final int[] ALTARS = new int[]
	{
		ALTAR_1,
		ALTAR_2,
		ALTAR_3,
		ALTAR_4,
		ALTAR_5
	};
	// Items
	private static final int REPLENISHED_BEAD = 14877;
	private static final int DISCHARGED_BEAD = 14878;
	
	public Q00451_LuciensAltar()
	{
		super(false);
		addStartNpc(DAICHIR);
		addTalkId(ALTARS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("30537-03.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.giveItems(REPLENISHED_BEAD, 5);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
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
		final Player player = qs.getPlayer();
		
		if (npcId == DAICHIR)
		{
			switch (cond)
			{
				case 0:
					if (player.getLevel() < 80)
					{
						htmltext = "30537-00.htm";
						qs.exitCurrentQuest(true);
					}
					else if (!canEnter(player))
					{
						htmltext = "30537-06.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "30537-01.htm";
					}
					break;
				
				case 1:
					htmltext = "30537-04.htm";
					break;
				
				case 2:
					htmltext = "30537-05.htm";
					qs.addExpAndSp(13773960, 16232820);
					qs.giveItems(ADENA_ID, 742800);
					qs.takeItems(DISCHARGED_BEAD, -1);
					qs.exitCurrentQuest(true);
					qs.playSound(SOUND_FINISH);
					qs.getPlayer().setVar(getName(), String.valueOf(System.currentTimeMillis()), -1);
					break;
			}
		}
		else if ((cond == 1) && Util.contains(ALTARS, npcId))
		{
			switch (npcId)
			{
				case ALTAR_1:
					if (qs.getInt("Altar1") < 1)
					{
						htmltext = "recharge.htm";
						onAltarCheck(qs);
						qs.set("Altar1", 1);
					}
					break;
				
				case ALTAR_2:
					if (qs.getInt("Altar2") < 1)
					{
						htmltext = "recharge.htm";
						onAltarCheck(qs);
						qs.set("Altar2", 1);
					}
					break;
				
				case ALTAR_3:
					if (qs.getInt("Altar3") < 1)
					{
						htmltext = "recharge.htm";
						onAltarCheck(qs);
						qs.set("Altar3", 1);
					}
					break;
				
				case ALTAR_4:
					if (qs.getInt("Altar4") < 1)
					{
						htmltext = "recharge.htm";
						onAltarCheck(qs);
						qs.set("Altar4", 1);
					}
					break;
				
				case ALTAR_5:
					if (qs.getInt("Altar5") < 1)
					{
						htmltext = "recharge.htm";
						onAltarCheck(qs);
						qs.set("Altar5", 1);
					}
					break;
				
				default:
					htmltext = "findother.htm";
					break;
			}
		}
		
		return htmltext;
	}
	
	private void onAltarCheck(QuestState qs)
	{
		qs.takeItems(REPLENISHED_BEAD, 1);
		qs.giveItems(DISCHARGED_BEAD, 1);
		qs.playSound(SOUND_ITEMGET);
		
		if (qs.getQuestItemsCount(DISCHARGED_BEAD) >= 5)
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
		}
	}
	
	private boolean canEnter(Player player)
	{
		if (player.isGM())
		{
			return true;
		}
		
		final String var = player.getVar(getName());
		
		if (var == null)
		{
			return true;
		}
		
		return (Long.parseLong(var) - System.currentTimeMillis()) > (24 * 60 * 60 * 1000);
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
