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

public class CampaignEvent
{
	public int _campaignId;
	public String _campaignName;
	public int _step;
	public int _participantsCount;
	public int _remainingTime;
	public int _currentProgress;
	public int _maxProgress;
	public CampaignTask _campaignTasks;
	private final Player _player = null;
	
	public CampaignEvent(int id, String name, int step)
	{
		_campaignId = id;
		_campaignName = name;
		_step = step;
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public String getCampaignName()
	{
		return _campaignName;
	}
	
	public int getId()
	{
		return _campaignId;
	}
	
	public int getStep()
	{
		return _step;
	}
	
	public int getParticipantsCount()
	{
		return _participantsCount;
	}
	
	public int getRemainingTime()
	{
		return _remainingTime;
	}
	
	public int getCurrentProgress()
	{
		return _currentProgress;
	}
	
	public int getMaxProgress()
	{
		return _maxProgress;
	}
	
	public CampaignTask getCampaignTasks()
	{
		return _campaignTasks;
	}
	
	private final int rewardId = 33501;
	
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
