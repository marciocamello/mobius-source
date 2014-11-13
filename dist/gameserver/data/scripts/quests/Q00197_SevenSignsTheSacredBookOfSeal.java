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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00197_SevenSignsTheSacredBookOfSeal extends Quest implements ScriptFile
{
	// Npcs
	private static final int Wood = 32593;
	private static final int Orven = 30857;
	private static final int Leopard = 32594;
	private static final int Lawrence = 32595;
	private static final int Sofia = 32596;
	// Monster
	private static final int ShilensEvilThoughts = 27396;
	// Items
	private static final int PieceofDoubt = 14354;
	private static final int MysteriousHandwrittenText = 13829;
	
	public Q00197_SevenSignsTheSacredBookOfSeal()
	{
		super(false);
		addStartNpc(Wood);
		addTalkId(Wood, Orven, Leopard, Lawrence, Sofia);
		addKillId(ShilensEvilThoughts);
		addQuestItem(PieceofDoubt, MysteriousHandwrittenText);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		String htmltext = event;
		
		switch (event)
		{
			case "wood_q197_2.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "orven_q197_2.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "leopard_q197_2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "lawrence_q197_2.htm":
				NpcInstance mob = qs.addSpawn(ShilensEvilThoughts, 152520, -57502, -3408, 0, 0, 180000);
				Functions.npcSay(mob, "Shilen's power is endless!");
				mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
				qs.set("evilthought", 1);
				break;
			
			case "lawrence_q197_4.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "sofia_q197_2.htm":
				qs.setCond(6);
				qs.giveItems(MysteriousHandwrittenText, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "wood_q197_4.htm":
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.takeItems(PieceofDoubt, -1);
					qs.takeItems(MysteriousHandwrittenText, -1);
					qs.addExpAndSp(10000000, 2500000);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				else
				{
					return "subclass_forbidden.htm";
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
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case Wood:
				final QuestState state = player.getQuestState(Q00196_SevenSignsSealOfTheEmperor.class);
				if (cond == 0)
				{
					if ((player.getLevel() >= 79) && (state != null) && state.isCompleted())
					{
						htmltext = "wood_q197_1.htm";
					}
					else
					{
						htmltext = "wood_q197_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 6)
				{
					htmltext = "wood_q197_3.htm";
				}
				else
				{
					htmltext = "wood_q197_5.htm";
				}
				break;
			
			case Orven:
				if (cond == 1)
				{
					htmltext = "orven_q197_1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "orven_q197_3.htm";
				}
				break;
			
			case Leopard:
				if (cond == 2)
				{
					htmltext = "leopard_q197_1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "leopard_q197_3.htm";
				}
				break;
			
			case Lawrence:
				if (cond == 3)
				{
					if ((qs.get("evilthought") != null) && (Integer.parseInt(qs.get("evilthought")) == 1))
					{
						htmltext = "lawrence_q197_0.htm";
					}
					else
					{
						htmltext = "lawrence_q197_1.htm";
					}
				}
				else if (cond == 4)
				{
					htmltext = "lawrence_q197_3.htm";
				}
				else if (cond == 5)
				{
					htmltext = "lawrence_q197_5.htm";
				}
				break;
			
			case Sofia:
				if (cond == 5)
				{
					htmltext = "sofia_q197_1.htm";
				}
				else if (cond == 6)
				{
					htmltext = "sofia_q197_3.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 3)
		{
			qs.setCond(4);
			qs.playSound(SOUND_ITEMGET);
			qs.giveItems(PieceofDoubt, 1);
			qs.set("evilthought", 2);
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
