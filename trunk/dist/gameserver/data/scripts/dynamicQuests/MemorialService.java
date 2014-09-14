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

import lineage2.gameserver.listener.actor.player.OnSocialActionListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.dynamic.DynamicQuest;
import lineage2.gameserver.network.clientpackets.RequestActionUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MemorialService extends DynamicQuest implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(MemorialService.class);
	// ID
	private static final int QUEST_ID = 3;
	// private static final String START_TIME = "00 23 * * Mon";
	private static final String START_TIME = "00 17 * * *";
	private static final int MIN_LEVEL = 40;
	private static final int MAX_LEVEL = 99;
	private static final int DURATION = 3600;
	// ID
	private static final int SORROW_EMOTE = 301;
	private static final int PERFORM_POLITE = 302;
	// NPC
	private static final int PRIEST_KHYBER = 33330;
	private static final int GUARD_JANSON = 33331;
	private static final String SPAWN_GROUP = "memorial_service_npc";
	private static final int MAX_TASK_POINT = 1;
	private static final int REWARD_1 = 33501;
	private final OnSocialActionListener socialActionListener = new OnSocialActionListenerImpl();
	
	public MemorialService()
	{
		super(QUEST_ID, DURATION);
		addSpawns(SPAWN_GROUP);
		addTask(SORROW_EMOTE, MAX_TASK_POINT, TASK_INCREASE_MODE_ONCE_PER_CHAR);
		addTask(PERFORM_POLITE, MAX_TASK_POINT, TASK_INCREASE_MODE_ONCE_PER_CHAR);
		addReward(REWARD_1, 1);
		addEliteReward(REWARD_1, 2, 2);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		initSchedulingPattern(START_TIME);
	}
	
	@Override
	protected boolean onStartCondition()
	{
		return true;
	}
	
	@Override
	protected void onStart()
	{
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
					return "dc0003_start001.htm";
				}
				
				return "dc0003_context001.htm";
			}
			else if (isSuccessed())
			{
				boolean rewardReceived = rewardReceived(player);
				
				if (rewardReceived)
				{
					return "dc0003_reward_received001.htm";
				}
				
				return "dc0003_reward001.htm";
			}
			else
			{
				return "dc0003_failed001.htm";
			}
		}
		
		return null;
	}
	
	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if (getParticipants().contains(player.getObjectId()))
		{
			addParticipant(player);
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
		
		if (event.equals("Accept"))
		{
			addParticipant(player);
			response = "dc0003_context001.htm";
		}
		else if (event.equals("Reward"))
		{
			tryReward(player);
			response = "dc0003_reward_received001.htm";
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
		player.getListeners().add(socialActionListener);
	}
	
	@Override
	protected void onRemoveParticipant(Player player)
	{
		player.getListeners().remove(socialActionListener);
	}
	
	@Override
	protected boolean isZoneQuest()
	{
		return false;
	}
	
	@Override
	public void onLoad()
	{
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: Memorial Service - Campaign");
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private final class OnSocialActionListenerImpl implements OnSocialActionListener
	{
		public OnSocialActionListenerImpl()
		{
		}
		
		@Override
		public void onSocialAction(Player player, GameObject target, RequestActionUse.Action action)
		{
			if ((target != null) && target.isNpc() && player.isInRange(target, Creature.INTERACTION_DISTANCE))
			{
				NpcInstance npc = (NpcInstance) target;
				
				switch (npc.getId())
				{
					case PRIEST_KHYBER:
						if (action.value == SocialAction.SORROW)
						{
							increaseTaskPoint(SORROW_EMOTE, player, 1);
						}
						
						break;
					
					case GUARD_JANSON:
						if (action.value == SocialAction.BOW)
						{
							increaseTaskPoint(PERFORM_POLITE, player, 1);
						}
						
						break;
				}
			}
		}
	}
}
