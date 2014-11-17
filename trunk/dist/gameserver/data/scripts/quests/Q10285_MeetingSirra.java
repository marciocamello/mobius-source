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
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10285_MeetingSirra extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Jinia2 = 32781;
	private static final int Kegor = 32761;
	private static final int Sirra = 32762;
	
	public Q10285_MeetingSirra()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Jinia2, Kegor, Sirra);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "rafforty_q10285_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "enterinstance":
				if (qs.getCond() == 1)
				{
					qs.setCond(2);
				}
				enterInstance(qs.getPlayer(), 141);
				return null;
				
			case "jinia_q10285_02.htm":
				qs.setCond(3);
				break;
			
			case "kegor_q10285_02.htm":
				qs.setCond(4);
				break;
			
			case "sirraspawn":
				qs.setCond(5);
				qs.getPlayer().getReflection().addSpawnWithoutRespawn(Sirra, new Location(-23848, -8744, -5413, 49152), 0);
				for (NpcInstance sirra : qs.getPlayer().getAroundNpc(1000, 100))
				{
					if (sirra.getId() == Sirra)
					{
						Functions.npcSay(sirra, "Вас послушать, получается, что Вы знаете обо всем на свете. Но я больше не могу слушать Ваши мудрствования");
					}
				}
				return null;
				
			case "sirra_q10285_07.htm":
				qs.setCond(6);
				for (NpcInstance sirra : qs.getPlayer().getAroundNpc(1000, 100))
				{
					if (sirra.getId() == 32762)
					{
						sirra.deleteMe();
					}
				}
				break;
			
			case "jinia_q10285_10.htm":
				if (!qs.getPlayer().getReflection().isDefault())
				{
					qs.getPlayer().getReflection().startCollapseTimer(60 * 1000L);
					qs.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
				}
				qs.setCond(7);
				break;
			
			case "exitinstance":
				qs.getPlayer().getReflection().collapse();
				return null;
				
			case "enterfreya":
				qs.setCond(9);
				enterInstance(qs.getPlayer(), 137);
				return null;
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
			case Rafforty:
				if (cond == 0)
				{
					final QuestState state = qs.getPlayer().getQuestState(Q10284_AcquisitionOfDivineSword.class);
					
					if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
					{
						htmltext = "rafforty_q10285_01.htm";
					}
					else
					{
						htmltext = "rafforty_q10285_00.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if ((cond >= 1) && (cond < 7))
				{
					htmltext = "rafforty_q10285_03.htm";
				}
				else if (cond == 10)
				{
					htmltext = "rafforty_q10285_04.htm";
					qs.giveItems(ADENA_ID, 283425);
					qs.addExpAndSp(939075, 83855);
					qs.setState(COMPLETED);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case Jinia:
				switch (cond)
				{
					case 2:
						htmltext = "jinia_q10285_01.htm";
						break;
					
					case 4:
						htmltext = "jinia_q10285_03.htm";
						break;
					
					case 6:
						htmltext = "jinia_q10285_05.htm";
						break;
					
					case 7:
						htmltext = "jinia_q10285_10.htm";
						break;
				}
				break;
			
			case Kegor:
				if (cond == 3)
				{
					htmltext = "kegor_q10285_01.htm";
				}
				break;
			
			case Sirra:
				if (cond == 5)
				{
					htmltext = "sirra_q10285_01.htm";
				}
				break;
			
			case Jinia2:
				if ((cond == 7) || (cond == 8))
				{
					qs.setCond(8);
					htmltext = "jinia2_q10285_01.htm";
				}
				else if (cond == 9)
				{
					htmltext = "jinia2_q10285_02.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private void enterInstance(Player player, int izId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, izId);
			
			if (izId == 137)
			{
				ThreadPoolManager.getInstance().schedule(new FreyaSpawn(newInstance, player), 2 * 60 * 1000L);
			}
		}
	}
	
	private class FreyaSpawn extends RunnableImpl
	{
		private final Player _player;
		private final Reflection _r;
		
		public FreyaSpawn(Reflection r, Player player)
		{
			_r = r;
			_player = player;
		}
		
		@Override
		public void runImpl()
		{
			if (_r != null)
			{
				NpcInstance freya = _r.addSpawnWithoutRespawn(18847, new Location(114720, -117085, -11088, 15956), 0);
				ThreadPoolManager.getInstance().schedule(new FreyaMovie(_player, _r, freya), 2 * 60 * 1000L);
			}
		}
	}
	
	private class FreyaMovie extends RunnableImpl
	{
		Player _player;
		Reflection _r;
		NpcInstance _npc;
		
		public FreyaMovie(Player player, Reflection r, NpcInstance npc)
		{
			_player = player;
			_r = r;
			_npc = npc;
		}
		
		@Override
		public void runImpl()
		{
			for (Spawner sp : _r.getSpawns())
			{
				sp.deleteAll();
			}
			
			if ((_npc != null) && !_npc.isDead())
			{
				_npc.deleteMe();
			}
			
			_player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_FORCED_DEFEAT);
			ThreadPoolManager.getInstance().schedule(new ResetInstance(_player, _r), 23000L);
		}
	}
	
	private class ResetInstance extends RunnableImpl
	{
		Player _player;
		Reflection _r;
		
		public ResetInstance(Player player, Reflection r)
		{
			_player = player;
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			_player.getQuestState(Q10285_MeetingSirra.class).setCond(10);
			_r.collapse();
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
