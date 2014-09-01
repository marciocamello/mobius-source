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
package dynamicQuests;

import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.quest.dynamic.DynamicQuest;
import lineage2.gameserver.network.serverpackets.ExDynamicQuestPacket;
import lineage2.gameserver.network.serverpackets.ExDynamicQuestPacket.DynamicQuestInfo;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LabyrinthOfHarnak extends DynamicQuest implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(LabyrinthOfHarnak.class);
	private static final int QUEST_ID = 8;
	private static final int MIN_LEVEL = 85;
	private static final int MAX_LEVEL = 99;
	private static final int DURATION = 10800;
	private static final String START_TIME = "1 * * * *";
	private static final int REWARD = 32726;
	private static final int ELITE_REWARD = 32725;
	private static final int KILL_LOH_MOB = 801;
	private static final int MAX_TASK_POINT = 10000;
	private static final String QUEST_ZONE_FIRST_SECOND = "[loh_first_second]";
	private static final String QUEST_ZONE_THIRD = "[loh_third]";
	private ZoneListener _zoneListener;
	private Zone zoneFirstSecond;
	private Zone zoneThird;
	private final KillListenerImpl _killListener = new KillListenerImpl();
	private static final int DEMONIC_BATHUS = 22939;
	private static final int DEMONIC_CARCASS = 22940;
	private static final int DEMONIC_LOTUS = 22941;
	private static final int DEMONIC_RAKZAN = 22942;
	private static final int DEMONIC_WEISS_KHAN = 22943;
	private static final int DEMONIC_WEISS_ELE = 22944;
	private static final int DEMONIC_BAMONTI = 22945;
	private static final int DEMONIC_SEKNUS = 22946;
	private static final int DEMONIC_NOKTUM = 25773;
	static final int[] LOH_MOBS =
	{
		22939,
		22940,
		22941,
		22942,
		22943,
		22944,
		22945,
		22946,
		25773
	};
	
	public LabyrinthOfHarnak()
	{
		super(QUEST_ID, DURATION);
		addTask(KILL_LOH_MOB, MAX_TASK_POINT, TASK_INCREASE_MODE_NO_LIMIT);
		addReward(REWARD, 1);
		addEliteReward(ELITE_REWARD, 1, 3);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		addZoneCheck(QUEST_ZONE_FIRST_SECOND);
		addZoneCheck(QUEST_ZONE_THIRD);
		initSchedulingPattern(START_TIME);
	}
	
	@Override
	protected boolean isZoneQuest()
	{
		return true;
	}
	
	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		zoneFirstSecond = ReflectionUtils.getZone(QUEST_ZONE_FIRST_SECOND);
		zoneFirstSecond.addListener(_zoneListener);
		zoneThird = ReflectionUtils.getZone(QUEST_ZONE_THIRD);
		zoneThird.addListener(_zoneListener);
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: Labyrinth of Harnak - Zone Quest");
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	protected void onStart()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.isInZone(QUEST_ZONE_FIRST_SECOND) || player.isInZone(QUEST_ZONE_THIRD))
			{
				if (!getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
					addParticipant(player);
				}
			}
		}
	}
	
	@Override
	protected void onStop(boolean success)
	{
		for (int objectId : getParticipants())
		{
			Player player = GameObjectsStorage.getPlayer(objectId);
			
			if (player != null)
			{
				removeParticipant(player);
			}
		}
	}
	
	@Override
	protected void onFinish()
	{
	}
	
	@Override
	protected String onRequestHtml(Player player, boolean participant)
	{
		if (getCurrentStep() == 1)
		{
			if (isStarted())
			{
				if (!participant)
				{
					return "dc0008_01_start001.htm";
				}
				
				return "dc0008_01_context001.htm";
			}
			else if (isSuccessed())
			{
				boolean rewardReceived = rewardReceived(player);
				
				if (rewardReceived)
				{
					return null;
				}
				
				return "dc0008_01_reward001.htm";
			}
			else
			{
				return "dc0008_01_failed001.htm";
			}
		}
		
		return null;
	}
	
	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if (player.isInZone(zoneFirstSecond) || player.isInZone(zoneThird))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onTaskCompleted(int taskId)
	{
	}
	
	@Override
	protected String onDialogEvent(String event, Player player)
	{
		String response = null;
		
		if (event.equals("Reward"))
		{
			tryReward(player);
			// response = null;
		}
		else if (event.endsWith(".htm"))
		{
			response = event;
		}
		
		return response;
	}
	
	@Override
	protected void onAddParticipant(Player player)
	{
		player.getListeners().add(_killListener);
	}
	
	@Override
	protected void onRemoveParticipant(Player player)
	{
		player.getListeners().remove(_killListener);
	}
	
	@Override
	protected boolean onStartCondition()
	{
		return true;
	}
	
	private final class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature character)
		{
			if (zone == null)
			{
				return;
			}
			
			if (!character.isPlayer())
			{
				return;
			}
			
			Player player = character.getPlayer();
			
			if (isStarted() && !isSuccessed())
			{
				if (!getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
					addParticipant(player);
				}
				else
				{
					sendQuestInfoParticipant(player);
				}
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature character)
		{
			if (!character.isPlayer())
			{
				return;
			}
			
			Player player = character.getPlayer();
			
			if (isStarted() && !isSuccessed())
			{
				if (getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
				}
			}
		}
	}
	
	private final class KillListenerImpl implements OnKillListener
	{
		public KillListenerImpl()
		{
		}
		
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if (victim.isPlayer())
			{
				return;
			}
			
			if (!actor.isPlayer())
			{
				return;
			}
			
			if (victim.isNpc() && isStarted() && ArrayUtils.contains(LOH_MOBS, victim.getNpcId()))
			{
				switch (victim.getNpcId())
				{
					case DEMONIC_BATHUS:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_CARCASS:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_LOTUS:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_RAKZAN:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_WEISS_KHAN:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_WEISS_ELE:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_BAMONTI:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_SEKNUS:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					
					case DEMONIC_NOKTUM:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 2);
						break;
				}
			}
		}
		
		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}
}