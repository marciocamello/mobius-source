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
package instances;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Camelion
 * @modified KilRoy
 */
public final class HarnakUndergroundRuins extends Reflection
{
	private static final int DOOR1_ID = 16240100;
	private static final int DOOR2_ID = 16240102;
	private static final String ZONE_1 = "[harnak_underground_4pf_1]";
	private static final String ZONE_2 = "[harnak_underground_4pf_2]";
	private static final int RAKZAN_ID = 27440;
	private static final int DEFENSE_SKILL_ID = 14700;
	private static final String FIRST_ROOM_SECOND_GROUP = "2_group";
	private static final String SECOND_ROOM_FIRST_GROUP = "3_group_1";
	private static final String SECOND_ROOM_SOURCE_POWER = "3_group_source_power";
	private static final String THIRD_ROOM_GROUP = "4_group";
	private static final String THIRD_ROOM_SEALS = "4_group_seal";
	private static final String THIRD_ROOM_MINIONS = "4_group_minion";
	private static final String HERMUNKUS_GROUP = "hermunkus";
	private boolean introShowed = false;
	private volatile int first_room_mobs_count = 8;
	private int secondRoomGroup = 0;
	int classId = -1;
	private ScheduledFuture<?> failTask;
	private final int state;
	
	public HarnakUndergroundRuins(int state)
	{
		this.state = state;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		
		if (state == 1)
		{
			if (!introShowed)
			{
				ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.AN_INTRUDER_INTERESTING), 2500);
				ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.PROVE_YOUR_WORTH), 5000);
				ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.PROVE_YOUR_WORTH), 7500);
				ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ONLY_THOSE_STRONG_ENOUGH_SHALL_PROCEED), 8500);
				ThreadPoolManager.getInstance().schedule(new SpawnNpcTask(), 7500);
				
				for (ClassId classId1 : ClassId.VALUES)
				{
					if ((classId1.getClassLevel() == ClassLevel.Fourth) && classId1.childOf(player.getClassId()))
					{
						classId = classId1.getId();
						break;
					}
				}
				
				introShowed = true;
			}
		}
		else if (state == 2)
		{
			spawnByGroup(HERMUNKUS_GROUP);
			openDoor(DOOR1_ID);
			openDoor(DOOR2_ID);
		}
	}
	
	public void decreaseFirstRoomMobsCount()
	{
		if (--first_room_mobs_count == 0)
		{
			openDoor(DOOR1_ID);
			spawnByGroup(FIRST_ROOM_SECOND_GROUP);
			Zone z = getZone(ZONE_1);
			
			if (z != null)
			{
				z.setActive(true);
				z.addListener(new ZoneListener(1));
			}
		}
	}
	
	public void increaseSecondRoomGroup()
	{
		secondRoomGroup++;
		
		if (secondRoomGroup == 2)
		{
			ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.I_MUST_GO_HELP_SOME_MORE), 100);
			Skill skill = SkillTable.getInstance().getInfo(DEFENSE_SKILL_ID, 1);
			
			for (Player player : getPlayers())
			{
				skill.getEffects(player, player, false, false);
			}
			
			spawnByGroup(SECOND_ROOM_SOURCE_POWER);
		}
		else if (secondRoomGroup == 4)
		{
			openDoor(DOOR2_ID);
			Zone z = getZone(ZONE_2);
			
			if (z != null)
			{
				z.setActive(true);
				z.addListener(new ZoneListener(2));
			}
		}
	}
	
	public void startLastStage()
	{
		for (Player player : getPlayers())
		{
			player.sendPacket(new ExSendUIEvent(player, 0, 0, 60, 0, NpcString.REMAINING_TIME));
			player.sendPacket(new ExShowScreenMessage(NpcString.NO_THE_SEAL_CONTROLS_HAVE_BEEN_EXPOSED_GUARDS_PROTECT_THE_SEAL_CONTROLS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
		}
		
		ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(THIRD_ROOM_MINIONS), 1);
		failTask = ThreadPoolManager.getInstance().schedule(new FailTask(), 60000);
		spawnByGroup(THIRD_ROOM_SEALS);
	}
	
	public void successEndInstance()
	{
		if (failTask != null)
		{
			failTask.cancel(true);
		}
		
		despawnByGroup(THIRD_ROOM_SEALS);
		despawnByGroup(THIRD_ROOM_GROUP);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId);
		despawnByGroup(HERMUNKUS_GROUP);
		
		for (Player p : getPlayers())
		{
			p.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_BOSS_ENDING_A);
		}
		
		ThreadPoolManager.getInstance().schedule(() -> spawnByGroup(HERMUNKUS_GROUP), 25050L);
	}
	
	private class ScreenMessageTask extends RunnableImpl
	{
		private final NpcString msg;
		
		public ScreenMessageTask(NpcString msg)
		{
			this.msg = msg;
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(msg, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
			}
		}
	}
	
	private class SpawnNpcTask extends RunnableImpl
	{
		public SpawnNpcTask()
		{
		}
		
		@Override
		public void runImpl()
		{
			spawnByGroup("1_group");
			ThreadPoolManager.getInstance().schedule(() ->
			{
				List<NpcInstance> npcs = getAllByNpcId(RAKZAN_ID, true);
				
				if (!npcs.isEmpty())
				{
					npcs.get(0).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "SELECT_ME");
				}
			}, 3000);
		}
	}
	
	private class SpawnNpcByPlayerClass extends RunnableImpl
	{
		private final String group;
		
		public SpawnNpcByPlayerClass(String group)
		{
			this.group = group;
		}
		
		@Override
		public void runImpl()
		{
			spawnByGroup(group + "_" + classId);
		}
	}
	
	private class ZoneListener implements OnZoneEnterLeaveListener
	{
		private final int state;
		
		public ZoneListener(int state)
		{
			this.state = state;
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			if (actor.isPlayer())
			{
				if (state == 1)
				{
					ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString._PROVE_YOUR_WORTH), 100);
					ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ARE_YOU_STRONG_OR_WEAK_OF_THE_LIGHT_OR_DARKNESS), 2600);
					ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.ONLY_THOSE_OF_LIGHT_MAY_PASS_OTHERS_MUST_PROVE_THEIR_STRENGTH), 5100);
					ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(SECOND_ROOM_FIRST_GROUP), 6900);
				}
				else if (state == 2)
				{
					ThreadPoolManager.getInstance().schedule(new SpawnThirdRoom(), 28000);
					
					for (Player p : getPlayers())
					{
						p.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_BOSS_OPENING);
					}
				}
			}
			
			zone.setActive(false);
			zone.removeListener(this);
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
		}
	}
	
	private class SpawnThirdRoom extends RunnableImpl
	{
		public SpawnThirdRoom()
		{
		}
		
		@Override
		public void runImpl()
		{
			spawnByGroup(THIRD_ROOM_GROUP);
		}
	}
	
	private class FailTask extends RunnableImpl
	{
		public FailTask()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (NpcInstance npc : getNpcs())
			{
				npc.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "FAIL_INSTANCE");
			}
			
			for (Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_BOSS_ENDING_B);
			}
			
			ThreadPoolManager.getInstance().schedule(new EndTask(), 13500);
		}
	}
	
	private class EndTask extends RunnableImpl
	{
		public EndTask()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player p : getPlayers())
			{
				p.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
			}
		}
	}
}
