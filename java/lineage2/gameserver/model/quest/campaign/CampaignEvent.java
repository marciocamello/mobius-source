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
package lineage2.gameserver.model.quest.campaign;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CampaignEvent
{
	/**
	 * Field _campaignId.
	 */
	public int _campaignId;
	/**
	 * Field _campaignName.
	 */
	public String _campaignName;
	/**
	 * Field _step.
	 */
	public int _step;
	/**
	 * Field _participantsCount.
	 */
	public int _participantsCount;
	/**
	 * Field _remainingTime.
	 */
	public int _remainingTime;
	/**
	 * Field _currentProgress.
	 */
	public int _currentProgress;
	/**
	 * Field _maxProgress.
	 */
	public int _maxProgress;
	/**
	 * Field _campaignTasks.
	 */
	public CampaignTask _campaignTasks;
	/**
	 * Field _player.
	 */
	private final Player _player = null;
	
	/**
	 * Constructor for CampaignEvent.
	 * @param id int
	 * @param name String
	 * @param step int
	 */
	public CampaignEvent(int id, String name, int step)
	{
		_campaignId = id;
		_campaignName = name;
		_step = step;
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * Method getCampaignName.
	 * @return String
	 */
	public String getCampaignName()
	{
		return _campaignName;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _campaignId;
	}
	
	/**
	 * Method getStep.
	 * @return int
	 */
	public int getStep()
	{
		return _step;
	}
	
	/**
	 * Method getParticipantsCount.
	 * @return int
	 */
	public int getParticipantsCount()
	{
		return _participantsCount;
	}
	
	/**
	 * Method getRemainingTime.
	 * @return int
	 */
	public int getRemainingTime()
	{
		return _remainingTime;
	}
	
	/**
	 * Method getCurrentProgress.
	 * @return int
	 */
	public int getCurrentProgress()
	{
		return _currentProgress;
	}
	
	/**
	 * Method getMaxProgress.
	 * @return int
	 */
	public int getMaxProgress()
	{
		return _maxProgress;
	}
	
	/**
	 * Method getCampaignTasks.
	 * @return CampaignTask
	 */
	public CampaignTask getCampaignTasks()
	{
		return _campaignTasks;
	}
	
	/**
	 * Field rewardId.
	 */
	private final int rewardId = 33501;
	
	/**
	 * Method giveItems.
	 * @param count long
	 * @param rate boolean
	 */
	public void giveItems(long count, boolean rate)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (count <= 0)
		{
			count = 1;
		}
		ItemFunctions.addItem(player, rewardId, count * getParticipantsCount(), true);
		player.sendChanges();
	}
}
