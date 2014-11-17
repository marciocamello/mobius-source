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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

/**
 * @author pchayka
 */
public class Q10296_SevenSignsOneWhoSeeksThePowerOfTheSeal extends Quest implements ScriptFile
{
	private static final int Eris = 32792;
	private static final int ElcardiaInzone1 = 32787;
	private static final int EtisEtina = 18949;
	private static final int ElcardiaHome = 32784;
	private static final int Hardin = 30832;
	private static final int Wood = 32593;
	private static final int Franz = 32597;
	private static final Location hiddenLoc = new Location(120744, -87432, -3392);
	
	public Q10296_SevenSignsOneWhoSeeksThePowerOfTheSeal()
	{
		super(false);
		addStartNpc(Eris);
		addTalkId(ElcardiaInzone1, ElcardiaHome, Hardin, Wood, Franz);
		addKillId(EtisEtina);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "eris_q10296_3.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "start_scene":
				qs.setCond(2);
				teleportElcardia(player, hiddenLoc);
				ThreadPoolManager.getInstance().schedule(new Teleport(player), 60500L);
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_BOSS_OPENING);
				return null;
				
			case "teleport_back":
				player.teleToLocation(new Location(76736, -241021, -10832));
				teleportElcardia(player);
				return null;
				
			case "elcardiahome_q10296_3.htm":
				qs.setCond(4);
				break;
			
			case "hardin_q10296_3.htm":
				qs.setCond(5);
				break;
			
			case "enter_instance":
				enterInstance(qs, 146);
				return null;
				
			case "franz_q10296_3.htm":
				if (player.getLevel() >= 81)
				{
					qs.addExpAndSp(125000000, 12500000);
					qs.giveItems(17265, 1);
					qs.setState(COMPLETED);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "franz_q10296_0.htm";
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
		final Player player = qs.getPlayer();
		
		if (!player.isBaseClassActive())
		{
			return "no_subclass_allowed.htm";
		}
		
		switch (npc.getId())
		{
			case Eris:
				if (cond == 0)
				{
					final QuestState state = player.getQuestState(Q10295_SevenSignsSolinasTomb.class);
					
					if ((player.getLevel() >= 81) && (state != null) && state.isCompleted())
					{
						htmltext = "eris_q10296_1.htm";
					}
					else
					{
						htmltext = "eris_q10296_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "eris_q10296_4.htm";
				}
				else if (cond == 2)
				{
					htmltext = "eris_q10296_5.htm";
				}
				else if (cond >= 3)
				{
					htmltext = "eris_q10296_6.htm";
				}
				break;
			
			case ElcardiaInzone1:
				if (cond == 1)
				{
					htmltext = "elcardia_q10296_1.htm";
				}
				else if (cond == 2)
				{
					if (qs.getInt("EtisKilled") == 0)
					{
						htmltext = "elcardia_q10296_1.htm";
					}
					else
					{
						qs.setCond(3);
						htmltext = "elcardia_q10296_2.htm";
					}
				}
				else if (cond >= 3)
				{
					htmltext = "elcardia_q10296_4.htm";
				}
				break;
			
			case ElcardiaHome:
				if (cond == 3)
				{
					htmltext = "elcardiahome_q10296_1.htm";
				}
				else if (cond >= 4)
				{
					htmltext = "elcardiahome_q10296_3.htm";
				}
				break;
			
			case Hardin:
				if (cond == 4)
				{
					htmltext = "hardin_q10296_1.htm";
				}
				else if (cond == 5)
				{
					htmltext = "hardin_q10296_4.htm";
				}
				break;
			
			case Wood:
				if (cond == 5)
				{
					htmltext = "wood_q10296_1.htm";
				}
				break;
			
			case Franz:
				if (cond == 5)
				{
					htmltext = "franz_q10296_1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		qs.set("EtisKilled", 1);
		
		for (NpcInstance n : qs.getPlayer().getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(new Location(120664, -86968, -3392));
			}
		}
		
		ThreadPoolManager.getInstance().schedule(new ElcardiaTeleport(qs.getPlayer()), 60500L);
		qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_BOSS_CLOSING);
		
		return null;
	}
	
	void teleportElcardia(Player player)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(Location.findPointToStay(player, 60));
				
				if (n.isBlocked())
				{
					n.unblock();
				}
			}
		}
	}
	
	private void teleportElcardia(Player player, Location loc)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(loc);
				n.block();
			}
		}
	}
	
	private class Teleport extends RunnableImpl
	{
		Player _player;
		
		public Teleport(Player player)
		{
			_player = player;
		}
		
		@Override
		public void runImpl()
		{
			_player.teleToLocation(new Location(76736, -241021, -10832));
			teleportElcardia(_player);
		}
	}
	
	private class ElcardiaTeleport extends RunnableImpl
	{
		Player _player;
		
		public ElcardiaTeleport(Player player)
		{
			_player = player;
		}
		
		@Override
		public void runImpl()
		{
			teleportElcardia(_player);
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