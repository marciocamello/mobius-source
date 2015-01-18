/*
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package lineage2.gameserver.taskmanager.tasks;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExWorldChatCnt;
import lineage2.gameserver.taskmanager.Task;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.TaskManager.ExecutedTask;
import lineage2.gameserver.taskmanager.TaskTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 */
public class TaskWorldChat extends Task
{
	private static final Logger _log = LoggerFactory.getLogger(TaskWorldChat.class);
	private static final String NAME = "sp_worldchat";
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return NAME;
	}
	
	/**
	 * Method onTimeElapsed.
	 * @param task ExecutedTask
	 */
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		_log.info("World Chat Global Task: launched.");
		
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.setUsedWorldChatPoints(0);
			player.sendPacket(new ExWorldChatCnt(player));
		}
		
		_log.info("World Chat Global Task: completed.");
	}
	
	/**
	 * Method initializate.
	 */
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask("sp_worldchat", TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}