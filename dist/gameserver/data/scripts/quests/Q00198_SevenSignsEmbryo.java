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
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q00198_SevenSignsEmbryo extends Quest implements ScriptFile
{
	// Npcs
	private static final int Wood = 32593;
	private static final int Franz = 32597;
	private static final int Jaina = 32582;
	// Monster
	private static final int ShilensEvilThoughtsCapt = 27346;
	// Items
	private static final int PieceOfDoubt = 14355;
	private static final int DawnsBracelet = 15312;
	private static final int Adena = 57;
	// Others
	private static final int instanceId = 113;
	final Location setcloc = new Location(-23734, -9184, -5384, 0);
	
	public Q00198_SevenSignsEmbryo()
	{
		super(false);
		addStartNpc(Wood);
		addTalkId(Wood, Franz, Jaina);
		addKillId(ShilensEvilThoughtsCapt);
		addQuestItem(PieceOfDoubt);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "wood_q198_2.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "wood_q198_3.htm":
				enterInstance(player);
				if (qs.get("embryo") != null)
				{
					qs.unset("embryo");
				}
				break;
			
			case "franz_q198_3.htm":
				NpcInstance embryo = player.getReflection().addSpawnWithoutRespawn(ShilensEvilThoughtsCapt, setcloc, 0);
				qs.set("embryo", 1);
				Functions.npcSay(npc, player.getName() + "! You should kill this monster! I'll try to help!");
				Functions.npcSay(embryo, "This is not yours.");
				embryo.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 500);
				break;
			
			case "wood_q198_8.htm":
				enterInstance(player);
				break;
			
			case "franz_q198_5.htm":
				Functions.npcSay(npc, "We will be with you always...");
				qs.takeItems(PieceOfDoubt, -1);
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "jaina_q198_2.htm":
				player.getReflection().collapse();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case Wood:
				final QuestState state = player.getQuestState(Q00197_SevenSignsTheSacredBookOfSeal.class);
				if (cond == 0)
				{
					if ((player.getLevel() >= 79) && (state != null) && state.isCompleted())
					{
						htmltext = "wood_q198_1.htm";
					}
					else
					{
						htmltext = "wood_q198_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = "wood_q198_2a.htm";
				}
				else if (cond == 3)
				{
					if (player.getBaseClassId() == player.getActiveClassId())
					{
						qs.addExpAndSp(67500000, 15000000);
						qs.giveItems(DawnsBracelet, 1);
						qs.giveItems(Adena, 1500000);
						qs.setState(COMPLETED);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
						htmltext = "wood_q198_4.htm";
					}
					else
					{
						htmltext = "subclass_forbidden.htm";
					}
				}
				break;
			
			case Franz:
				if (cond == 1)
				{
					if ((qs.get("embryo") == null) || (Integer.parseInt(qs.get("embryo")) != 1))
					{
						htmltext = "franz_q198_1.htm";
					}
					else
					{
						htmltext = "franz_q198_3a.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "franz_q198_4.htm";
				}
				else
				{
					htmltext = "franz_q198_6.htm";
				}
				break;
			
			case Jaina:
				htmltext = "jaina_q198_1.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (qs.getCond() == 1)
		{
			Functions.npcSay(npc, player.getName() + ", I'm leaving now. But we shall meet again!");
			qs.set("embryo", 2);
			qs.setCond(2);
			qs.giveItems(PieceOfDoubt, 1);
			player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_EMBRYO);
		}
		
		return null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instanceId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instanceId))
		{
			ReflectionUtils.enterReflection(player, instanceId);
		}
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
