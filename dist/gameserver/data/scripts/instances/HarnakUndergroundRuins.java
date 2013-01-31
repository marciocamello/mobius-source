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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HarnakUndergroundRuins extends Reflection
{
	/**
	 * Field DOOR1_ID. (value is 16240100)
	 */
	private static final int DOOR1_ID = 16240100;
	/**
	 * Field DOOR2_ID. (value is 16240102)
	 */
	private static final int DOOR2_ID = 16240102;
	/**
	 * Field ZONE_1. (value is ""[harnak_underground_4pf_1]"")
	 */
	private static final String ZONE_1 = "[harnak_underground_4pf_1]";
	/**
	 * Field ZONE_2. (value is ""[harnak_underground_4pf_2]"")
	 */
	private static final String ZONE_2 = "[harnak_underground_4pf_2]";
	/**
	 * Field RAKZAN_ID. (value is 27440)
	 */
	private static final int RAKZAN_ID = 27440;
	/**
	 * Field DEFENSE_SKILL_ID. (value is 14700)
	 */
	private static final int DEFENSE_SKILL_ID = 14700;
	/**
	 * Field FIRST_ROOM_SECOND_GROUP. (value is ""2_group"")
	 */
	private static final String FIRST_ROOM_SECOND_GROUP = "2_group";
	/**
	 * Field SECOND_ROOM_FIRST_GROUP. (value is ""3_group_1"")
	 */
	private static final String SECOND_ROOM_FIRST_GROUP = "3_group_1";
	/**
	 * Field SECOND_ROOM_SOURCE_POWER. (value is ""3_group_source_power"")
	 */
	private static final String SECOND_ROOM_SOURCE_POWER = "3_group_source_power";
	/**
	 * Field THIRD_ROOM_GROUP. (value is ""4_group"")
	 */
	private static final String THIRD_ROOM_GROUP = "4_group";
	/**
	 * Field THIRD_ROOM_SEALS. (value is ""4_group_seal"")
	 */
	private static final String THIRD_ROOM_SEALS = "4_group_seal";
	/**
	 * Field THIRD_ROOM_MINIONS. (value is ""4_group_minion"")
	 */
	private static final String THIRD_ROOM_MINIONS = "4_group_minion";
	/**
	 * Field HERMUNKUS_GROUP. (value is ""hermunkus"")
	 */
	private static final String HERMUNKUS_GROUP = "hermunkus";
	/**
	 * Field introShowed.
	 */
	private boolean introShowed = false;
	/**
	 * Field first_room_mobs_count.
	 */
	private volatile int first_room_mobs_count = 8;
	/**
	 * Field secondRoomGroup.
	 */
	private int secondRoomGroup = 0;
	/**
	 * Field classId_139.
	 */
	int classId_139 = 139;
	/**
	 * Field classId_140.
	 */
	int classId_140 = 140;
	/**
	 * Field classId_141.
	 */
	int classId_141 = 141;
	/**
	 * Field classId_142.
	 */
	int classId_142 = 142;
	/**
	 * Field classId_143.
	 */
	int classId_143 = 143;
	/**
	 * Field classId_144.
	 */
	int classId_144 = 144;
	/**
	 * Field classId_145.
	 */
	int classId_145 = 145;
	/**
	 * Field classId_146.
	 */
	int classId_146 = 146;
	/**
	 * Field failTask.
	 */
	private ScheduledFuture<?> failTask;
	/**
	 * Field state.
	 */
	private final int state;
	
	/**
	 * Constructor for HarnakUndergroundRuins.
	 * @param state int
	 */
	public HarnakUndergroundRuins(int state)
	{
		this.state = state;
	}
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 */
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
					if (player.getClassId().isOfLevel(ClassLevel.Second) && classId1.childOf(player.getClassId()))
					{
						classId_139 = classId1.getId();
						classId_140 = classId1.getId();
						classId_141 = classId1.getId();
						classId_142 = classId1.getId();
						classId_143 = classId1.getId();
						classId_144 = classId1.getId();
						classId_145 = classId1.getId();
						classId_146 = classId1.getId();
						break;
					}
				}
				introShowed = true;
			}
		}
		else if (state == 2)
		{
			spawnByGroup(HERMUNKUS_GROUP);
			getDoor(DOOR1_ID).openMe();
			getDoor(DOOR2_ID).openMe();
		}
	}
	
	/**
	 * Method decreaseFirstRoomMobsCount.
	 */
	public void decreaseFirstRoomMobsCount()
	{
		if (--first_room_mobs_count == 0)
		{
			getDoor(DOOR1_ID).openMe();
			spawnByGroup(FIRST_ROOM_SECOND_GROUP);
			Zone z = getZone(ZONE_1);
			if (z != null)
			{
				z.setActive(true);
				z.addListener(new ZoneListener(1));
			}
		}
	}
	
	/**
	 * Method increaseSecondRoomGroup.
	 */
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
			getDoor(DOOR2_ID).openMe();
			Zone z = getZone(ZONE_2);
			if (z != null)
			{
				z.setActive(true);
				z.addListener(new ZoneListener(2));
			}
		}
	}
	
	/**
	 * Method startLastStage.
	 */
	public void startLastStage()
	{
		for (Player player : getPlayers())
		{
			player.sendPacket(new ExSendUIEvent(player, false, false, 60, 0, NpcString.REMAINING_TIME));
			player.sendPacket(new ExShowScreenMessage(NpcString.NO_THE_SEAL_CONTROLS_HAVE_BEEN_EXPOSED_GUARDS_PROTECT_THE_SEAL_CONTROLS, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
			ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(player.getClassId(), THIRD_ROOM_MINIONS), 1);
			break;
		}
		failTask = ThreadPoolManager.getInstance().schedule(new FailTask(), 60000);
		spawnByGroup(THIRD_ROOM_SEALS);
	}
	
	/**
	 * Method successEndInstance.
	 */
	public void successEndInstance()
	{
		if (failTask != null)
		{
			failTask.cancel(true);
		}
		despawnByGroup(THIRD_ROOM_SEALS);
		despawnByGroup(THIRD_ROOM_GROUP);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_139);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_140);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_141);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_142);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_143);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_144);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_145);
		despawnByGroup(THIRD_ROOM_MINIONS + "_" + classId_146);
		for (Player p : getPlayers())
		{
			p.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_BOSS_ENDING_A);
		}
		spawnByGroup(HERMUNKUS_GROUP);
	}
	
	/**
	 * @author Mobius
	 */
	private class ScreenMessageTask extends RunnableImpl
	{
		/**
		 * Field msg.
		 */
		private final NpcString msg;
		
		/**
		 * Constructor for ScreenMessageTask.
		 * @param msg NpcString
		 */
		public ScreenMessageTask(NpcString msg)
		{
			this.msg = msg;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(msg, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnNpcTask extends RunnableImpl
	{
		/**
		 * Constructor for SpawnNpcTask.
		 */
		public SpawnNpcTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnByGroup("1_group");
			List<NpcInstance> npcs = getAllByNpcId(RAKZAN_ID, true);
			if (!npcs.isEmpty())
			{
				npcs.get(0).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "SELECT_ME");
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnNpcByPlayerClass extends RunnableImpl
	{
		/**
		 * Field classId.
		 */
		private final ClassId classId;
		/**
		 * Field group.
		 */
		private final String group;
		
		/**
		 * Constructor for SpawnNpcByPlayerClass.
		 * @param classId ClassId
		 * @param group String
		 */
		public SpawnNpcByPlayerClass(ClassId classId, String group)
		{
			this.classId = classId;
			this.group = group;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((classId == ClassId.SHILLIEN_TEMPLAR) || (classId == ClassId.PHOENIX_KNIGHT) || (classId == ClassId.HELL_KNIGHT) || (classId == ClassId.EVAS_TEMPLAR))
			{
				spawnByGroup(group + "_" + classId_139);
			}
			else if ((classId == ClassId.DOOMBRINGER) || (classId == ClassId.MAESTRO) || (classId == ClassId.GRAND_KHAVATARI) || (classId == ClassId.DREADNOUGHT) || (classId == ClassId.DUELIST) || (classId == ClassId.TITAN))
			{
				spawnByGroup(group + "_" + classId_140);
			}
			else if ((classId == ClassId.FORTUNE_SEEKER) || (classId == ClassId.ADVENTURER) || (classId == ClassId.WIND_RIDER) || (classId == ClassId.GHOST_HUNTER))
			{
				spawnByGroup(group + "_" + classId_141);
			}
			else if ((classId == ClassId.SAGITTARIUS) || (classId == ClassId.MOONLIGHT_SENTINEL) || (classId == ClassId.GHOST_SENTINEL) || (classId == ClassId.TRICKSTER))
			{
				spawnByGroup(group + "_" + classId_142);
			}
			else if ((classId == ClassId.F_SOUL_HOUND) || (classId == ClassId.M_SOUL_HOUND) || (classId == ClassId.STORM_SCREAMER) || (classId == ClassId.ARCHMAGE) || (classId == ClassId.SOULTAKER) || (classId == ClassId.MYSTIC_MUSE))
			{
				spawnByGroup(group + "_" + classId_143);
			}
			else if ((classId == ClassId.JUDICATOR) || (classId == ClassId.SPECTRAL_DANCER) || (classId == ClassId.HIEROPHANT) || (classId == ClassId.SWORD_MUSE) || (classId == ClassId.DOMINATOR) || (classId == ClassId.DOOMCRYER))
			{
				spawnByGroup(group + "_" + classId_144);
			}
			else if ((classId == ClassId.ARCANA_LORD) || (classId == ClassId.ELEMENTAL_MASTER) || (classId == ClassId.SPECTRAL_MASTER))
			{
				spawnByGroup(group + "_" + classId_145);
			}
			else if ((classId == ClassId.CARDINAL) || (classId == ClassId.EVAS_SAINT) || (classId == ClassId.SHILLIEN_SAINT))
			{
				spawnByGroup(group + "_" + classId_146);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Field state.
		 */
		private final int state;
		
		/**
		 * Constructor for ZoneListener.
		 * @param state int
		 */
		public ZoneListener(int state)
		{
			this.state = state;
		}
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
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
					ThreadPoolManager.getInstance().schedule(new SpawnNpcByPlayerClass(actor.getPlayer().getClassId(), SECOND_ROOM_FIRST_GROUP), 6900);
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
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnThirdRoom extends RunnableImpl
	{
		/**
		 * Constructor for SpawnThirdRoom.
		 */
		public SpawnThirdRoom()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnByGroup(THIRD_ROOM_GROUP);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class FailTask extends RunnableImpl
	{
		/**
		 * Constructor for FailTask.
		 */
		public FailTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
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
	
	/**
	 * @author Mobius
	 */
	private class EndTask extends RunnableImpl
	{
		/**
		 * Constructor for EndTask.
		 */
		public EndTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
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
